package com.code.servlet.login.quickgame;

import com.annotation.AvoidRepeatableCommit;
import com.code.protocols.login.LoginResponse;
import com.code.protocols.sdk.Huawei;
import com.code.servlet.base.ServletMain;
import com.code.servlet.login.LoginServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/login/huawei", name = "huawei")
public class HuaweiServlet extends ServletMain<Huawei.RequestImpl, LoginResponse> implements LoginServlet
{
    protected LoginResponse doLogic(RequestParameter parameter, HttpServletRequest request, HttpServletResponse response)
    {
        Huawei.RequestImpl req = parameter.getRequestObject();
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
