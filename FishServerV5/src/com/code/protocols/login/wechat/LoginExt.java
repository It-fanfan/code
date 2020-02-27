package com.code.protocols.login.wechat;

import com.code.protocols.login.LoginRequest;

/**
 * 登陆协议
 *
 * @author Sky
 */
public class LoginExt
{
    public static class RequestImpl extends LoginRequest
    {
        // appid
        public String appid;
        // 登陆code
        public String code;
        // 加密数据
        public String encrypteddata;
        // 加密向量
        public String iv;
        // 签名数据
        public String signature;
        // 用户信息
        public WxInfo userinfo;
        // 场景数据
        public Object scenejson;

    }

    /**
     * 微信用户数据
     *
     * @author Sky
     */
    public static class WxInfo
    {
        public String avatarUrl;
        public String city;
        public String country;
        public int gender;
        public String language;
        public String nickName;
        public String province;
    }

    public static class Storage_SessionKey
    {
        public String session_key;
        public String openid;
    }

    /**
     * 进行好友来源
     *
     * @author Sky
     */
    public static class FriendFrom
    {
        public String friend;

        public String openGId;
    }
}
