package com.code.protocols.login;

import com.code.protocols.AbstractResponse;

/**
 * 登录异常码
 */
public enum ERROR
{
    success(200, "成功"),
    un_token(201, "您的操作被系统识别成跨登录操作，请重新登录~"),
    timeout(202, "您太久没有操作了，请重新登录~"),
    frequently(203, "您的操作过于频繁，请稍后重试~"),
    system_error(204, "系统故障,请联系客服~"),
    conflict(205, "您的账号出现多端访问~"),
    server_error(206, "服务器君临场暂歇，请稍后再来~"),
    api_code(400, "api请求失败，获取不到数据~"),
    wechat_api_login(401, "调取微信api失败，无法解析用户基本信息~"),
    wecaht_api_user(402, "调取微信api失败，无法获取用户详情~"),
    nonsupport(403, "暂不支持无账号玩家登陆~"),
    non_platform(999, "暂不支持平台");

    ERROR(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setException(AbstractResponse res)
    {
        res.code = code;
        res.msg = msg;
        res.status = true;
    }

}
