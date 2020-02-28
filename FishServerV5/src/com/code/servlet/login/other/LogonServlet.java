package com.code.servlet.login.other;

import com.annotation.AvoidRepeatableCommit;
import com.code.protocols.login.LoginResponse;
import com.code.protocols.login.other.Logon;
import com.code.servlet.base.ServletMain;
import com.code.servlet.login.LoginServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/login/other", name = "com.code.protocols.login.other.Logon")
public class LogonServlet extends ServletMain<Logon.RequestImpl, LoginResponse> implements LoginServlet
{
    protected LoginResponse doLogic(RequestParameter parameter, HttpServletRequest request, HttpServletResponse response)
    {
        Logon.RequestImpl req = parameter.getRequestObject();
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
