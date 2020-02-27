package servlet.notice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbNoticeReward;
import service.UserService;
import service.match.MatchRewardService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 赛区奖励，进行主动查询中奖纪录,并发送奖励
 */
@WebServlet(urlPatterns = "/reward/query")
public class CmServletMatchReward extends CmServletMain
{
    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String ddUid = requestPackage.getString("uid");
        if (null == ddUid)
            return null;
        String appId = requestPackage.getString("appId");
        //群比赛编号
        MatchRewardService service = new MatchRewardService(ddUid, appId);
        //获取当前正在使用的赛区
        JSONObject result = new JSONObject();
        result.put("result", "success");
        //赛区奖励公告
        JSONArray reward = service.receiveMatchReward();
        if (reward != null && !reward.isEmpty())
            result.put("reward-notice", reward);
        //补偿公告
        JSONArray makeUp = PeDbNoticeReward.getRewardNotice(ddUid, appId);
        if (makeUp != null && !makeUp.isEmpty())
            result.put("makeUp-notice", makeUp);
        UserService.putUserValue(result, ddUid);
        return result;
    }
}
