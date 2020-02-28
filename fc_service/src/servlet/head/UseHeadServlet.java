package servlet.head;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
<<<<<<< HEAD
=======
import db.PeDbUser;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
import service.UserService;
import service.match.RankingService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/useHead", name = "use head")
public class UseHeadServlet extends CmServletMain implements Serializable {
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content) {
=======
@WebServlet(urlPatterns = "/useHead", name = "use head")
public class UseHeadServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject result = new JSONObject();
        //用户信息
        String ddUid = content.getString("uid");
        //头像
        int avatarFrame = content.getInteger("avatarFrame");
        boolean exist = UserService.existAvatarFrame(ddUid, avatarFrame) && !UserService.existUseFrame(ddUid, avatarFrame);
<<<<<<< HEAD
        if (exist) {
            result.put("avatarFrame", avatarFrame);
            result.put("avatarFrameGain", UserService.getUserFrameGain(ddUid));
=======
        if (exist)
        {
            result.put("avatarFrame", avatarFrame);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
