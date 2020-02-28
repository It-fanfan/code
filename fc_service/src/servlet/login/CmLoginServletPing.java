package servlet.login;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbUser;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/loginPing")
public class CmLoginServletPing extends CmServletMain {
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        String uid = requestPackage.getString("uid");
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        PeDbUser userGained = PeDbUser.gainUserObject(uid);
        if (userGained != null) {
            userGained.gainUserMessage(result);
        }
=======
@WebServlet(urlPatterns = "/loginPing")
public class CmLoginServletPing extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String uid = requestPackage.getString("uid");
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        PeDbUser userGained = PeDbUser.gainUserObject(uid);
        if (userGained != null)
            userGained.gainUserMessage(result);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return result;
    }
}
