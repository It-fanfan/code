package com.code.service.login;

import com.code.cache.UserCache;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.other.Logon;

public class OtherUserService implements UserService<Logon.RequestImpl>
{
    private Logon.RequestImpl request;
    private ERROR error = ERROR.success;

    OtherUserService(Logon.RequestImpl request)
    {
        this.request = request;
    }

    @Override
    public Logon.RequestImpl request()
    {
        return this.request;
    }

    @Override
    public String existLogin()
    {
        if (exist(request.userid))
        {
            return request.userid;
        }
        return null;
    }

    @Override
    public UserCache register()
    {
        error = ERROR.nonsupport;
        return null;
    }

    @Override
    public void setError(ERROR error)
    {
        this.error = error;
    }


    @Override
    public ERROR error()
    {
        return error;
    }

    @Override
    public LoginCode setLoginCode(UserCache userCache)
    {
        return LoginCode.createLogonCode(userCache);
    }
}
