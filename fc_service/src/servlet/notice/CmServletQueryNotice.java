package servlet.notice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbAppConfig;
import db.PeDbNoticeSystem;
import db.PeDbRoundMatch;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

import static db.PeDbNoticeSystem.NoticeType.game_explain;
import static db.PeDbNoticeSystem.NoticeType.mini_explain;

@WebServlet(urlPatterns = "/query/notice")
public class CmServletQueryNotice extends CmServletMain
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
        //对应appId
        String appId = requestPackage.getString("appId");
        JSONObject result = new JSONObject();
        JSONArray noticeList = new JSONArray();
        //常规
        //获取维护公告
        //维护公告
        Vector<PeDbNoticeSystem> safes = PeDbNoticeSystem.getNoticeByType(PeDbNoticeSystem.NoticeType.safe);
        safes.forEach(system -> noticeList.add(system.getMessage()));
        //常规公告
        Vector<PeDbNoticeSystem> routines = PeDbNoticeSystem.getNoticeByType(PeDbNoticeSystem.NoticeType.routine);
        routines.forEach(system -> noticeList.add(system.getMessage()));
        PeDbAppConfig wxConfig = PeDbAppConfig.getConfigsFast(appId);
        PeDbNoticeSystem.NoticeType explain = mini_explain;
        //非小程序下发赛制通知
        if (wxConfig == null || wxConfig.ddProgram != 1)
        {
            //赛区预告
            JSONObject matchNotice = PeDbRoundMatch.getMessage(System.currentTimeMillis());
            result.put("match", matchNotice);
            if (matchNotice.getJSONArray("matchList").size() > 0)
            {
                Vector<PeDbNoticeSystem> matches = PeDbNoticeSystem.getNoticeByType(PeDbNoticeSystem.NoticeType.match);
                matches.forEach(system -> noticeList.add(system.getMessage()));
            }
            explain = game_explain;
        }
        //说明公告
        Vector<PeDbNoticeSystem> explains = PeDbNoticeSystem.getNoticeByType(explain);
        explains.forEach(system -> noticeList.add(system.getMessage()));
        result.put("noticeList", noticeList);
        result.put("result", "success");
        return result;
    }
}
