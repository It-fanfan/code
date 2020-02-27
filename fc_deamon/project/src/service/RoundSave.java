package service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.PeDbRoundExt;
import db.PeDbRoundRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;
import tool.CmTool;
import tool.db.RedisUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 赛场保存一场记录
 */
public class RoundSave implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(RoundSave.class);
    //赛场状态
    private static final String ROUND_REDIS_KEY = "round-status";
    //一场赛场记录
    private String matchKey;

    RoundSave(String matchKey)
    {
        this.matchKey = matchKey;
    }

    /**
     * 更新游戏状态
     *
     * @param info   赛场配置
     * @param status 状态
     */
    static void updateStatus(JSONObject info, int result, String status)
    {
        LOG.debug("updateStatus:" + info.toJSONString() + ",status=" + status);
        String field = info.getString("matchKey");
        info.put("status", status);
        info.put("result", result);
        LOG.debug("matchInfo:" + info.toJSONString());
        //进行移除redis
        if (status.equals("over"))
        {
            //进行保存历史场次
            if (result > 0)
                saveHistoryRecord(info, result);
            if (result <= 0)
            {
                RedisUtils.hdel(ROUND_REDIS_KEY, field);
                return;
            }
        }
        RedisUtils.hset(ROUND_REDIS_KEY, field, info.toJSONString());
    }

    /**
     * 记录保存历史场次
     *
     * @param info   赛场信息
     * @param result 赛场结果
     */
    private static void saveHistoryRecord(JSONObject info, int result)
    {
        PeDbRoundRecord record = new PeDbRoundRecord();
        record.ddGame = info.getInteger("gameCode");
        record.ddRound = info.getString("round");
        record.ddName = info.getString("name");
        record.ddSubmit = new Timestamp(info.getLong("submit"));
        record.ddEnd = new Timestamp(info.getLong("end"));
        record.ddStart = new Timestamp(info.getLong("start"));
        record.ddTime = new Timestamp(System.currentTimeMillis());
        record.ddCode = info.getInteger("code");
        record.ddGroup = info.getBoolean("isGroup");
        record.ddIndex = info.getInteger("index");
        record.ddResult = result;
        record.insert();
    }

    /**
     * 获取赛制信息
     *
     * @param matchKey 赛场信息
     * @return 赛制内容
     */
    static JSONObject getRoundInfo(String matchKey)
    {
        String json = RedisUtils.hget(ROUND_REDIS_KEY, matchKey);
        JSONObject info = null;
        if (json != null)
            info = JSONObject.parseObject(json);
        if (info == null)
            info = new JSONObject();
        return info;
    }

    /**
     * 更新赛场信息
     *
     * @param matches 游戏赛场内容
     */
    static void updateNowRoundRecord(Map<Integer, JSONObject> matches, boolean isMatch)
    {
        matches.forEach((k, v) ->
        {
            String field = String.format("current-g%d", k);
            String json = RedisUtils.hget(ROUND_REDIS_KEY, field);
            JSONObject data = null;
            if (json != null)
            {
                data = JSONObject.parseObject(json);
            }
            if (data == null)
            {
                data = new JSONObject();
            }
            data.put(String.valueOf(isMatch), v.getString("matchKey"));
            RedisUtils.hset(ROUND_REDIS_KEY, field, data.toJSONString());
        });
    }

    @Override
    public void run()
    {
        long current = System.currentTimeMillis();
        //进行关停当前赛场
        JSONObject matchInfo = getRoundInfo(matchKey);
        String status = matchInfo.getString("status");
        long submit = matchInfo.getLong("submit");
        //是否达到结算时间
        if (current >= submit && status.equals("running"))
        {
            //标志该赛制正在结算
            updateStatus(matchInfo, 0, "finish");
            //获取排行信息0,1名
            Set<Tuple> sets = RedisUtils.zrevrangeWithScores(matchKey, 0, -1);
            //已处理结算
            if (sets == null)
            {
                updateStatus(matchInfo, 0, "over");
                return;
            }
            PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(matchInfo.getString("round"));
            if (roundExt == null)
                return;
            JSONObject reward = roundExt.getRoundReward();
            //排名和分数
            JSONArray array = new JSONArray();
            AtomicInteger atomic = new AtomicInteger();
            Vector<String> uids = new Vector<>();
            for (Tuple tuple : sets)
            {
                JSONObject user = new JSONObject();
                //当前分数
                long mark = BigDecimal.valueOf(tuple.getScore()).longValue();
                //用户uid
                String uid = tuple.getElement();
                //排名次数
                int index = atomic.incrementAndGet();
                user.put("mark", mark);
                user.put("uid", uid);
                user.put("index", index);
                if (reward.containsKey(String.valueOf(index)))
                {
                    JSONObject info = reward.getJSONObject(String.valueOf(index));
                    String type = info.getString("type");
                    int value = info.getInteger("value");
                    switch (type)
                    {
                        case "rmb":
                        {
                            value *= 100;
                        }
                        break;
                    }
                    user.put("type", type);
                    user.put("value", value);
                } else
                {
                    user.put("type", "none");
                    user.put("value", 0);
                }
                array.add(user);
                uids.add(uid);
            }
            //用户昵称头像
            String[] userId = new String[uids.size()];
            userId = uids.toArray(userId);
            List<String> data = getUserCollects(userId);
            if (data != null)
            {
                for (int i = 0; i < array.size(); i++)
                {
                    JSONObject user = array.getJSONObject(i);
                    if (i < data.size())
                    {
                        JSONObject info = JSONObject.parseObject(data.get(i));
                        for (Map.Entry<String, Object> entry : info.entrySet())
                        {
                            user.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            int resultSize = array.size();
            if (array.size() > 0)
            {
                AtomicInteger index = new AtomicInteger();
                String name = ReadConfig.get("match-save-path") + matchKey + "-" + index.getAndIncrement() + ".json";
                JSONArray save = new JSONArray();
                //进行保存写入文件
                for (int i = 0; i < array.size(); i++)
                {
                    save.add(array.getJSONObject(i));
                    if (save.size() >= 100)
                    {
                        String json = save.toJSONString();
                        CmTool.writeFileString(name, json);
                        save.clear();
                        name = ReadConfig.get("match-save-path") + matchKey + "-" + index.getAndIncrement() + ".json";
                    }
                }
                if (save.size() > 0)
                {
                    String json = save.toJSONString();
                    CmTool.writeFileString(name, json);
                    save.clear();
                }
            }
            updateStatus(matchInfo, resultSize, "over");
        }
    }

    /**
     * 获取用户集合信息
     *
     * @param userId 用户编号
     * @return 集合
     */
    private static List<String> getUserCollects(String... userId)
    {
        if (userId.length <= 0)
            return null;
        String key = getUserCollectsKey();
        return RedisUtils.hmget(key, userId);
    }

    /**
     * 用户集合信息
     *
     * @return 用户集合key
     */
    private static String getUserCollectsKey()
    {
        return "ranking-user-collect";
    }

    /**
     * 获取赛场记录标签
     *
     * @param ddCode  赛场编号
     * @param ddGroup 群标签
     * @param ddIndex 轮次编号
     */
    static String getField(int ddCode, boolean ddGroup, int ddIndex)
    {
        return String.format("match-c%d-g%d-i%d", ddCode, ddGroup ? 1 : 0, ddIndex);
    }

    /**
     * 獲取當前賽場信息
     *
     * @return 賽場詳情
     */
    static Set<String> getMatchList()
    {
        Set<String> keys = RedisUtils.hkeys(ROUND_REDIS_KEY);
        if (keys != null)
        {
            keys.removeIf(key -> key.startsWith("current"));
            return keys;
        }
        return new HashSet<>();
    }
}
