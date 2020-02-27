package com.code.service.ui;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigInvite;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.fish.userinfo.UserReward;
import com.code.protocols.basic.BasePro;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.utils.Member;
import com.code.protocols.operator.utils.ReceiveReward;
import com.code.protocols.operator.utils.UtilsType;
import com.code.service.reward.RewardService;
import com.code.service.trade.MarketService;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;

public class InviteService
{
    private static final Logger LOG = LoggerFactory.getLogger(InviteService.class);
    protected UserCache userCache;

    public InviteService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 獲取獎勵配置信息
     *
     * @return 獎勵配置
     */
    public static Map<String, OperatorBase.WelfareInfo> getRewardUtilConfig()
    {
        String json = Systemstatusinfo.getText("reward_activity");
        if (json == null)
            return new HashMap<>();
        Map<String, OperatorBase.WelfareInfo> data = XwhTool.parseJSONByFastJSON(json, new TypeToken<Map<String, OperatorBase.WelfareInfo>>()
        {
        }.getType());
        if (data == null)
            return new HashMap<>();
        return data;
    }

    /**
     * 进行配置邀请奖励数据
     *
     * @return cache
     */
    public static Map<String, Integer> getInviteReceiveConfig()
    {
        Map<String, Integer> data = new LinkedHashMap<>();
        Vector<ConfigInvite> invites = FishInfoDb.instance().getCacheListByClass(ConfigInvite.class);
        if (invites != null)
        {
            for (ConfigInvite invite : invites)
            {
                data.put(String.valueOf(invite.getInviteNum()), invite.getAwardNum());
            }
        }
        return data;
    }

