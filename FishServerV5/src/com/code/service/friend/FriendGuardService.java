package com.code.service.friend;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigEmail;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.userinfo.UserGuard;
import com.code.dao.entity.record.RecordFriendGuard;
import com.code.protocols.basic.BasePro.FriendPro;
import com.code.protocols.login.Init;
import com.code.protocols.operator.OperatorBase;
import com.code.service.message.NoticeService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.code.protocols.social.SocialBase.ERROR;

public class FriendGuardService
{
    private static Logger LOG = LoggerFactory.getLogger(FriendGuardService.class);
    private UserCache userCache;
    private FriendGuard friendGuard = null;

    public FriendGuardService(UserCache userCache)
    {
        this.userCache = userCache;
    }


    /**
     * 查询玩家图鉴守护用户
     *
     * @return 正在守护己方好友
     */
    public static Vector<FriendPro> getBookGuard(UserCache userCache)
    {
        FriendGuard guard = getFriendGuard(userCache);
        Vector<FriendPro> result = new Vector<>();
        guard.friends.forEach(friendId -> result.add(new FriendPro(friendId, true, "0")));
        Init.FriendInit init = FriendService.getInitConfig();
        guard.history.forEach((k, v) ->
        {
            if ((init.protectedcd + v) >= System.currentTimeMillis())
            {
                //守护结束，依旧CD期
                result.add(new FriendPro(k, false, String.valueOf(v)));
            }
        });
        return result;
    }

    /**
     * 获取查询好友守护信息
     *
     * @return 节点
     */
    private static FriendGuard getFriendGuard(UserCache userCache)
    {
        String field = getUserGuardRedis();
        String json = userCache.getValue(field);
        int basinId = Integer.valueOf(userCache.getBasin());
        FriendGuard guard = null;
        if (json != null)
            guard = XwhTool.parseJSONByFastJSON(json, FriendGuard.class);
        if (guard == null)
        {
            guard = new FriendGuard();
            guard.history = new HashMap<>();

        }
        if (guard.basinId != basinId)
        {
            guard.basinId = basinId;
        }
        if (guard.friends == null)
            guard.friends = new Vector<>();
        return guard;
    }

    /**
     * 設置好友守護信息
     *
     * @param userCache 玩家
     * @param guard     守护信息
     */
    private static void putFriendGuard(UserCache userCache, FriendGuard guard) throws Exception
    {
        String field = getUserGuardRedis();
        String json = XwhTool.getJSONByFastJSON(guard);
        userCache.hSet(field, json);
        //TODO:同步数据库
        UserGuard userGuard = new UserGuard();
        userGuard.setUserId(userCache.userId());
        userGuard.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userGuard.setFriendGuard(json);
        FishInfoDb.instance().saveOrUpdate(userGuard, true);
    }

    /**
     * 好友守护模块节点
     *
     * @return redis key
     */
    private static String getUserGuardRedis()
    {
        return "friend-guard";
    }

    /**
     * 进行守护
     *
     * @return 守护是否成功
     */
    public String protectedFriend(int ftId) throws Exception
    {
        FriendGuard guard = getFriendGuard();
        if (guard.friends.isEmpty())
            return null;
        String friendId = guard.friends.remove(0);
        guard.history.put(friendId, System.currentTimeMillis());
        setFriendGuard(guard);
        addProtectedRecord(friendId, ftId);
        return friendId;
    }

    /**
     * 添加守护图鉴记录
     *
     * @param friendId 好友编号
     * @param ftId     图鉴编号
     */
    private void addProtectedRecord(String friendId, int ftId)
    {
        try
        {
            ConfigFish configFish = (ConfigFish) FishInfoDb.instance().getCacheKey(ConfigFish.class, new String[]{"ftId", String.valueOf(ftId)});
            //udp消息
            UserCache friendCache = UserCache.getUserCache(friendId);
            if (configFish != null && friendCache != null)
            {
                //添加邮件数据
                NoticeService service = new NoticeService(friendCache);
                ConfigEmail configEmail = NoticeService.getConfigEmail(OperatorBase.MessageType.friendProtected);
                String context = String.format(configEmail.getMsg(), userCache.getNickName());
                JSONObject json = new JSONObject();
                json.put("userId", userCache.getUserId());
                service.addMessageInfo(json, context, configEmail);
            }

        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

    }

    /**
     * 添加好友守护列表
     *
     * @param friendCache 好友信息
     * @return 是否允许添加
     */
    public ERROR addFriendGuard(UserCache friendCache)
    {
        try
        {
            Init.FriendInit init = FriendService.getInitConfig();
            FriendGuard guard = getFriendGuard();
            String friendId = friendCache.getUserId();
            if (guard.friends.contains(friendId))
                return ERROR.GUARD_REPEAT;
            if (guard.friends.size() >= init.protectedlimit)
                return ERROR.GUARD_LIMIT;
            Long historyTime = guard.history.get(friendId);
            if (historyTime != null && historyTime + init.protectedcd > System.currentTimeMillis())
                return ERROR.NO_CD;
            guard.friends.add(friendId);
            setFriendGuard(guard);
            //TODO:記錄保護記錄
            saveRecord(friendId, guard);
            return ERROR.SUCCESS;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return ERROR.GUARD_FAIL;
    }

    /**
     * 保存记录信息
     *
     * @param friendId 好友列表
     * @param guard    保护信息
     */
    private void saveRecord(String friendId, FriendGuard guard) throws Exception
    {
        String userId = userCache.getUserId();
        RecordFriendGuard record = new RecordFriendGuard();
        record.setBasinId(guard.basinId);
        record.setFriendId(Long.valueOf(friendId));
        record.setInsertTime(new Timestamp(System.currentTimeMillis()));
        record.setUserId(Long.valueOf(userId));
        FishInfoDb.instance().saveOrUpdate(record, true);
    }

    /**
     * 获取好友守护数据
     *
     * @return 守护数据
     */
    private FriendGuard getFriendGuard()
    {
        if (friendGuard == null)
            friendGuard = getFriendGuard(userCache);
        return friendGuard;
    }

    /**
     * 设置好友守护数据
     */
    private void setFriendGuard(FriendGuard guard) throws Exception
    {
        putFriendGuard(userCache, guard);
    }

    /**
     * 好友守护节点
     */
    public static class FriendGuard
    {
        //守护水域
        public int basinId;
        //好友列表
        public Vector<String> friends;
        //守护历史记录
        public Map<String, Long> history;
    }
}
