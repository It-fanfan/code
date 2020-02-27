package com.code.protocols.login;

import com.code.protocols.AbstractResponse;

public class LoginResponse extends AbstractResponse
{
    //用户编号
    public String userid;
    // 登陆态
    public String logincode;
    // 当前服务器时间
    public long currenttime;
}
