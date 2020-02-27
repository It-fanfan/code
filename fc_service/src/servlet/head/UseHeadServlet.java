package servlet.head;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbUser;
import service.UserService;
import service.match.RankingService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@WebServlet(urlPatterns = "/useHead", name = "use head")
public class UseHeadServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        //用户信息
        String ddUid = content.getString("uid");
        //头像
        int avatarFrame = content.getInteger("avatarFrame");
        boolean exist = UserService.existAvatarFrame(ddUid, avatarFrame) && !UserService.existUseFrame(ddUid, avatarFrame);
        if (exist)
        {
            result.put("avatarFrame", avatarFrame);
            result.put("result", "success");
            JSONObject update = new JSONObject();
            update.put("uid", ddUid);
            update.put("avatarFrame", avatarFrame);
            UserService.updateAvatarFrame(update);
            RankingService.handleRanking(update);
            return result;
        }
        result.put("result", "fail");
        result.put("msg", "无效操作，请重新授权");
        JSONObject user = new JSONObject();
        UserService.putUserValue(user, ddUid);
        result.put("user", user);
        return result;
    }
}
