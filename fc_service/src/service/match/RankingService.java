package service.match;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;
import tool.Log4j;
import tool.db.RedisUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingService
{
    //当前排行榜key
    private String rankingKey;
    //用户汇总排行key
    private String userCollectKey;
    //游戲編號
    private int gameCode;

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingService.class);

    public RankingService(int gameCode, String rankingKey)
    {
        this.gameCode = gameCode;
        this.rankingKey = rankingKey;
        this.userCollectKey = "user" + rankingKey.substring(0, 1).toUpperCase() + rankingKey.substring(1);
    }

    /**
     * 设置赛场胜负记录
     */
    public void setMatchRecord(String uid, boolean isWin)
    {
        JSONObject data = getMatchRecord(uid);
        Integer total = data.getInteger("total");
        Integer success = data.getInteger("success");
        if (total == null)
            total = 1;
        else
            total += 1;
        data.put("total", total);
        if (isWin)
        {
            if (success == null)
                success = 1;
            else
                success += 1;
            data.put("success", success);
        }
        RedisUtils.hset(userCollectKey, uid, data.toJSONString());
    }

    /**
     * 获取赛场胜负记录
     *
     * @param uid 用户编号
     */
    public JSONObject getMatchRecord(String uid)
    {
        String json = RedisUtils.hget(userCollectKey, uid);
        JSONObject data = null;
        if (json != null)
            data = JSONObject.parseObject(json);
        if (data == null)
            data = new JSONObject();
        return data;
    }

    /**
     * 获取同玩次数
     */
    public long getSamePlayCount(Set<String> keys)
    {
        //记录相同次数
        String key = getSameUserKey(keys);
        String value = RedisUtils.hget(userCollectKey, key);
        if (value != null)
            return Long.valueOf(value);
        return 0;
    }

    /**
     * 获取同玩玩家key
     *
     * @param keys 用户聚合
     * @return key
     */
    private static String getSameUserKey(Set<String> keys)
    {
        Vector<String> users = new Vector<>(keys);
        users.sort(Comparator.naturalOrder());
        StringBuilder key = new StringBuilder();
        for (String user : users)
        {
            if (key.length() != 0)
                key.append("&");
            key.append(user);
        }
        return key.toString();
    }

    /**
     * 添加分数集合
     *
     * @param isPk      是否PK模式
     * @param userScore 用户信息
     */
    public void zAdd(boolean isPk, Map<String, Integer> userScore)
    {
        userScore.forEach((k, v) -> zAdd(k, v, isPk));
        if (isPk && userScore.size() > 1)
        {
            //记录相同次数
            String key = getSameUserKey(userScore.keySet());
            RedisUtils.hincrby(userCollectKey, key, 1);
        }
    }

    /**
     * 进行添加排行版，不存在获取更新最大值
     *
     * @param ddUid 用户编号
     * @param score 当前分数
     * @param isPk  是否为PK模式
     */
    void zAdd(String ddUid, long score, boolean isPk)
    {
        try
        {
            if (isPk)
            {
                RedisUtils.zincrby(rankingKey, ddUid, score);
            } else
            {
                //
                JSONObject tuple = Ranking.getUserRanking(ddUid, rankingKey);
                if (tuple.getInteger("index") <= 0 || tuple.getDouble("mark") < score)
                {
                    RedisUtils.zAdd(rankingKey, score, ddUid);
                }
            }
        } finally
        {
            //插入记录
            MatchRewardService.insertMatchRecord(ddUid,rankingKey);
        }
    }

    /**
     * 查询全球榜 start - end 的排行数据
     *
     * @param start 开始编号
     * @param end   截止编号
     * @return 排行用户
     */
    public Vector<JSONObject> globalRanking(int start, int end)
    {
        Vector<JSONObject> globals = new Vector<>();
        try
        {
            // 排行榜
            Set<Tuple> scores = RedisUtils.zrevrangeWithScores(rankingKey, start, end);
            if (scores == null)
                return globals;
            AtomicInteger atomic = new AtomicInteger();
            scores.forEach(tuple ->
            {
                String key = tuple.getElement();
                int index = atomic.getAndIncrement();
                JSONObject element = new JSONObject();
                element.put("uid", key);
                element.put("mark", Double.valueOf(tuple.getScore()).longValue());
                element.put("index", index + 1);
                globals.add(element);
            });
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return globals;
    }

    /**
     * 设置用户简单集合
     *
     * @param user 排名信息
     */
    public static void handleRanking(JSONObject user)
    {
        JSONObject userData = null;
        String uid = user.getString("uid");
        String json = RedisUtils.hget(Ranking.getUserCollectsKey(), uid);
        if (json != null)
            userData = JSONObject.parseObject(json);
        if (userData == null)
            userData = new JSONObject();
        //获取更新项
        userData.putAll(user);
        RedisUtils.hset(Ranking.getUserCollectsKey(), uid, userData.toJSONString());
    }
}
