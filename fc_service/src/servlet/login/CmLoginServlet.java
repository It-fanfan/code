package servlet.login;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import servlet.CmServletMain;
import servlet.login.api.CmLogin;
import tool.EncrypteUserData;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author feng
 */
@WebServlet(urlPatterns = "/login", name = "login")
public class CmLoginServlet extends CmServletMain implements CmLogin
{
    private static final long serialVersionUID = -2869864936687157528L;

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
        String version = requestPackage.getString("version");
        String appId = requestPackage.getString("appId");
        EncrypteUserData userData = null;
        //
        // 条件过滤
        //
        if (null == uid || null == version)
        {
            LOG.error("failed-param --> uid = null");
            return JSONObject.parseObject("{\"result\":\"failed-param\"}");
        }
        if (uid.equals("0"))
        {
            String encryptedData = requestPackage.getString("encryptedData");
            String iv = requestPackage.getString("iv");
            String code = requestPackage.getString("code");
            userData = UserService.getUserInfo(appId, code, encryptedData, iv);
            if (null == userData)
            {
                LOG.error("failed-userdata --> uid = " + uid);
                return JSONObject.parseObject("{\"result\":\"failed-userdata\"}");
            }
        }
        String inviteUid = requestPackage.getString("inviteUid");
        if (requestPackage.containsKey("invite"))
            inviteUid = requestPackage.getString("invite");
        return login(uid, version, userData, inviteUid, appId);
    }

}
