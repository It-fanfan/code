package com.code.protocols;

import com.code.cache.UserCache;
import com.code.protocols.login.LoginRequest;
import com.code.service.message.NoticeService;
import com.utils.XwhTool;
import com.utils.db.RedisUtils;

import java.util.UUID;

import static com.code.protocols.login.LoginRequest.Platform;
import static com.code.protocols.login.LoginRequest.Platform.other;
import static com.code.protocols.login.LoginRequest.Platform.wechat;

/**
 * 登陆态数据
 */
public class LoginCode
{
    private static long loginTotal = 0;
    //玩家编号
    private String userId;
    //玩家应用编号
    private String openId;
    //登陆时间
    private String logonTime;
    //平台数据
    private Platform platform;
    //私有数据
    private WeChat privateData;

    /**
     * 获取登陆态信息
     *
     * @param userCache 玩家信息
     * @return 登陆态信息
     */
    public static LoginCode getLoginCode(UserCache userCache)
    {
        String codeVal = RedisUtils.get(userCache.getLoginCode());
        if (codeVal != null)
        {
            return XwhTool.parseJSONByFastJSON(codeVal, LoginCode.class);
        }
        return null;
    }

    /**
     * 设置登陆态信息
     *
     * @param userCache 玩家信息
     * @param loginCode 登陆态信息
     */
    private static void putLoginCode(UserCache userCache, LoginCode loginCode)
    {
        String key = userCache.getLoginCode();
        // 登陆态有效数据记录
        RedisUtils.set(key, XwhTool.getJSONByFastJSON(loginCode));
        RedisUtils.pexpire(key, 1000 * 60 * 20);
    }

    /**
     * 进行登陆态更新处理
     *
     * @param userCache 玩家信息
     * @return 登陆信息
     */
    public static LoginCode createLogonCode(UserCache userCache)
    {
        return createLogonCode(userCache, other);
    }

    /**
     * 进行登陆态更新处理
     *
     * @param userCache 玩家信息
     * @return 登陆信息
     */
    public static LoginCode createLogonCode(UserCache userCache, Platform platform)
    {
        LoginCode element = new LoginCode();
        element.platform = platform;
        element.userId = userCache.getUserId();
        element.logonTime = String.valueOf(System.currentTimeMillis());
        return element;
    }

    /**
     * 获取登陆态
     *
     * @param loginCode logon code element
     * @param userCache user element
     * @return code string
     */
    public static String createLogonCode(LoginCode loginCode, UserCache userCache)
    {
        String key = getKey();
        String code = userCache.getLoginCode();
        if (code != null)
        {
            RedisUtils.del(code);
        }
        userCache.setLoginCode(key);
        putLoginCode(userCache, loginCode);
        //进行初始化消息
        NoticeService service = new NoticeService(userCache);
        service.init();
        return key;
    }

    /**
     * 获取key
     *
     * @return create login key
     */
    public static String getKey()
    {
        return String.format("%s-%s-%d", "logoncode", UUID.randomUUID().toString(), loginTotal++);
    }

    /**
     * 获取sessionKey
     *
     * @return 返回微信session key
     */
    public String weChatSessionKey()
    {
        if (platform == wechat)
        {
            return privateData.sessionkey;
        }
        return null;
    }

    /**
     * 獲取微信appid
     *
     * @return 值
     */
    public String weChatAppId()
    {
        if (platform == wechat)
        {
            return privateData.appid;
        }
        return null;
    }

    /**
     * 设置微信平台数据
     *
     * @param appid      微信appid
     * @param sessionkey 微信session key
     */
    public void setPrivateData(String appid, String sessionkey)
    {
        this.platform = wechat;
        this.privateData = new WeChat();
        this.privateData.sessionkey = sessionkey;
        this.privateData.appid = appid;
        this.logonTime = String.valueOf(System.currentTimeMillis());
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getLogonTime()
    {
        return logonTime;
    }

    public void setLogonTime(String logonTime)
    {
        this.logonTime = logonTime;
    }

    public LoginRequest.Platform getPlatform()
    {
        return platform;
    }

    public void setPlatform(LoginRequest.Platform platform)
    {
        this.platform = platform;
    }

    public WeChat getPrivateData()
    {
        return privateData;
    }

    public void setPrivateData(WeChat privateData)
    {
        this.privateData = privateData;
    }

    //微信私有数据
    class WeChat
    {
        // 對應的session key
        public String sessionkey;
        // 对应的appid
        public String appid;
    }
}
