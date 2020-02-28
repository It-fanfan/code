package com.code.service.login;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.utils.XWHMathTool;
import com.utils.db.BatchSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface UserService<T>
{
    Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * 请求节点
     *
     * @return 节点
     */
    T request();

    /**
     * 登录检测,返回正确用户编号
     */
    String existLogin();

    /**
     * 进行用户编号检测
     *
     * @param userId 用户信息
     * @return 是否存在
     */
    default boolean exist(String userId)
    {
        if (!XWHMathTool.isNumeric(userId) || userId.equals("0"))
            return false;
        return UserCache.exist(userId);
    }

    /**
     * 进行注册
     */
    UserCache register() throws Exception;

    /**
     * 更新用户数据
     *
     * @param userCache 用户缓存
     */
    default void updateUserData(UserCache userCache)
    {

    }

    /**
     * 进行登录
     */
    default UserCache login(String userId)
    {
        if (error() != ERROR.success)
            return null;
        UserCache userCache = UserCache.getUserCache(userId);
        if (userCache != null)
        {
            updateUserData(userCache);
            updateLoginTime(userCache.getIcon(), userCache.getNickName(), Long.valueOf(userId));
        } else
        {
            setError(ERROR.api_code);
        }
        return userCache;
    }

    void setError(ERROR error);

    ERROR error();

    /**
     * 设置登录code
     *
     * @return 登录code
     */
    LoginCode setLoginCode(UserCache userCache);

    /**
     * 更新登录时间
     */
    default void updateLoginTime(String icon, String nickName, long userId)
    {
        BatchSQL batchSQL = new BatchSQL()
        {
            @Override
            public String getSQL()
            {
                return "update user_info set icon=?,nickName=?,loginTime=now() where userId=?";
            }

            @Override
            public int getLength()
            {
                return 1;
            }

            @Override
            public void addBatch(PreparedStatement prest, int index) throws SQLException
            {
                int parameterIndex = 1;
                prest.setString(parameterIndex++, icon);
                prest.setString(parameterIndex++, nickName);
                prest.setLong(parameterIndex, userId);
            }
        };
        FishInfoDb.instance().execBatchSQL(batchSQL);
        //进行设置用户集合
        UserCache.setRankingUser(userId, icon, nickName);
    }
}
