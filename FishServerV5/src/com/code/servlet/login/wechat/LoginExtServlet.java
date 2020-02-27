package com.code.servlet.login.wechat;

import com.annotation.AvoidRepeatableCommit;
import com.code.protocols.login.LoginResponse;
import com.code.protocols.login.wechat.LoginExt;
import com.code.servlet.base.ServletMain;
import com.code.servlet.login.LoginServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/login/weChat", name = "com.code.protocols.login.wechat.LoginExt")
public class LoginExtServlet extends ServletMain<LoginExt.RequestImpl, LoginResponse> implements LoginServlet
{
    protected LoginResponse doLogic(RequestParameter parameter, HttpServletRequest request, HttpServletResponse response)
    {
        LoginExt.RequestImpl req = parameter.getRequestObject();
        LoginResponse res = doLogic(req, parameter.getIp(), parameter.getSessionId());
        setUserCache(request, res.userid);
        return res;
    }

    @Override
    protected boolean existFlush()
    {
        return false;
    }
}
