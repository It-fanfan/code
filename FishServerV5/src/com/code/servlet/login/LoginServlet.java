package com.code.servlet.login;

import com.code.cache.UserCache;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.login.LoginResponse;
import com.code.service.goods.ShopService;
import com.code.service.login.UserInfoService;
import com.utils.log4j.Log4j;

public interface LoginServlet
{
    default LoginResponse doLogic(LoginRequest req, String ip, String sessionId)
    {
        LoginResponse res = new LoginResponse();
        String userId = null, type = "login";
        try
        {
            UserInfoService service = new UserInfoService(req);
            UserCache userCache = service.login();
            ERROR error = service.error();
            error.setException(res);
            if (error == ERROR.success)
            {
                res.userid = userCache.getUserId();
                type = userCache.isRegister ? "register" : "login";
                userId = res.userid;
                LoginCode loginCode = service.setLoginCode(userCache);
                res.logincode = LoginCode.createLogonCode(loginCode, userCache);
                res.currenttime = System.currentTimeMillis();
                if (!userCache.isRegister)
                    new ShopService(userCache).init(loginCode);
            }
        } finally
        {
            Log4j.analysisLog(req.platform.name(), type, userId, ip, sessionId);
        }
        return res;
    }
}
