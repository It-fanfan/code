package servlet.login;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
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
@WebServlet(urlPatterns = "/loginweb", name = "loginweb")
public class CmLoginServletWeb extends CmServletMain implements CmLogin
{
    private static final long serialVersionUID = 2676012240165079678L;

    /**
     * 获取一份用户数据
     */
    private EncrypteUserData gainUserInfo()
    {
        EncrypteUserData encryptedData = new EncrypteUserData();

        String headerUrl = ReadConfig.get("commonUrl");
        String userIdRandom = UserService.getOnceRandomUserId();
        encryptedData.openId = "web" + userIdRandom;
        encryptedData.avatarUrl = headerUrl + "customer.jpg";
        encryptedData.city = "web";
        encryptedData.country = "china";
        encryptedData.language = "zhcn";
        encryptedData.nickName = "游客" + userIdRandom.substring(userIdRandom.length() - 4);
        encryptedData.province = "web";
        encryptedData.sex = 1;

        return encryptedData;
    }

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
        String uid = (String) requestPackage.get("uid");
        String version = (String) requestPackage.get("version");
        EncrypteUserData userData = null;
        LOG.info(requestPackage.toJSONString());
        //
        // 条件过滤
        //
        if (null == uid)
        {
            LOG.error("failed-param --> uid = null");

            return JSONObject.parseObject("{\"result\":\"failed-param\"}");
        }
        if (uid.equals("0"))
        {
            userData = gainUserInfo();
        }
        String inviteUid = requestPackage.getString("inviteUid");
        return login(uid, version, userData, inviteUid, null);
    }

}
