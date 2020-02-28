package servlet.login;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import db.PeDbUser;
import db.PeDbWxConfig;
import service.UserService;
import servlet.CmServletMain;
import servlet.login.api.CmLogin;
import tool.CmTool;
import tool.EncrypteUserData;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/loginSnsapi")
public class CmLoginServletSnsapi extends CmServletMain implements CmLogin
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
        long start = System.currentTimeMillis();
        try
        {
            String uid = requestPackage.getString("uid");
            String version = requestPackage.getString("version");
            String appId = requestPackage.getString("appId");
            String code = requestPackage.getString("code");
            String authorization = ReadConfig.authorization;
            PeDbWxConfig wxConfig = PeDbWxConfig.getConfigFast(appId);
            JSONObject result = new JSONObject();
            if (wxConfig == null)
            {
                result.put("result", "fail");
                result.put("error", "empty appId");
                return result;
            }

            authorization = authorization.replace("#APPID", appId).replace("#SECRET", wxConfig.ddAppSecret).replace("#CODE", code);
            String json = CmTool.makeHttpConnect(authorization);
            JSONObject author = JSONObject.parseObject(json);
            if (!author.containsKey("access_token"))
            {
                return author;
            }
            String userinfoSns = ReadConfig.userinfoSns;
            userinfoSns = userinfoSns.replace("#ACCESS_TOKEN", author.getString("access_token")).replace("#OPENID", author.getString("openid"));
            String userJson = CmTool.makeHttpConnect(userinfoSns);
            JSONObject userInfo = JSONObject.parseObject(userJson);
            if (userInfo.containsKey("errcode"))
            {
                return userInfo;
            }
            String unionid = userInfo.getString("unionid");

            author.put("now", System.currentTimeMillis());
            UserService.setCache(unionid, "author", author.toJSONString());
            EncrypteUserData userData = new EncrypteUserData();
            userData.openId = author.getString("openid");
            userData.unionId = userInfo.getString("unionid");
            userData.avatarUrl = userInfo.getString("headimgurl");
            userData.nickName = userInfo.getString("nickname");
            userData.sex = userInfo.getInteger("sex");
            userData.city = userInfo.getString("city");
            userData.country = userInfo.getString("country");
            userData.province = userInfo.getString("province");
            String inviteUid = requestPackage.getString("inviteUid");
            if (requestPackage.containsKey("invite"))
                inviteUid = requestPackage.getString("invite");
            return login(uid, version, userData, inviteUid, appId);
        } finally
        {
            LOG.debug("耗时:" + (System.currentTimeMillis() - start));
        }

    }

    /**
     * 进行登录处理
     *
     * @param uid      用户编号
     * @param version  版本
     * @param userData 用户详情
     * @param appId    游戏编号
     * @return 处理结果
     */
    public JSONObject login(String uid, String version, EncrypteUserData userData, String inviteUid, String appId)
    {
        try
        {
            PeDbUser userGained = CmLogin.createGainUser(uid, version, userData, inviteUid, true, appId);
            if (null == userGained)
            {
                LOG.error("failed-usergain --> uid = " + uid);

                return JSONObject.parseObject("{\"result\":\"failed-usergain\"}");
            }
            //
            // 返回信息
            //
            JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
            userGained.gainUserMessage(result);
            return result;
        } finally
        {
            LOG.debug("[login]request:" + CmTool.getJSONByFastJSON(userData));
        }
    }
}