package com.code.service.login.quickgame;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.userinfo.UserInfo;
import com.code.dao.entity.fish.userinfo.UserInfoHuawei;
import com.code.dao.entity.fish.userinfo.UserInfoVivo;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.sdk.Huawei;
import com.code.protocols.sdk.Vivo;
import com.code.service.login.UserService;
import com.utils.XwhTool;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

public class VivoUserService implements UserService<Vivo.RequestImpl>
{
    private Vivo.RequestImpl request;
    private ERROR error = ERROR.success;

    public VivoUserService(Vivo.RequestImpl request)
    {
        this.request = request;
    }

    @Override
    public Vivo.RequestImpl request()
    {
        return this.request;
    }

    @Override
    public String existLogin()
    {
        String userId = request().userid, openid = request().openid;
        if (exist(userId))
        {
            UserCache userCache = UserCache.getUserCache(userId);
            if (userCache != null && userCache.getOpenid().equals(openid))
            {
                return userId;
            }
        }
        String SQL = "select * from user_info_vivo where openId='" + openid + "'";
        Vector<UserInfoVivo> data = FishInfoDb.instance().findBySQL(SQL, UserInfoVivo.class);
        if (data == null || data.isEmpty())
            return null;
        return String.valueOf(data.firstElement().getUserId());
    }

    @Override
    public UserCache register() throws Exception
    {
        if (error != ERROR.success)
        {
            error = ERROR.nonsupport;
            return null;
        }
        FishInfoDb db = FishInfoDb.instance();
        UserInfo userInfo = setUserInfo();
        UserInfoVivo vivo = new UserInfoVivo();
        vivo.setOpenId(request().openid);
        vivo.setUnionId(request().unionid);
        if (request().openid == null)
        {
            error = ERROR.nonsupport;
            return null;
        }
        vivo.setLoginTime(new Timestamp(System.currentTimeMillis()));
        String SQL = db.getEntitySQL(userInfo);
        userInfo.setUserId(db.insertGenerateKey(SQL));
        //进行保存数据
        vivo.setUserId(userInfo.getUserId());
        if (userInfo.getUserId() == 0)
        {
            error = ERROR.nonsupport;
            return null;
        }
        FishInfoDao.instance().saveOrUpdate(vivo, true);
        UserCache userCache = UserCache.putUserInfo(hash(userInfo, vivo));
        userCache.isRegister = true;
        //进行设置用户集合
        UserCache.setRankingUser(userInfo.getUserId(), userInfo.getIcon(), userInfo.getNickName());
        return userCache;
    }

    @Override
    public void setError(ERROR error)
    {
        this.error = error;
    }


    /**
     * 进行hash值处理
     *
     * @param userInfo 用户基础信息
     * @param vivo   微信数据
     * @return hash
     */
    private HashMap<String, Object> hash(UserInfo userInfo, UserInfoVivo vivo)
    {
        HashMap<String, Object> hash = new HashMap<>();
        hash.putAll(XwhTool.getClassInfo(UserInfo.class, userInfo));
        hash.putAll(XwhTool.getClassInfo(UserInfoVivo.class, vivo));
        return hash;
    }

    /**
     * 設置玩家信息
     */
    private UserInfo setUserInfo()
    {
        UserInfo basic = new UserInfo();
        basic.setPlatform(request().platform.name());
        basic.setDevice(request().device.name());
        basic.setRegistTime(new Timestamp(System.currentTimeMillis()));
        basic.setNickName(request().nickname);
        basic.setLoginTime(basic.getRegistTime());
        basic.setIcon(request().avatar);
        return basic;
    }

    @Override
    public ERROR error()
    {
        return error;
    }

    @Override
    public LoginCode setLoginCode(UserCache userCache)
    {
        return LoginCode.createLogonCode(userCache, LoginRequest.Platform.vivo);
    }
}
