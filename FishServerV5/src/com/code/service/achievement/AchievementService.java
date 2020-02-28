package com.code.service.achievement;

import com.code.cache.UserCache;
import com.code.dao.entity.achievement.*;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.angling.AnglingResultExt;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.operator.OperatorBase.AchievementInfo;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.reward.RewardService;
import com.code.service.ui.FlushService;
import com.utils.XwhTool;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.code.protocols.operator.achievement.AchievementType.*;

/**
 * 成就系统
 */
public class AchievementService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AchievementService.class);
    private UserCache userCache;
    //成就配置数据
    private Map<String, String> receiveCache = null;

    public AchievementService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 获取最大combo值
     *
     * @param json 用户对应数据
     * @return 最大combo
     */
    private static int getMaxCombos(String json)
    {
        Map<Integer, Integer> process = XwhTool.parseJsonMap(json);
        //获取最大进度值
        if (process != null && !process.isEmpty())
        {
            Set<Integer> sets = process.keySet();
            Integer[] values = new Integer[sets.size()];
            values = sets.toArray(values);
            Arrays.sort(values);
            return values[values.length - 1];
        }
        return 0;
    }

    public static void main(String[] args)
    {
        String json = "{2:23,3:12,4:7,5:4,8:1}";
        int max = getMaxCombos(json);
        System.out.println(max);
    }

    /**
     * 获取成就体系数据
     *
     * @param userId 玩家信息
     */
    private static String getRedisKey(String userId)
    {
        return "achievement-user-" + userId;
    }

    /**
     * 获取成就配置信息
     *
     * @return 成就信息
     */
    public static BigData getConfigAchievement(LoginRequest.Platform platform)
    {
        Vector<ConfigAchievement> achievements = FishInfoDao.instance().getCacheListByClass(ConfigAchievement.class);
        Vector<ConfigAchievement> list = new Vector<>(achievements);
        switch (platform)
        {
            case huawei:
            {
                list.removeIf(e -> e.getSpecies().equals("Invite") || e.getSpecies().equals("Ads"));
            }
            break;
            case vivo:
            case oppo:
            case xiaomi:
                list.removeIf(e -> e.getSpecies().equals("Invite"));
                break;
        }
        return BigData.getBigData(list, ConfigAchievement.class);
    }

    /**
     * 获取成就配置信息
     *
     * @return 成就信息
     */
    public static BigData getConfig(LoginRequest.Platform platform)
    {
        Vector<ConfigAchievementType> configs = FishInfoDao.instance().getCacheListByClass(ConfigAchievementType.class);
        Vector<ConfigAchievementType> list = new Vector<>(configs);
        switch (platform)
        {
            case huawei:
            {
                list.removeIf(e -> e.getTypeName().equals("Invite") || e.getTypeName().equals("Ads"));
            }
            break;
            case vivo:
            case oppo:
            case xiaomi:
                list.removeIf(e -> e.getTypeName().equals("Invite"));
                break;
        }
        return BigData.getBigData(list, ConfigAchievementType.class);
    }

    /**
     * 垂钓上报获取成就信息
     *
     * @param angling 垂钓上报
     */
    public void addAchievement(AnglingResultExt.RequestImpl angling)
    {
        //垂钓鱼数量
        int sum = angling.fishes.stream().mapToInt(action -> action.sum).sum();
        addAchievement(FishingNumber, sum);
        //垂钓次数
        addAchievement(FishingFrequency, 1);
        //赶尽杀绝
        if (angling.gamestatus.surplus <= 0)
        {
            addAchievement(UnFish, 1);
        }
        //道具鱼数据
        if (angling.gamestatus.props != null)
            angling.gamestatus.props.forEach((id, count) ->
            {
                ConfigProp prop = (ConfigProp) FishInfoDao.instance().getCacheKey(ConfigProp.class, new String[]{"id", id});
                if (prop != null && prop.getAchievementType() != null)
                {
                    addAchievement(AchievementType.valueOf(prop.getAchievementType()), count);
                }
            });
        //combo次数
        updateCombos(angling.gamestatus.combos);
        //不错连击
        //完美连击
        AchievementType achievementType = angling.gamestatus.operation.getAchievementType();
        if (achievementType != none)
        {
            String value = getAchievementProcess(achievementType);
            int doubleHit = angling.gamestatus.doublehit;
            //当前不存在连击次数，或者记录中保存连击次数小于目前连接数，进行更新数据
            if (value == null || Integer.valueOf(value) < doubleHit)
            {
                updateProcess(achievementType, String.valueOf(doubleHit));
                //更新成就进度
                addFlushNode(achievementType, doubleHit);
            }
        }
    }

    /**
     * 更新combos数据
     *
     * @param combos 连击数据
     */
    private int updateCombos(Map<String, Integer> combos)
    {
        String json = getAchievementProcess(Combo);
        AtomicInteger maxCombo = new AtomicInteger(getMaxCombos(json));
        if (combos != null && !combos.isEmpty())
        {
            Map<Integer, Integer> process = XwhTool.parseJsonMap(json);
            combos.forEach((k, v) ->
            {
                Integer key = Integer.valueOf(k);
                Integer count = process.get(key);
                if (count == null)
                    count = 0;
                if (maxCombo.get() <= key)
                    maxCombo.set(key);
                process.put(key, count + v);
            });
            updateProcess(Combo, XwhTool.getJSONByFastJSON(process));
            addFlushNode(Combo, maxCombo.get());
        }
        return maxCombo.get();
    }

    /**
     * 更新成就进度信息
     *
     * @param type 成就类型
     * @param text 成就数据
     */
    private void updateProcess(AchievementType type, String text)
    {
        String redisKey = getRedisKey(userCache.getUserId());
        RedisUtils.hset(redisKey, type.name(), text);
        updateDatabase(type, text);
    }

    /**
     * 获取成就进度数据
     *
     * @param type 成就类型
     * @return 进度数据
     */
    private String getAchievementProcess(AchievementType type)
    {
        if (receiveCache == null)
        {
            String redisKey = getRedisKey(userCache.getUserId());
            receiveCache = RedisUtils.hgetall(redisKey);
        }
        if (receiveCache != null)
        {
            return receiveCache.get(type.name());
        }
        return null;
    }

    /**
     * 添加成就信息
     *
     * @param type  成就类型
     * @param count 成就次数
     */
    public void addAchievement(AchievementType type, int count)
    {
        //添加成就进度
        switch (type)
        {
            case Ads://广告
            case Book://图鉴
            case Basin://海域
            case Order://订单
            case Invite://邀请
            case UnFish://没鱼次数
            case Friends://好友
            case StoreDeals://商城交易
            case StealBookSuccess://偷取图鉴成功
            case StealShellPerfect://偷取贝壳完美
            case FishingFrequency://捕鱼次数
            case FishingNumber://捕鱼数量
            case FishingPropEel:
            case FishingPropIce:
            case FishingPropBombs:
            case FishingPropShell:
            {
                long process = RedisUtils.hincrby(getRedisKey(userCache.getUserId()), type.name(), count);

                updateDatabase(type, String.valueOf(process));
                addFlushNode(type, process);
            }
            break;
            default:
                break;
        }
    }

    private void addFlushNode(AchievementType type, long process)
    {
        Map<AchievementType, Long> flush = new HashMap<>();
        flush.put(type, process);
        new FlushService(userCache).addFlag(FlushService.FlushType.achievement, flush);
    }

    /**
     * 更新进度进入数据库
     *
     * @param type 进度类型
     * @param text 描述
     */
    private void updateDatabase(AchievementType type, String text)
    {
        try
        {
            if (receiveCache != null)
            {
                receiveCache.put(type.name(), text);
            }
            UserAchievement achievement = new UserAchievement();
            achievement.setAchievementType(type.name());
            achievement.setProcess(text);
            achievement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            achievement.setUserId(userCache.userId());
            FishInfoDao.instance().saveOrUpdate(achievement, true);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 检测是否达标
     *
     * @param achievement 成就
     * @return 达标
     */
    public boolean existStandard(ConfigAchievement achievement)
    {
        achievement.process = 0;
        if (achievement.isStatus())
        {
            int condition = achievement.getCondition();
            AchievementType type = AchievementType.valueOf(achievement.getSpecies());
            String value = getAchievementProcess(type);
            switch (type)
            {
                case Ads://广告
                case Book://图鉴
                case Basin://海域
                case Order://订单
                case Invite://邀请
                case UnFish://没鱼次数
                case StoreDeals://商城交易
                case Friends://好友
                case StealBookSuccess://偷取图鉴成功
                case StealShellPerfect://偷取贝壳完美
                case FishingFrequency://捕鱼次数
                case FishingNumber://捕鱼数量
                case FishingPropEel:
                case FishingPropIce:
                case FishingPropBombs:
                case FishingPropShell:
                case FishingPerfectCombo:
                case FishingGood:
                {
                    int process = 0;
                    if (value != null)
                        process = Integer.valueOf(value);
                    achievement.process = process;
                    if (process >= condition)
                        return true;
                }
                break;
                case Combo:
                {
                    int process = updateCombos(null);
                    return process >= condition;
                }
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * 检测任务是否领取过
     *
     * @param id 成就编号
     * @return 是否领取
     */
    public boolean existReceive(int id)
    {
        String tableName = FishInfoDao.instance().getTableName(UserAchievementGet.class);
        String SQL = String.format("select * from " + tableName + " where userId=%s and achievementId=%d", userCache.getUserId(), id);
        Vector<UserAchievementGet> gets = FishInfoDao.instance().findBySQL(SQL, UserAchievementGet.class);
        return gets != null && !gets.isEmpty();
    }

    /**
     * 获取玩家成就完成获取领取列表
     */
    public Vector<AchievementInfo> getUserAchievement()
    {
        Vector<AchievementInfo> list = new Vector<>();
        Vector<ConfigAchievement> achievements = FishInfoDao.instance().getCacheListByClass(ConfigAchievement.class);
        //获取玩家成就列表
        achievements.forEach(achievement ->
        {
            AchievementInfo achievementInfo = new AchievementInfo();
            achievementInfo.id = achievement.getId();
            achievementInfo.status = "no";
            if (existStandard(achievement))
                achievementInfo.status = "standard";
            achievementInfo.process = achievement.process;
            list.add(achievementInfo);
        });
        //进行设置玩家领取数据
        Vector<UserAchievementGet> userAchievements = FishInfoDao.searchDate(UserAchievementGet.class, userCache.getUserId());
        if (userAchievements != null)
            userAchievements.forEach(user -> list.stream().filter(data -> user.getAchievementId() == data.id).findFirst().ifPresent(get -> get.status = "receive"));
        return list;
    }

    /**
     * 成就领取
     *
     * @param config 成就配置
     * @return 是否领取成功
     */
    public boolean receive(ConfigAchievement config)
    {
        Vector<BasePro.RewardInfo> infos = RewardService.getRewardData(config.getReward());
        if (infos != null && !infos.isEmpty())
        {
            try
            {
                new RewardService(userCache).receiveReward(infos, "achievement");
                UserAchievementGet get = new UserAchievementGet();
                get.setAchievementId(config.getId());
                get.setGetTime(new Timestamp(System.currentTimeMillis()));
                get.setUserId(userCache.userId());
                FishInfoDao.instance().saveOrUpdate(get, false);
                return true;
            } catch (Exception e)
            {
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
        }
        return false;
    }
}
