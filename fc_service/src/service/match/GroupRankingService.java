package service.match;

import com.alibaba.fastjson.JSONObject;

/**
 * 比赛排行榜服务
 */
public class GroupRankingService implements Ranking
{
    //赛区状态
    private JSONObject matchInfo = null;

    public GroupRankingService(String matchId)
    {
        setRedisKey(matchId);
    }

    private RankingService service;

    @Override
    public RankingService getService()
    {
        return service;
    }

    @Override
    public JSONObject getMatchInfo()
    {
        return matchInfo;
    }

    /**
     * 获取查询Redis
     *
     * @param matchKey 赛场编号
     */
    private void setRedisKey(String matchKey)
    {
        matchInfo = Ranking.getMatchInfo(matchKey);
        service = setRedisKey();
    }
}
