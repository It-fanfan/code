package com.code.service.login.quickgame;

import com.code.cache.UserCache;
import com.code.cache.UserRedisField;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.userinfo.UserInfo;
import com.code.dao.entity.fish.userinfo.UserInfoHuawei;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.sdk.Huawei;
import com.code.service.login.UserService;
import com.utils.XwhTool;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HuaweiUserService implements UserService<Huawei.RequestImpl>
{
    private Huawei.RequestImpl request;
    private ERROR error = ERROR.success;

    public HuaweiUserService(Huawei.RequestImpl request)
    {
        this.request = request;
    }

    @Override
    public Huawei.RequestImpl request()
    {
        return this.request;
    }

    @Override
    public String existLogin()
    {
        String userId = request().userid, openid = request().unionid;
        if (exist(userId))
        {
            UserCache userCache = UserCache.getUserCache(userId);
            if (userCache != null && userCache.getOpenid().equals(openid))
            {
                return userId;
            }
        }
        String SQL = "select * from user_info_huawei where unionId='" + openid + "'";
        Vector<UserInfoHuawei> data = FishInfoDb.instance().findBySQL(SQL, UserInfoHuawei.class);
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
        UserInfoHuawei huawei = new UserInfoHuawei();
        huawei.setOpenId(request().openid);
        huawei.setUnionId(request().unionid);
        if (request().unionid == null)
        {
            error = ERROR.nonsupport;
            return null;
        }
        huawei.setLoginTime(new Timestamp(System.currentTimeMillis()));
        String SQL = db.getEntitySQL(userInfo);
        userInfo.setUserId(db.insertGenerateKey(SQL));
        //进行保存微信数据
        huawei.setUserId(userInfo.getUserId());
        if (userInfo.getUserId() == 0)
        {
            error = ERROR.nonsupport;
            return null;
        }
        FishInfoDao.instance().saveOrUpdate(huawei, true);
        UserCache userCache = UserCache.putUserInfo(hash(userInfo, huawei));
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
     * @param huawei   微信数据
     * @return hash
     */
    private HashMap<String, Object> hash(UserInfo userInfo, UserInfoHuawei huawei)
    {
        HashMap<String, Object> hash = new HashMap<>();
        hash.putAll(XwhTool.getClassInfo(UserInfo.class, userInfo));
        hash.putAll(XwhTool.getClassInfo(UserInfoHuawei.class, huawei));
        return hash;
    }

    /**
     * 更新用户数据
     *
     * @param userCache 用户缓存
     */
    public void updateUserData(UserCache userCache)
    {
        Map<String, String> hash = new HashMap<>();
        //進行更新节点
        if (!userCache.getNickName().equals(request().nickname))
        {
            hash.put(UserRedisField.nickName.name(), request().nickname);
        }
        if (!userCache.getIcon().equals(request().avatar))
        {
            hash.put(UserRedisField.icon.name(), request().avatar);
        }
        userCache.setHashValue(hash);
        userCache.save(hash);
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
        return LoginCode.createLogonCode(userCache, LoginRequest.Platform.huawei);
    }
}
