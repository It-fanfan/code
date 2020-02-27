package service.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.PeDbAppConfig;
import db.PeDbGame;
import db.PeDbRoundExt;
import db.PeDbRoundReceive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import tool.CmTool;
import tool.Log4j;
import tool.db.RedisUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * 赛区奖励发放和通知
 */
public class MatchRewardService
{
    private static final Logger LOG = LoggerFactory.getLogger(MatchRewardService.class);
    private final String appId;
    private final String ddUid;
    private final PeDbAppConfig appConfig;

    public MatchRewardService(String ddUid, String appId)
    {
        this.ddUid = ddUid;
        this.appId = appId;
        this.appConfig = PeDbAppConfig.getConfigsFast(appId);
    }

    /**
     * 插入用户参与赛区编号
     *
     * @param matchKey 赛区编号
     */
    static void insertMatchRecord(String ddUid, String matchKey)
    {
        String field = getMatchRecordField();
        String json = UserService.getStr(ddUid, field);
        JSONArray array = null;
        if (json != null)
            array = JSONObject.parseArray(json);
        if (array == null)
            array = new JSONArray();
        //已经设置则更新
        for (int i = 0; i < array.size(); i++)
        {
            if (matchKey.equals(array.getString(i)))
                return;
        }
        array.add(matchKey);
        LOG.debug("insertMatchRecord:" + matchKey + ":" + ddUid + ":" + array.toJSONString());
        UserService.setCache(ddUid, field, array.toJSONString());
    }

    /**
     * 獲取玩家赛场数据
     */
    private Set<String> getUserMatchRecord()
    {
        Set<String> record = new HashSet<>();
        String field = getMatchRecordField();
        String json = UserService.getStr(ddUid, field);
        JSONArray array = null;
        if (json != null)
            array = JSONObject.parseArray(json);
        if (array != null)
        {
            for (int i = 0; i < array.size(); i++)
            {
                record.add(array.getString(i));
            }
        }
        LOG.debug("获取用户赛区记录:" + record);
        return record;
    }

    /**
     * 获取玩家领取记录
     *
     * @return 领取记录
     */
    private Set<String> getUserReceiveRecord()
    {
        Set<String> record = new HashSet<>();
        String field = getMatchRewardField();
        String json = UserService.getStr(ddUid, field);
        JSONArray array = null;
        if (json != null)
            array = JSONObject.parseArray(json);
        if (array != null)
        {
            for (int i = 0; i < array.size(); i++)
            {
                record.add(array.getString(i));
            }
        }
        LOG.debug("获取用户赛区记录:" + record);
        return record;
    }

    /**
     * 领取赛区奖励
     *
     * @return 奖励内容
     */
    public JSONArray receiveMatchReward()
    {
        //用户参加赛区记录
        Set<String> records = getUserMatchRecord();
        //用户领取奖励记录
        Set<String> receives = getUserReceiveRecord();
        //已领取记录数字
        int size = receives.size();
        //移除已经领取过奖励
        records.removeIf(receives::contains);
        //未处理奖励
        if (!records.isEmpty())
        {
            JSONArray rewardInfo = new JSONArray();
            records.forEach(matchKey ->
            {
                String msg = receiveMatchReward(matchKey);
                if (msg != null && !msg.isEmpty())
                {
                    rewardInfo.add(msg);
                    //领取记录
                    receives.add(matchKey);
                }
            });
            if (size != receives.size())
            {
                //设置领取记录
                RedisUtils.hdel(ddUid, getMatchRewardField());
                //进行移除领取记录
                records.removeAll(receives);
                JSONArray array = new JSONArray();
                array.addAll(records);
                UserService.setCache(ddUid, getMatchRecordField(), array.toJSONString());
            }
            return rewardInfo;
        }
        return null;
    }

