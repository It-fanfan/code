package com.code.service.login;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.dto.UserIdDTO;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.login.other.Logon;
import com.code.protocols.login.wechat.LoginExt;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.protocols.sdk.Huawei;

import com.code.protocols.sdk.Vivo;
import com.code.service.achievement.AchievementService;
import com.code.service.login.quickgame.HuaweiUserService;

import com.code.service.login.quickgame.VivoUserService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

/**
 * 玩家服务信息
 */
public class UserInfoService
{
    private static Logger LOG = LoggerFactory.getLogger(UserInfoService.class);


    private UserService userService;

    public UserInfoService(LoginRequest request)
    {
        LOG.debug("登陆平台信息:" + XwhTool.getJSONByFastJSON(request));
        switch (request.platform)
        {
            case wechat:
                userService = new WechatUserService((LoginExt.RequestImpl) request);
                break;
            case baidu:
                break;
            case facebook:
                break;
            case appstore:
                break;
            case googleplay:
                break;
            case qq:
                break;
            case android:
                break;
            //仅支持登录，无法支持注册
            case windows:
            case other:
                userService = new OtherUserService((Logon.RequestImpl) request);
                break;
            case huawei:
                userService = new HuaweiUserService((Huawei.RequestImpl) request);
                break;
            case vivo:
                userService = new VivoUserService((Vivo.RequestImpl) request);
                break;
            default:
                break;
        }
    }

    /**
     * 进行给玩家注册礼包
     *
     * @param userCache 玩家信息
     */
    private void putRegisterGit(UserCache userCache)
    {
        try
        {
            long registerShell = Systemstatusinfo.getInt("register_shell");
            int basinId = 1;
            ConfigBasin basin = (ConfigBasin) FishInfoDb.instance().getCacheKey(ConfigBasin.class, new String[]{"basinId", String.valueOf(basinId)});
            if (userCache.shell() != 0)
                registerShell = registerShell - userCache.shell();
            userCache.incrShell(registerShell);
            if (basin != null)
            {
                userCache.setBasinData(basinId, basin.getArchiveName());
                //初始添加水域成就
                new AchievementService(userCache).addAchievement(AchievementType.Basin, 1);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 進行查詢玩家信息
     *
     * @param selectSQL sql
     * @return 玩家信息
     */
    public static Vector<UserIdDTO> selectUserIdBySQL(String selectSQL)
    {
        return FishInfoDb.instance().findBySQL(selectSQL, UserIdDTO.class);
    }

    public UserCache login()
    {
        if (userService == null)
            return null;
        //检测是否存在用户
        String userId = userService.existLogin();
        if (userId == null)
        {
            try
            {
                //进行注册
                UserCache userCache = userService.register();
                if (userCache != null)
                {
                    putRegisterGit(userCache);
                }
                return userCache;
            } catch (Exception e)
            {
                LOG.error("REGISTER ERROR:" + Log4j.getExceptionInfo(e));
            }
            return null;
        }
        return userService.login(userId);
    }

    public ERROR error()
    {
        if (userService == null)
            return ERROR.non_platform;
        return userService.error();
    }

    public LoginCode setLoginCode(UserCache userCache)
    {
        return userService.setLoginCode(userCache);
    }
}

