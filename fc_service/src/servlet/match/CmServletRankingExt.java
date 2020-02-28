package servlet.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import service.match.DayRankingService;
import service.match.GroupRankingService;
import service.match.Ranking;
import service.match.RankingService;
import servlet.CmServletMain;
import servlet.login.CmServletQueryMatch;
import tool.CmTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

/**
 * @author feng
 */
@WebServlet(urlPatterns = "/rankingExt")
public class CmServletRankingExt extends CmServletMain
{

    private static final long serialVersionUID = -1415031131715473740L;
    //查询排行榜数量个数
    private static final int MAX_SEARCH_RANKING = 99;

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
        String uid = requestPackage.getString("uid");
        int gameCode = requestPackage.getInteger("gameCode");
        String startInfo = requestPackage.getString("start");
        String group = requestPackage.getString("matchKey");
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        if (null == uid || 0 >= gameCode || null == startInfo)
        {
            return result;
        }
        //获取集合排行数据
        int start = CmTool.parseInt(startInfo, 0);
        Ranking ranking;
        if (group != null)
        {
            ranking = new GroupRankingService(group);
        } else
        {
            ranking = new DayRankingService(gameCode);
        }
        JSONObject matchInfo = ranking.getMatchInfo();
        if (matchInfo == null || !"running".equals(matchInfo.getString("status")))
        {
            result.put("msg", "当前还未开赛");
            return result;
        }
        //获取在线人数
        String matchKey = Ranking.getField(matchInfo.getInteger("code"), matchInfo.getBoolean("isGroup"), matchInfo.getInteger("index"));
        result.put("online", CmServletQueryMatch.getOnlineUserCount(matchKey));
        RankingService service = ranking.getService();
        if (service == null)
        {
            return result;
        }
        Vector<JSONObject> rankingUsers = service.globalRanking(start, start + MAX_SEARCH_RANKING);

        JSONArray rankArray = new JSONArray();
        int count = 0;
        if (rankingUsers != null)
        {
            for (int i = 0; i < rankingUsers.size(); i++)
            {
                JSONObject element = rankingUsers.elementAt(i);
                rankArray.add(element);
            }
            count = rankingUsers.size();
        }
        JSONObject own = Ranking.getRankingData(uid, matchKey);
        own.put("history", service.getMatchRecord(uid));
        result.putAll(own);
        result.put("gameCode", gameCode);
        result.put("uid", uid);
        result.put("start", start);
        result.put("count", count);
        result.put("ranks", rankArray);
        result.put("matchInfo", matchInfo);
        result.put("currentTime", System.currentTimeMillis());
        //进行检测用户是否有被动标签
        UserService userService = new UserService(uid);
        List<String> flushFlag = userService.getFlushFlag();
        if (flushFlag != null)
        {
            JSONObject flushNode = userService.readFlushNode(flushFlag);
            if (flushNode != null)
                result.put("flushNode", flushNode);
        }
        return result;
    }
}
