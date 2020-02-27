package service.match;

import com.alibaba.fastjson.JSONObject;
import tool.db.RedisUtils;

/**
 * 比赛排行榜服务
 */
public class DayRankingService implements Ranking
{
    private RankingService service;
    //赛制信息
    private JSONObject matchInfo = null;

    public DayRankingService(int gameCode)
    {
        setRedisKey(gameCode);
    }

    public JSONObject getMatchInfo()
    {
        return matchInfo;
    }

    @Override
    public RankingService getService()
    {
        return service;
    }

    /**
     * 获取查询Redis
     *
     * @param gameCode 游戏编号
     */
    private void setRedisKey(int gameCode)
    {
        String matchKey = getCurrentMatchKey(gameCode);
        if (matchKey == null)
            return;
        matchInfo = Ranking.getMatchInfo(matchKey);
        service = setRedisKey();
    }

    /**
     * 获取当前排行榜信息
     *
     * @param gameCode 游戏编号
     */
    public static String getCurrentMatchKey(int gameCode)
    {
        String field = String.format("current-g%d", gameCode);
        String json = RedisUtils.hget(ROUND_REDIS_KEY, field);
        JSONObject data = null;
        if (json != null)
        {
            data = JSONObject.parseObject(json);
        }
        if (data == null)
            return null;
        return data.getString(String.valueOf(false));
    }
}
