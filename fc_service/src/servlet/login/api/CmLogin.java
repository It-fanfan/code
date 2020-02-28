package servlet.login.api;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.match.RankingService;
import tool.CmTool;
import tool.EncrypteUserData;
import tool.Log4j;

import java.util.Date;

public interface CmLogin
{
    Logger LOG = LoggerFactory.getLogger(CmLogin.class);

    /**
     * 创建用户
     *
     * @param uid       用户编号
     * @param version   版本
     * @param userData  用户信息
     * @param inviteUid 邀请用户信息
     * @param program   是否小程序
     * @param appId     应用编号
     * @return 用户信息
     */
    static PeDbUser createGainUser(String uid, String version, EncrypteUserData userData, String inviteUid, boolean program, String appId)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbUser userGained = null;
        try
        {
            if (null == userData)
            {
                userGained = PeDbUser.gainUserObject(uid);
            } else
            {
                uid = program ? userData.unionId : userData.openId;
                userGained = PeDbUser.gainUserObject(uid);
                if (null == userGained)
                {
                    userGained = PeDbUser.createUser(uid, userData.openId, appId, userData.nickName, userData.avatarUrl, userData.sex, userData.province, userData.city, userData.country, userData.language);
                    userGained.insertObject(sqlResource);
                    //处理用户数值保存
                    PeDbUserValue value = new PeDbUserValue(uid, 0, 0, 0, 0, 0, new Date());
                    value.insertObject(sqlResource);
                    JSONObject extra = new JSONObject();
                    extra.put("type", "register");
                    extra.put("appId", appId);
                    UserService.addValue(uid, "coin", ReadConfig.registerCoin, extra);
                    //添加用户邀请列表
                    if (inviteUid != null && !inviteUid.isEmpty() && !inviteUid.equals(uid))
                    {
                        UserService.setUserInvite(inviteUid, userGained);
                    }
                } else
                {
                    userGained.ddName = userData.nickName;
                    userGained.ddAvatarUrl = userData.avatarUrl;
                    userGained.ddSex = userData.sex;
                    userGained.update("ddName#ddAvatarUrl#ddSex");
                }
                //用户数据存储
                if (UserService.getStr(uid, appId) == null)
                {
                    PeDbUserApp userApp = PeDbUserApp.createUserApp(uid, userData.openId, version, appId);
                    userApp.insertObject(sqlResource);
                    //进行保存用户数据
                    JSONObject appData = new JSONObject();
                    appData.put("openId", userData.openId);
                    appData.put("version", version);
                    UserService.setCache(uid, appId, appData.toJSONString());
                }
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        if (userGained != null)
        {
            JSONObject update = new JSONObject();
            update.put("uid", userGained.ddUid);
            update.put("avatarFrame", userGained.ddAvatarFrame);
            update.put("name", userGained.ddName);
            update.put("avatarUrl", userGained.ddAvatarUrl);
            update.put("sex", userGained.ddSex);
            RankingService.handleRanking(update);
        }
        return userGained;
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
    default JSONObject login(String uid, String version, EncrypteUserData userData, String inviteUid, String appId)
    {
        try
        {
            //
            // 生成用户
            //
            boolean isProgram = false;
            if (appId != null)
            {
                PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
                isProgram = appConfig.ddProgram == 1;
            }

            PeDbUser userGained = createGainUser(uid, version, userData, inviteUid, isProgram, appId);
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
