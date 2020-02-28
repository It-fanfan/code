package com.code.service.friend;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.dto.UserIdDTO;
import com.code.dao.entity.fish.config.ConfigEmail;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.fish.userinfo.UserShip;
import com.code.dao.entity.message.ApplyFriend;
import com.code.protocols.basic.BasePro;
import com.code.protocols.login.Init;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.protocols.social.SocialBase;
import com.code.protocols.social.friend.FriendInfo;
import com.code.service.achievement.AchievementService;
import com.code.service.gm.RobotService;
import com.code.service.login.UserInfoService;
import com.code.service.message.NoticeService;
import com.code.service.range.BookRankingService;
import com.code.service.ui.FlushService;
import com.code.service.work.WorkService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 好友服务信息
 */
public class FriendService
{
    //日志驱动
    private static final Logger LOG = LoggerFactory.getLogger(FriendService.class);

    //玩家信息
    private UserCache userCache;

    public FriendService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 进行拆解邀请码
     *
     * @param friendCode 邀请码
     * @return 邀请码包含用户编号
     */
    public static long spiltCode(String friendCode)
    {
        try
        {
            if (friendCode.length() < 4)
                return 0;
            String hex = friendCode.substring(2, friendCode.length() - 2);
            return Long.parseLong(hex, 16);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return 0;
    }

    /**
     * 获取好友列表信息
     *
     * @return 好友列表
     */
    private static UserFriend getFriendInfo(UserCache userCache)
    {
        String field = friendRedis();
        String json = userCache.getValue(field);
        UserFriend userFriend = null;
        if (json != null)
            userFriend = XwhTool.parseJSONByFastJSON(json, UserFriend.class);
        int dayFlag = XwhTool.getCurrentDateValue();
        if (userFriend == null)
        {
            userFriend = new UserFriend();
            userFriend.friend = new Vector<>();
            userFriend.presented = new HashMap<>();
        }
        if (userFriend.dayFlag != dayFlag)
        {
            userFriend.dayFlag = dayFlag;
            userFriend.presented.clear();
            userFriend.presented = new HashMap<>();
        }
        return userFriend;
    }

    /**
     * 好友节点信息
     *
     * @return 返回节点值
     */
    private static String friendRedis()
    {
        return "friend-ship";
    }

    /**
     * 好友申请节点
     */
    private static String applyRedis()
    {
        return "friend-apply-ext";
    }

    /**
     * 获取查詢SQL
     *
     * @param contain 包含玩家
     * @param limit   隨機幾位
     * @return sql
     */
    private static String getSelectUser(Set<String> contain, int limit)
    {
        LOG.error("进行查询推荐好友:当前排除项:" + contain);
        StringBuilder builder = new StringBuilder();
        builder.append("select userId from user_info where userId not in(");
        int len = builder.length();
        for (String userId : contain)
        {
            if (len != builder.length())
                builder.append(",");
            builder.append(userId);
        }
        builder.append(") ORDER BY RAND() LIMIT ").append(limit);
        return builder.toString();
    }

    /**
     * 赠送配置
     */
    private static SocialBase.PresentedConfig presentedConfig()
    {
        String text = Systemstatusinfo.getText("friend-presented-limit");
        return XwhTool.parseJSONByFastJSON(text, SocialBase.PresentedConfig.class);
    }

    /**
     * 好友配置参数
     */
    public static Init.FriendInit getInitConfig()
    {
        Init.FriendInit init = new Init.FriendInit();
        init.presented = presentedConfig();
        init.protectedcd = Systemstatusinfo.getLong("friend_guard_cd", "3600000");
        init.protectedlimit = Systemstatusinfo.getInt("friend_guard_limit", "5");
        return init;
    }

    /**
     * 获取玩家好友申请记录
     *
     * @return 记录信息
     */
    private FriendApply getFriendApply()
    {
        String json = userCache.getValue(applyRedis());
        FriendApply apply = XwhTool.parseJSONByFastJSON(json, FriendApply.class);
        if (apply == null)
            apply = new FriendApply();
        return apply;
    }

    /**
     * 更新玩家好友申请记录
     *
     * @param apply 记录信息
     */
    private void updateFriendApply(FriendApply apply)
    {
        userCache.hSet(applyRedis(), XwhTool.getJSONByFastJSON(apply));
    }

    /**
     * 获取友情码
     *
     * @return 友情码
     */
    public String getFriendCode()
    {
        try
        {
            String userId = userCache.getUserId();
            String openid = userCache.getOpenid();
            String hex = String.format("%05x", Long.valueOf(userId));
            if (openid != null && openid.length() >= 4)
            {
                String code = openid.substring(0, 2) + hex + openid.substring(openid.length() - 2);
                return code.toUpperCase();
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return String.format("%s%s%s", "HD", "XXX", "Q2").toUpperCase();
    }

    /**
     * 检测是否为好友
     *
     * @param friendId 好友编号
     * @return bool
     */
    public boolean isFriend(String friendId)
    {
        UserFriend data = getFriendInfo(userCache);
        return data.friend.contains(friendId);
    }

    /**
     * 添加申请记录
     *
     * @param friendCache 申請玩家
     */
    private void addApplyFriend(UserCache friendCache)
    {
        try
        {
            ApplyFriend applyFriend = new ApplyFriend(friendCache.userId(), userCache.userId(), userCache.getNickName(), friendCache.getNickName());
            FishInfoDb.instance().saveOrUpdate(applyFriend, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 添加申请列表
     *
     * @param friendCache 好友编号
     * @return bool
     */
    public boolean addApplyUser(UserCache friendCache)
    {
        String userId = userCache.getUserId();
        if (friendCache == null)
        {
            LOG.error("当前玩家不存在:userId=" + userId);
            return false;
        }
        if (userId.equals(friendCache.getUserId()))
        {
            LOG.error("相同玩家:userId=" + userId);
            return false;
        }
        String friendId = friendCache.getUserId();
        //好友关系无需申请
        if (isFriend(friendId))
        {
            LOG.error("当前是好友关系:friendId=" + friendId + ",userId=" + userId);
            return false;
        }
        FriendApply apply = getFriendApply();
        //添加申请记录
        if (!apply.addRecord(friendId))
        {
            LOG.error("当前已经申请过:friendId=" + friendId + ",userId=" + userId);
            return false;
        }
        if (existAddFriend(friendCache, apply))
        {
            return true;
        }
        //保存用户记录信息
        updateFriendApply(apply);
        addApplyFriend(friendCache);
        //更新玩家被动刷新节点
        new FlushService(friendCache).addFlag(FlushService.FlushType.user);
        return true;
    }

    /**
     * 检测是否添加成好友
     *
     * @param friendCache 好友缓存
     * @param apply       玩家申请记录
     * @return 是否成功添加好友
     */
    private boolean existAddFriend(UserCache friendCache, FriendApply apply)
    {
        String friendId = friendCache.getUserId();
        String userId = userCache.getUserId();
        FriendService service = new FriendService(friendCache);
        FriendApply friendApply = service.getFriendApply();
        //检测是否可成为好友
        if (apply.existCanBeFriend(friendId))
        {
            addFriend(friendCache, service);
            apply.removeRecord(friendId);
            friendApply.removeRecord(userId);
            updateFriendApply(apply);
            service.updateFriendApply(friendApply);
            return true;
        }
        //添加被动申请记录
        friendApply.addPassive(userId);
        service.updateFriendApply(friendApply);
        return false;
    }

    /**
     * 添加申请列表
     *
     * @param friendId 好友编号
     */
    public void addApplyUser(String friendId)
    {
        UserCache friendCache = UserCache.getUserCache(friendId);
        addApplyUser(friendCache);
    }

    /**
     * 添加好友信息节点
     *
     * @param friendCache 好友列表
     * @param service     好友信息
     */
    private void addFriend(UserCache friendCache, FriendService service)
    {
        //相互添加好友
        addFriend(friendCache.getUserId());
        service.addFriend(userCache.getUserId());
        //添加入數據庫
        try
        {
            long userId = userCache.userId(), friendId = friendCache.userId();
            UserShip ship = new UserShip();
            if (userId > friendId)
            {
                ship.setUserId(friendId);
                ship.setFriendId(userId);
            } else
            {
                ship.setUserId(userId);
                ship.setFriendId(friendId);
            }
            ship.setInsertTime(new Timestamp(System.currentTimeMillis()));
            FishInfoDb.instance().saveOrUpdate(ship, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

    }

    /**
     * 创建好友节点信息
     *
     * @param friendCache 好友缓存信息
     * @return 好友节点
     */
    public FriendInfo createFriendInfo(UserCache friendCache)
    {

        String userId = friendCache.getUserId();
        String nickname = friendCache.getNickName();
        String icon = friendCache.getIcon();
        int basin = friendCache.getBasin();
        FriendInfo friendInfo = new FriendInfo(userId, nickname, icon, basin);
        //檢測是否被申請過
        FriendService service = new FriendService(friendCache);
        friendInfo.isapply = service.getFriendApply().passive.contains(userCache.getUserId());
        return friendInfo;
    }

    /**
     * 获取用户好友信息
     *
     * @param userFriend  玩家好友信息节点
     * @param init        推荐数值
     * @param friendCache 好友缓存
     * @return 好友节点
     */
    private FriendInfo getUserFriend(UserFriend userFriend, Init.FriendInit init, UserCache friendCache)
    {
        FriendInfo info = createFriendInfo(friendCache);
        //进行判断好友是否可赠送
        info.presented = init.presented.limit;
        Integer count = userFriend.presented.get(friendCache.getUserId());
        if (count != null)
            info.presented -= count;
        //TODO:皮肤
        //获取玩家
        //TODO:获取图鉴数据以及好友的世界排行
        Tuple tuple = BookRankingService.getInstance().getUserRankingAndBooks(friendCache);
        if (tuple != null)
        {
            info.gloal = Integer.valueOf(tuple.getElement());
            info.booknum = (int) tuple.getScore();
        }
        return info;
    }

    /**
     * 好友节点信息
     *
     * @param applyId 好友缓存
     */
    public FriendInfo getUserFriend(String applyId)
    {
        UserCache friendCache = UserCache.getUserCache(applyId);
        UserFriend userFriend = getFriendInfo(userCache);
        Init.FriendInit init = getInitConfig();
        return getUserFriend(userFriend, init, friendCache);
    }

    /**
     * 获取好友数据节点
     *
     * @return 好友节点信息
     */
    public Vector<FriendInfo> getFriendList()
    {
        Vector<FriendInfo> temp = new Vector<>();
        UserFriend userFriend = getFriendInfo(userCache);
        if (userFriend == null)
            return temp;
        Init.FriendInit init = getInitConfig();
        Set<String> friends = new HashSet<>(userFriend.friend);
        for (String userId : friends)
        {
            UserCache friendCache = UserCache.getUserCache(userId);
            if (friendCache != null)
            {
                FriendInfo info = getUserFriend(userFriend, init, friendCache);
                temp.add(info);
            }
        }
        return temp;
    }

    /**
     * 设置好友数据
     *
     * @param userFriend 好友信息
     */
    private void putUserFriend(UserFriend userFriend)
    {
        String field = friendRedis();
        userCache.hSet(field, XwhTool.getJSONByFastJSON(userFriend));
    }

    /**
     * 添加好友信息列表
     *
     * @param friendId 玩家编号
     */
    private void addFriend(String friendId)
    {
        UserFriend data = getFriendInfo(userCache);
        if (data.friend.contains(friendId))
        {
            LOG.debug("these are friend!" + friendId + "," + userCache.getUserId());
            return;
        }
        data.friend.add(friendId);
        Collections.sort(data.friend);
        putUserFriend(data);
        //移除申请记录
        removeApplyFriend(friendId);
        //进度更新
        try (WorkService workService = new WorkService(userCache))
        {
            workService.addProcess(OperatorBase.ActivityType.addFriend, 1);
        }
        //添加成就
        new AchievementService(userCache).addAchievement(AchievementType.Friends, 1);
    }

    /**
     * 获取被动申请玩家
     *
     * @return 好友信息
     */
    public Vector<FriendInfo> getApplyList()
    {
        FriendApply apply = getFriendApply();
        Vector<FriendInfo> list = new Vector<>();
        for (String friendId : apply.passive)
        {
            UserCache friendCache = UserCache.getUserCache(friendId);
            if (friendCache != null)
            {
                list.add(createFriendInfo(friendCache));
            }
        }
        return list;
    }

    /**
     * 获取推荐列表
     *
     * @return 推荐信息
     */
    public Vector<FriendInfo> getRecommend()
    {
        Vector<FriendInfo> infos = new Vector<>();
        //4个坑位
        int limit = Systemstatusinfo.getInt("recommend_max_size");
        try
        {
            Set<String> contain = new HashSet<>();
            contain.add(userCache.getUserId());
            //添加申請好友节点
            FriendApply apply = getFriendApply();
            contain.addAll(apply.record);
            //好友关系排除
            UserFriend userFriend = getFriendInfo(userCache);
            if (userFriend != null)
                contain.addAll(userFriend.friend);
            Vector<String> sameFriend = getSameFriendList((limit * 3) >> 2, contain);
            //获取申请好友列表
            infos = sameFriend.stream().map(userId -> createFriendInfoNode(userId, true)).filter(Objects::nonNull).collect(Collectors.toCollection(Vector::new));
            int surplus = limit - infos.size();
            if (surplus > 0)
            {
                //添加剩余玩家
                Vector<UserIdDTO> dto = UserInfoService.selectUserIdBySQL(getSelectUser(contain, surplus));
                if (dto != null)
                {
                    infos.addAll(dto.stream().map(element -> createFriendInfoNode(String.valueOf(element.userId), false)).filter(Objects::nonNull).collect(Collectors.toCollection(Vector::new)));
                }
            }
            //二次移除补救措施
            for (int i = infos.size() - 1; i >= 0; i--)
            {
                FriendInfo info = infos.elementAt(i);
                if ((userFriend != null && userFriend.friend.contains(info.userid)) || apply.record.contains(info.userid))
                {
                    infos.removeElementAt(i);
                }
            }
        } finally
        {
            if (infos.size() < limit)
            {
                int basinId = userCache.getBasin();
                int size = infos.size();
                Vector<FriendInfo> data = createRobotData(basinId, (limit - size));
                if (data != null && !data.isEmpty())
                    infos.addAll(data);
            }
        }
        return infos;
    }

    /**
     * 创建机器人好友信息
     *
     * @param basinId 水域
     * @param size    个数
     */
    private Vector<FriendInfo> createRobotData(int basinId, int size)
    {
        if (size <= 0)
            return null;
        Vector<FriendInfo> data = new Vector<>();
        Set<String> robotIds = new HashSet<>();
        for (int i = 0; i < size; i++)
        {
            BasePro.RewardUser robot = RobotService.createRobot(basinId, robotIds);
            robotIds.add(robot.userid);
            FriendInfo info = new FriendInfo(robot.userid, robot.nickname, robot.icon, robot.basin, false, SocialBase.APPLY.DESTINED);
            data.add(info);
        }
        return data;
    }

    /**
     * 創建好友節點信息
     *
     * @param userId 玩家編號
     * @return data
     */
    public FriendInfo createFriendInfoNode(String userId, boolean sameFriend)
    {
        UserCache friendCache = UserCache.getUserCache(userId);
        if (friendCache != null)
        {
            FriendInfo info = createFriendInfo(friendCache);
            info.samefriend = sameFriend;
            if (!sameFriend)
            {
                info.tip = SocialBase.APPLY.DESTINED;
            } else
            {
                info.tip = SocialBase.APPLY.SAME;
            }
            return info;
        }
        return null;
    }

    /**
     * 获取上限，共享好友
     *
     * @param limit     阀值
     * @param unContain 排除玩家列表
     * @return 好友编号
     */
    private Vector<String> getSameFriendList(int limit, Set<String> unContain)
    {
        LOG.error("进行查询共同好友:当前排除项:" + unContain);
        Set<String> sameFriend = new HashSet<>();
        UserFriend userFriend = getFriendInfo(this.userCache);
        Vector<String> friends = new Vector<>(userFriend.friend);
        Collections.shuffle(friends);
        //进行打乱好友顺序
        for (String userId : friends)
        {
            UserCache friendCache = UserCache.getUserCache(userId);
            if (friendCache == null)
                continue;
            UserFriend data = getFriendInfo(friendCache);
            for (String friend : data.friend)
            {
                if (unContain.contains(friend))
                    continue;
                if (limit != -1 && sameFriend.size() >= limit)
                    break;
                sameFriend.add(friend);
            }
            unContain.addAll(data.friend);
        }
        return new Vector<>(sameFriend);
    }

    /**
     * 提交申请结果
     *
     * @param applyId 申请玩家
     * @param result  申请结果
     * @return 申请结果
     */
    public boolean submitFriend(String applyId, boolean result)
    {
        //同一玩家，进行处理
        String userId = userCache.getUserId();
        if (applyId.equals(userId))
            return false;
        if (isFriend(applyId))
        {
            return true;
        }
        UserCache friendCache = UserCache.getUserCache(applyId);
        if (friendCache == null)
            return false;
        FriendApply apply = getFriendApply();
        if (result)
        {
            //添加主动申请记录
            apply.record.add(applyId);
            //检测是否为好友
            if (existAddFriend(friendCache, apply))
            {
                new FlushService(friendCache).addFlag(FlushService.FlushType.user);
                return true;
            }
            return false;
        }
        //移除被动申请记录
        apply.passive.removeElement(applyId);
        updateFriendApply(apply);
        removeApplyFriend(applyId);
        FriendService service = new FriendService(friendCache);
        FriendApply friendApply = service.getFriendApply();
        friendApply.record.removeElement(userId);
        service.updateFriendApply(friendApply);
        return true;
    }

    /**
     * 删除申请记录信息
     *
     * @param friendId 申请编号
     */
    private void removeApplyFriend(String friendId)
    {
        String userId = userCache.getUserId();
        //组信息
        String SQL = String.format("delete from apply_friend where (userId='%s' and applyId='%s') ", userId, friendId);
        FishInfoDb.instance().addQueue(SQL);
    }

    /**
     * 好友赠送逻辑信息
     *
     * @param friend 好友编号
     */
    public SocialBase.ERROR presented(String friend)
    {
        UserFriend userFriend = getFriendInfo(userCache);
        if (!userFriend.friend.contains(friend))
        {
            return SocialBase.ERROR.NOTFRIEND;
        }
        Integer count = userFriend.presented.get(friend);
        Init.FriendInit init = getInitConfig();
        if (count == null || count < init.presented.limit)
        {
            if (count == null)
                count = 0;
            userFriend.presented.put(friend, ++count);
            putUserFriend(userFriend);
            UserCache friendCache = UserCache.getUserCache(friend);
            //添加邮件数据
            NoticeService service = new NoticeService(friendCache);
            ConfigEmail configEmail = NoticeService.getConfigEmail(OperatorBase.MessageType.present);
            String context = String.format(configEmail.getMsg(), userCache.getNickName(), String.valueOf(init.presented.value));
            JSONObject json = new JSONObject();
            json.put("userId", userCache.getUserId());
            service.addMessageInfo(json, context, configEmail);

            return SocialBase.ERROR.SUCCESS;
        }
        return SocialBase.ERROR.ILLEGALPRESENTED;
    }

    public static class UserFriend
    {
        //好友列表
        public List<String> friend;
        //时间
        public int dayFlag;
        //赠送
        public Map<String, Integer> presented;
    }
}
