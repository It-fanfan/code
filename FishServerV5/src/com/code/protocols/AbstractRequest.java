package com.code.protocols;

import com.code.protocols.login.LoginRequest;

/**
 * 通用传入协议
 *
 * @author Sky
 */
public class AbstractRequest
{
    // 登陆态
    public String logincode;
    //用户编号
    public String userid;
    // 返回openid
    public String openid;
    // 平台
    public LoginRequest.Platform platform;
    //设备
    public LoginRequest.Device device;
    // IP地址
    public String ip;
    //当前本地化
    public String localization;
}