    /**
     * 查询用户獎勵領取
     *
     * @param userId     玩家编号
     * @param rewardType 獎勵類型
     * @return reward data
     */
    private static Vector<UserReward> searchUserReward(String userId, String rewardType)
    {
        try
        {
            String sql = String.format("select * from user_reward where userId=%s and rewardType='%s'", userId, rewardType);
            return FishInfoDb.instance().findBySQL(sql, UserReward.class);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 获取玩家奖励数据
     *
     * @param userCache 玩家信息
     * @return 奖励信息
     */
    public static ReceiveReward getUserReceiveReward(UserCache userCache)
    {
        int dayFlag = XwhTool.getCurrentDateValue();
        String json = userCache.getValue(getRewardReceiveRedis());
        ReceiveReward reward = null;
        if (json != null)
            reward = XwhTool.parseJSONByFastJSON(json, ReceiveReward.class);
        if (reward == null || reward.dayFlag != dayFlag)
        {
            if (reward == null)
            {
                reward = new ReceiveReward();
            }
            reward.dayFlag = dayFlag;
            reward.utils.clear();
        }
        return reward;
    }

    /**
     * 邀请节点
     *
     * @return redis
     */
    private static String getInviteRedis()
    {
        return "invite-data";
    }

    /**
     * 领取节点
     */
    private static String getRewardReceiveRedis()
    {
        return "reward-receive";
    }

    /**
     * 邀请标志位信息
     *
     * @return field
     */
    private String getRedisField()
    {
        return "flag_invite";
    }

    /**
     * 新增邀请标志位
     *
     * @param inch 增量
     * @return 标志信息
     */
    private void inchInviteFlag(int inch)
    {
        if (userCache != null)
        {
            userCache.hincrBy(getRedisField(), inch);
        }
    }

    /**
     * 进行判断新增标志位
     */
    public boolean uiFlag()
    {
        String value = userCache.getValue(getRedisField());
        return value != null && Long.valueOf(value) > 0;
    }

    /**
     * 進行领取奖励数据
     *
     * @param index 领取索引
     * @return add shell
     */
    public OperatorBase.ERROR receiveReward(int index)
    {
        //领取索引+1
        Integer price = getInviteReceiveConfig().get(String.valueOf(index + 1));
        if (price == null || price <= 0)
        {
            return OperatorBase.ERROR.LIMIT_REACHED;
        }
        LOG.debug("领取奖励:" + price);
        List<OperatorBase.Invite> invites = getInviteList();
        LOG.debug("领取玩家信息:" + XwhTool.getJSONByFastJSON(invites) + ",index=" + index);
        if (invites != null && invites.size() > index)
        {
            if (!invites.get(index).receive)
            {
                try
                {
                    UserReward reward = new UserReward();
                    reward.setUserId(userCache.userId());
                    reward.setPrice(price);
                    reward.setRewardType(RewardType.invite.name());
                    reward.setRewardIndex(index);
                    reward.setReceiveTime(new Timestamp(System.currentTimeMillis()));
                    FishInfoDb.instance().saveOrUpdate(reward, true);
                    if (this.userCache.incrShell(price))
                    {
                        inchInviteFlag(-1);
                        return OperatorBase.ERROR.SUCCESS;
                    }
                } catch (Exception e)
                {
                    LOG.error(Log4j.getExceptionInfo(e));
                }
            } else
            {
                return OperatorBase.ERROR.REPEATRECEIVE;
            }
        }
        return OperatorBase.ERROR.ERROR;
    }

    /**
     * 进行领取奖励
     *
     * @param type 类型
     */
    public OperatorBase.ERROR receiveUtils(UtilsType type)
    {
        ReceiveReward reward = getUserReceiveReward(userCache);
        Map<String, OperatorBase.WelfareInfo> config = getRewardUtilConfig();
        if (!config.containsKey(type.name()))
            return OperatorBase.ERROR.ERROR;
        //福利不存在
        OperatorBase.WelfareInfo welfare = config.get(type.name());
        int count = reward.getUtilsValue(type);
        if (++count > welfare.limit)
            return OperatorBase.ERROR.LIMIT_REACHED;
        reward.utils.put(type, count);
        BasePro.RewardInfo rewardInfo = new BasePro.RewardInfo(MarketService.WealthType.shell.name(), -1, welfare.price);
        new RewardService(userCache).receiveReward(rewardInfo, "welfare-" + type.name());
        putUserReceiveReward(reward, userCache);
        return OperatorBase.ERROR.SUCCESS;
    }

    /**
     * 添加邀请记录
     *
     * @param inviteUser 邀请玩家
     */
    public void addInviteList(UserCache inviteUser)
    {
        //增加标志位
        inchInviteFlag(1);
        //添加玩家信息
        String field = getInviteRedis();
        String value = userCache.getValue(field);
        Map<String, String> hash = null;
        if (value != null)
        {
            hash = XwhTool.parseJSONByFastJSON(value, new TypeToken<Map<String, String>>()
            {
            }.getType());
        }
        if (hash == null)
            hash = new HashMap<>();
        //key为元素单位
        Member member = new Member();
        member.userid = inviteUser.getUserId();
        member.icon = inviteUser.getIcon();
        member.nickname = inviteUser.getNickName();
        String key = XwhTool.getJSONByFastJSON(member);
        hash.put(key, String.valueOf(System.currentTimeMillis()));
        String json = XwhTool.getJSONByFastJSON(hash);
        userCache.hSet(field, json);
    }

    /**
     * 获取玩家已邀请列表
     *
     * @return 列表信息
     */
    private Map<String, String> getUserInviteList()
    {
        String field = getInviteRedis();
        String value = this.userCache.getValue(field);
        if (value != null)
        {
            return XwhTool.parseJSONByFastJSON(value, new TypeToken<Map<String, String>>()
            {
            }.getType());
        }
        return null;
    }

    /**
     * 邀请list
     *
     * @return 邀请列表
     */
    public Vector<OperatorBase.Invite> getInviteList()
    {
        Vector<OperatorBase.Invite> data = new Vector<>();
        //获取邀请列表
        Map<String, String> invites = getUserInviteList();
        if (invites == null || invites.isEmpty())
        {
            return data;
        }
        for (Map.Entry<String, String> entry : invites.entrySet())
        {
            OperatorBase.Invite element = XwhTool.parseJSONByFastJSON(entry.getKey(), OperatorBase.Invite.class);
            if (element != null)
            {
                data.add(element);
                element.register = Long.valueOf(entry.getValue());
            }
        }
        //按照时间从小到大排序
        data.sort(Comparator.comparingLong(o -> o.register));
        //已奖励数据
        Vector<UserReward> user_rewards = searchUserReward(userCache.getUserId(), RewardType.invite.name());
        if (user_rewards != null)
        {
            for (UserReward reward : user_rewards)
            {
                int index = reward.getRewardIndex();
                if (data.size() > index)
                {
                    OperatorBase.Invite element = data.elementAt(index);
                    element.receive = true;
                    element.price = reward.getPrice();
                }
            }
        }
        return data;
    }

    /**
     * 进行奖励数据
     *
     * @param reward 奖励值
     */
    private void putUserReceiveReward(ReceiveReward reward, UserCache userCache)
    {
        userCache.hSet(getRewardReceiveRedis(), XwhTool.getJSONByFastJSON(reward));
    }

    //奖励情景
    enum RewardType
    {
        invite,//邀请
        appid,//跳转地址
    }
}