    /**
     * 领取奖励
     *
     * @param matchKey 比赛数据
     * @return 奖励详情
     */
    private String receiveMatchReward(String matchKey)
    {
        //获取排行榜
        JSONObject data = Ranking.getRankingData(ddUid, matchKey);
        //排行
        int ranking = data.getInteger("index");
        long mark = data.getDouble("mark").longValue();
        LOG.debug("当前玩家排名:" + data.toJSONString() + ",赛区:" + matchKey);
        String matchJSON = RedisUtils.hget(Ranking.ROUND_REDIS_KEY, matchKey);
        if (matchJSON != null)
        {
            JSONObject matchInfo = JSONObject.parseObject(matchJSON);
            //检测是否为小程序赛制
            boolean isMatch = matchInfo.getBoolean("isGroup");
            if ((isMatch && appConfig.ddProgram != 1) || (!isMatch && appConfig.ddProgram == 1))
                return null;
            //检测赛区状态是否结算
            if ("over".equals(matchInfo.getString("status")))
            {
                PeDbRoundExt dbRoundExt = PeDbRoundExt.getRoundFast(matchInfo.getString("round"));
                int ddGame = matchInfo.getInteger("gameCode");
                //参与排名
                if (dbRoundExt != null && ddGame != 0 && ranking > 0)
                    return setRewardResult(dbRoundExt, ddGame, matchInfo, ranking, mark);
                return "";
            }
            return null;
        }
        return "";
    }


    /**
     * 设置奖励结果
     *
     * @param dbRound   赛区内容
     * @param ddGame    游戏编号
     * @param matchInfo 赛场记录
     * @param ranking   排行
     * @param mark      分数
     * @return 奖励
     */
    private String setRewardResult(PeDbRoundExt dbRound, int ddGame, JSONObject matchInfo, int ranking, long mark)
    {
        try
        {
            PeDbGame game = PeDbGame.getGameFast(ddGame);
            if (game == null)
                return null;
            //保存领取奖励记录
            PeDbRoundReceive record = new PeDbRoundReceive();
            record.ddMark = mark;
            record.ddUid = ddUid;
            record.ddGCode = ddGame;
            record.ddTime = new Timestamp(System.currentTimeMillis());
            record.ddMCode = matchInfo.getInteger("code");
            record.ddGroup = matchInfo.getBoolean("isGroup");
            record.ddMIndex = matchInfo.getInteger("index");
            record.ddMStart = new Timestamp(matchInfo.getLong("start"));
            record.ddMEnd = new Timestamp(matchInfo.getLong("end"));
            record.ddRanking = ranking;
            StringBuilder rewardInfo = new StringBuilder();
            //奖金领取
            JSONObject array = dbRound.getRoundReward();
            JSONObject reward = new JSONObject();
            if (array.containsKey(String.valueOf(ranking)))
            {
                JSONObject info = array.getJSONObject(String.valueOf(ranking));
                String type = info.getString("type");
                int value = info.getInteger("value");
                reward.put("type", type);
                switch (type)
                {
                    case "rmb":
                    {
                        reward.put("rmb", CmTool.div(value, 100, 1));
                        reward.put("total", value);
                    }
                    break;
                    case "coin":
                    {
                        reward.put("total", value);
                    }
                    break;
                }
                record.ddType = reward.getString("type");
                record.ddTotal = reward.getIntValue("total");
                rewardInfo.append("    ").append(new SimpleDateFormat("MM/dd").format(record.ddMStart)).append("，");
                rewardInfo.append(game.ddName).append("（").append(matchInfo.getString("name")).append("），");
                rewardInfo.append("第 ").append(ranking).append(" 名，");
                switch (record.ddType)
                {
                    case "coin":
                        rewardInfo.append("金币 ").append(record.ddTotal);
                        break;
                    case "rmb":
                        rewardInfo.append("奖金 ").append(reward.get("rmb")).append(" 元");
                        break;
                }
                JSONObject extra = new JSONObject();
                extra.put("type", "match");
                //复活次数
                extra.put("extra", String.valueOf(ranking));
                extra.put("appId", appId);
                UserService.addValue(ddUid, record.ddType, record.ddTotal, extra);
            } else
            {
                rewardInfo.append("    ").append(new SimpleDateFormat("MM/dd").format(record.ddMStart)).append("，");
                rewardInfo.append(game.ddName).append("（").append(matchInfo.getString("name")).append("），");
                rewardInfo.append("第 ").append(ranking).append(" 名，就差一点就中奖了!");
                reward.put("type", "none");
                reward.put("total", 0);
            }
            //进行保存记录
            record.insertOrUpdate();
            return rewardInfo.toString();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }


    /**
     * 赛区领取奖励记录
     */
    private static String getMatchRewardField()
    {
        return "match-receive-record";
    }

    /**
     * 赛区纪录编号
     *
     * @return Redis field
     */
    private static String getMatchRecordField()
    {
        return "match-record";
    }
}
