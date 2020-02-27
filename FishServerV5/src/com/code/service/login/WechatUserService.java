package com.code.service.login;

import com.code.cache.UserCache;
import com.code.cache.UserRedisField;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.userinfo.UserInfo;
import com.code.dao.entity.fish.userinfo.UserInfoWechat;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.protocols.login.wechat.EncrypteUserData;
import com.code.protocols.login.wechat.LoginExt;
import com.code.service.friend.FriendService;
import com.code.service.ui.InviteService;
import com.utils.ReadConfig;
import com.utils.XWHMathTool;
import com.utils.XwhTool;
import com.utils.db.BatchSQL;
import com.utils.http.XwhHttp;
import com.utils.log4j.Log4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class WechatUserService implements UserService<LoginExt.RequestImpl>
{

    private LoginExt.RequestImpl request;
    private ERROR error = ERROR.success;
    private EncrypteUserData cyUserData = null;
    private String session_key = null;

    WechatUserService(LoginExt.RequestImpl request)
    {
        this.request = request;
    }

    /**
     * 获取好友信息
     *
     * @param userCache 玩家信息
     * @param friend    好友节点
     */
    private void setFriendInfo(UserCache userCache, LoginExt.FriendFrom friend)
    {
        if (friend != null && XWHMathTool.isNumeric(friend.friend) && !friend.friend.equals(userCache.getUserId()))
        {
            UserCache friendCache = UserCache.getUserCache(friend.friend);
            if (friendCache != null)
            {
                InviteService inviteService = new InviteService(friendCache);
                inviteService.addInviteList(userCache);
                //进行添加好友申请
                FriendService service = new FriendService(userCache);
                service.addApplyUser(friend.friend);
            }
            LOG.warn(friend.friend + "邀请标志增加!");
        }
    }

    /**
     * 解析群組信息
     *
     * @param encryptedData encrypted data
     * @param iv            iv
     * @return open gid
     */
    private String getEncryptOpenGId(String sessionKey, String encryptedData, String iv)
    {
        try
        {
            String result = encryptedData(sessionKey, encryptedData, iv);
            LOG.debug("解密数据:" + result);
            if (result != null)
            {
                Map map = XwhTool.parseJSONByFastJSON(result, Map.class);
                if (map != null && map.containsKey("openGId"))
                    return map.get("openGId").toString();
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 场景json数据
     */
    @SuppressWarnings("unchecked")
    private LoginExt.FriendFrom sceneTypeDeal(String sessionKey, Object object)
    {
        LoginExt.FriendFrom friendFrom = null;
        try
        {
            Map<String, Object> scenes = (Map<String, Object>) object;
            if (scenes != null && !scenes.isEmpty())
            {
                String scenetype = (String) scenes.get("scenetype");
                if (scenetype != null)
                {
                    if ("share".equals(scenetype))
                    {
                        friendFrom = new LoginExt.FriendFrom();
                        friendFrom.friend = (String) scenes.get("friend");
                        if (scenes.containsKey("shareTicket"))
                        {
                            Map<String, Object> shareTicket = (Map<String, Object>) scenes.get("shareTicket");
                            if (shareTicket != null && shareTicket.containsKey("iv") && shareTicket.containsKey("encryptedData"))
                            {
                                String iv = (String) shareTicket.get("iv");
                                String encryptedData = (String) shareTicket.get("encryptedData");
                                friendFrom.openGId = getEncryptOpenGId(sessionKey, encryptedData, iv);
                            }
                        }
                        return friendFrom;
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            LOG.debug("share data:" + XwhTool.getJSONByFastJSON(friendFrom));
        }
        return friendFrom;
    }

    @Override
    public LoginExt.RequestImpl request()
    {
        return request;
    }

    @Override
    public String existLogin()
    {
        EncrypteUserData cyUserData = cyUserData();
        if (cyUserData == null)
            return null;
        String userId = request().userid, openid = cyUserData.openId;
        if (exist(userId))
        {
            UserCache userCache = UserCache.getUserCache(userId);
            if (userCache != null && userCache.getOpenid().equals(openid))
            {
                return userId;
            }
        }
        Vector<UserInfoWechat> data = FishInfoDb.instance().findBySQL("select * from user_info_wechat where openId='" + openid + "'", UserInfoWechat.class);
        if (data == null || data.isEmpty())
            return null;
        return String.valueOf(data.firstElement().getUserId());
    }

    @Override
    public UserCache register() throws Exception
    {
        if (error != ERROR.success)
            return null;
        FishInfoDb db = FishInfoDb.instance();
        EncrypteUserData cyUserData = cyUserData();
        LoginExt.FriendFrom friend = friend();
        if (cyUserData == null)
            return null;
        UserInfo userInfo = new UserInfo();
        UserInfoWechat wechat = new UserInfoWechat();
        putUserInfo(cyUserData, friend, userInfo, wechat);
        String SQL = db.getEntitySQL(userInfo);
        userInfo.setUserId(db.insertGenerateKey(SQL));
        //进行保存微信数据
        wechat.setUserId(userInfo.getUserId());
        FishInfoDao.instance().saveOrUpdate(wechat, true);
        UserCache userCache = UserCache.putUserInfo(hash(userInfo, wechat));
        setFriendInfo(userCache, friend);
        userCache.isRegister = true;
        //进行设置用户集合
        UserCache.setRankingUser(userInfo.getUserId(), userInfo.getIcon(), userInfo.getNickName());
        return userCache;
    }


    /**
     * 进行hash值处理
     *
     * @param userInfo 用户基础信息
     * @param wechat   微信数据
     * @return hash
     */
    private HashMap<String, Object> hash(UserInfo userInfo, UserInfoWechat wechat)
    {
        HashMap<String, Object> hash = new HashMap<>();
        hash.putAll(XwhTool.getClassInfo(UserInfo.class, userInfo));
        hash.putAll(XwhTool.getClassInfo(UserInfoWechat.class, wechat));
        return hash;
    }

    /**
     * 进行设置数据信息
     *
     * @param cyUserData 加密数据
     * @param friend     好友数据
     */
    private void putUserInfo(EncrypteUserData cyUserData, LoginExt.FriendFrom friend, UserInfo userInfo, UserInfoWechat wechat)
    {
        userInfo.setPlatform(request().platform.name());
        userInfo.setDevice(request().device.name());
        setUserInfo(cyUserData, wechat, userInfo);
        if (friend != null && XWHMathTool.isNumeric(friend.friend))
        {
            wechat.setInvite(Long.valueOf(friend.friend));
            wechat.setInviteGroupId(friend.openGId);
        }
    }

    /**
     * 更新用户数据
     *
     * @param userCache 用户缓存
     */
    public void updateUserData(UserCache userCache)
    {
        try
        {
            EncrypteUserData data = cyUserData();
            if (data == null)
                return;
            Map<String, String> hash = new HashMap<>();
            //進行更新节点
            if (!userCache.getNickName().equals(cyUserData.nickName))
            {
                hash.put(UserRedisField.nickName.name(), cyUserData.nickName);
            }
            if (!userCache.getIcon().equals(cyUserData.avatarUrl))
            {
                hash.put(UserRedisField.icon.name(), cyUserData.avatarUrl);
            }
            if (!userCache.getSex().equals(String.valueOf(cyUserData.gender)))
                hash.put(UserRedisField.sex.name(), String.valueOf(cyUserData.gender));
            hash.put(UserRedisField.country.name(), String.valueOf(cyUserData.country));
            hash.put(UserRedisField.city.name(), String.valueOf(cyUserData.city));
            hash.put(UserRedisField.province.name(), String.valueOf(cyUserData.province));
            userCache.setHashValue(hash);
            userCache.save(hash);
            updateLoginData(cyUserData, userCache.userId());
        } finally
        {
            LoginExt.FriendFrom friend = friend();
            if (friend != null && userCache != null && !friend.friend.equals(userCache.getUserId()))
            {
                //进行添加好友申请
                FriendService service = new FriendService(userCache);
                service.addApplyUser(friend.friend);
            }
        }
    }

    @Override
    public void setError(ERROR error)
    {
        this.error = error;
    }

    private LoginExt.FriendFrom friend()
    {
        if (error != ERROR.success)
            return null;
        String sessionKey = sessionKey();
        if (sessionKey != null)
        {
            return sceneTypeDeal(sessionKey, request().scenejson);
        }
        return null;
    }

    /**
     * 解析微信加密数据
     *
     * @return 加密数据
     */
    private EncrypteUserData cyUserData()
    {
        if (error != ERROR.success)
            return null;
        if (cyUserData != null)
            return cyUserData;
        String sessionKey = sessionKey();
        String encryptedData = request().encrypteddata;
        String iv = request().iv;
        cyUserData = getUserInfo(sessionKey, encryptedData, iv);
        if (cyUserData == null)
            setError(ERROR.wecaht_api_user);

        return cyUserData;
    }

    /**
     * 获取session key
     *
     * @return sessionKey
     */
    private String sessionKey()
    {
        if (error != ERROR.success)
            return null;
        if (session_key == null)
        {
            try
            {
                LoginExt.Storage_SessionKey storage = getUserSessionKeyByCode();
                if (storage == null)
                {
                    setError(ERROR.wechat_api_login);
                    return session_key;
                }
                session_key = storage.session_key;
            } catch (Exception e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
        }
        return session_key;
    }

    /**
     * 通过微信API获取SessionKey
     */
    private LoginExt.Storage_SessionKey getUserSessionKeyByCode() throws Exception
    {
        String code = request().code, appid = request().appid;
        String session_key_url = ReadConfig.get("wx_get_session_key");
        if (session_key_url != null)
        {
            session_key_url = session_key_url.replace("#APPID#", appid);
            session_key_url = session_key_url.replace("#SECRET#", ReadConfig.get("secret"));
            session_key_url = session_key_url.replace("#JSCODE#", code);
            LOG.info(session_key_url);
            String sessionKeyJSONData = XwhHttp.sendGet(session_key_url);
            LOG.info(sessionKeyJSONData);
            if (!"errcode".contains(Objects.requireNonNull(sessionKeyJSONData)))
            {
                return XwhTool.parseJSONByFastJSON(sessionKeyJSONData, LoginExt.Storage_SessionKey.class);
            } else
            {
                LOG.error("登录code无效:" + sessionKeyJSONData + " code:" + code + " appid:" + appid);
                return null;
            }

        } else
        {
            LOG.error("sessionKeyURL 等于 NULL...");
        }
        return null;
    }

    /**
     * 解密用户数据
     *
     * @param encryptedData encrypted data
     * @param iv            iv
     * @return data
     */
    private EncrypteUserData getUserInfo(String sessionKey, String encryptedData, String iv)
    {
        try
        {
            String result = encryptedData(sessionKey, encryptedData, iv);
            if (result != null)
            {
                LOG.debug("解密数据:" + result);
                return XwhTool.parseConfigString(result, EncrypteUserData.class);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 进行解析数据
     *
     * @param sessionKey    wechat session key
     * @param encryptedData encrypted data
     * @param iv            iv
     * @return decode
     */
    private static String encryptedData(String sessionKey, String encryptedData, String iv)
    {
        byte[] dataByte = Base64.decode(encryptedData);

        byte[] keyByte = Base64.decode(sessionKey);

        byte[] ivByte = Base64.decode(iv);
        try
        {
            int base = 16;
            if (keyByte.length % base != 0)
            {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(2, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if ((resultByte != null) && (resultByte.length > 0))
            {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }


    @Override
    public ERROR error()
    {
        return error;
    }

    @Override
    public LoginCode setLoginCode(UserCache userCache)
    {
        LoginCode loginCode = new LoginCode();
        loginCode.setOpenId(userCache.getOpenid());
        loginCode.setUserId(userCache.getUserId());
        String sessionKey = sessionKey();
        loginCode.setPrivateData(request().appid, sessionKey);
        return loginCode;
    }

    /**
     * 設置玩家信息
     *
     * @param cyUserData 加密數據
     * @param userinfo   玩家信息
     */
    private void setUserInfo(EncrypteUserData cyUserData, UserInfoWechat userinfo, UserInfo basic)
    {
        userinfo.setSex(cyUserData.gender);
        basic.setRegistTime(new Timestamp(System.currentTimeMillis()));
        userinfo.setOpenId(cyUserData.openId);
        basic.setNickName(cyUserData.nickName);
        basic.setLoginTime(basic.getRegistTime());
        userinfo.setLoginTime(basic.getRegistTime());
        basic.setIcon(cyUserData.avatarUrl);
        userinfo.setUnionId(cyUserData.unionId);
        userinfo.setCity(cyUserData.city);
        userinfo.setCountry(cyUserData.country);
        userinfo.setProvince(cyUserData.province);
    }

    /**
     * 更新登陆数据
     *
     * @param cyUserData 加密数据
     */
    private void updateLoginData(EncrypteUserData cyUserData, long userId)
    {
        BatchSQL batchSQL = new BatchSQL()
        {
            @Override
            public String getSQL()
            {
                return "update user_info_wechat set sex=?,city=?,country=?,province=?,loginTime=now() where userId=?";
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
                prest.setInt(parameterIndex++, cyUserData.gender);
                prest.setString(parameterIndex++, cyUserData.city);
                prest.setString(parameterIndex++, cyUserData.country);
                prest.setString(parameterIndex++, cyUserData.province);
                prest.setLong(parameterIndex, userId);
            }
        };
        FishInfoDb.instance().execBatchSQL(batchSQL);
    }
}
