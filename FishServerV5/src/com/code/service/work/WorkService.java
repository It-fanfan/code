package com.code.service.work;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.fish.userinfo.UserActivityWork;
import com.code.dao.entity.work.WorkActivity;
import com.code.dao.entity.work.WorkSign;
import com.code.dao.entity.work.WorkTurntable;
import com.code.dao.entity.work.WorkWeekActivity;
import com.code.protocols.basic.BasePro;
import com.code.protocols.goods.CostType;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.work.TurntableProto;
import com.code.service.rate.TurnTableHit;
import com.code.service.reward.RewardService;
import com.code.service.ui.FlushService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Timestamp;
import java.util.*;

import static com.code.protocols.operator.work.TurntableListProto.*;
import static com.code.protocols.operator.work.TurntableListProto.TurntableType.ad;
import static com.code.protocols.operator.work.TurntableListProto.TurntableType.free;

public class WorkService implements Closeable
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkService.class);

    private UserCache userCache;
    private LoginRequest.Platform platform;

    public WorkService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    public WorkService(UserCache userCache, LoginRequest.Platform platform)
    {
        this.userCache = userCache;
        this.platform = platform;
    }

    private ActivityWork work = null;

    /**
     * 获取签到奖励
     *
     * @param dayTotal 连胜纪录
     * @return 奖励信息
     */
    private static Vector<BasePro.RewardInfo> getSignReward(int dayTotal)
    {
        Vector<WorkSign> signs = getWorkSign();
        WorkSign sign = signs.stream().filter(workSign ->
        {
            if (workSign.getDayFlag() == dayTotal)
                return true;
            return dayTotal > workSign.getDayFlag() && workSign.getLimitFlag() == -1;
        }).findFirst().orElse(null);
        if (sign != null)
        {
            return sign.getSignReward(dayTotal);
        }
        return null;
    }

    /**
     * 检测任务是否开启
     *
     * @param now        匹配日期
     * @param activities 元素列表
     * @return 是否开启
     */
    private static <T> Vector<T> getWorkConfig(Date now, Vector<T> activities)
    {
        for (int i = activities.size() - 1; i >= 0; i--)
        {
            T t = activities.elementAt(i);
            if (t instanceof WorkConfig)
            {
                WorkConfig element = (WorkConfig) t;
                if (!element.getState() || element.getStartDate().after(now) || element.getEndDate().before(now))
                    activities.removeElementAt(i);
            }
        }
        return activities;
    }

    /**
     * 获取签到任务信息
     *
     * @return 签到列表
     */
    public static Vector<WorkSign> getWorkSign()
    {
        Vector<WorkSign> activities = FishInfoDb.instance().getCacheListByClass(WorkSign.class);
        Date now = new Date();
        return getWorkConfig(now, activities);
    }

    /**
     * 活跃任务
     *
     * @return redis
     */
    private static String getActivityWorkRedis()
    {
        return "work-activity";
    }

    public static Vector<WorkTurntable> getWorkTurntablesCache()
    {
        return FishInfoDb.instance().getCacheListByClass(WorkTurntable.class);
    }

    /**
     * 活跃任务
     * signup
     *
     * @return 任务
     */
    public ActivityWork getWork()
    {
        if (work == null)
        {
            work = getUserActivityWork(getActivityWorkRedis());
        }
        return work;
    }

    /**
     * 添加任务进度
     *
     * @param activityType 类型
     * @param process      进度
     */
    public void addProcess(OperatorBase.ActivityType activityType, int process)
    {
        Map<OperatorBase.ActivityType, Integer> map = new HashMap<>();
        map.put(activityType, process);
        addProcess(map);
    }

    /**
     * 添加任务进度
     *
     * @param process 进度数据
     */
    public void addProcess(Map<OperatorBase.ActivityType, Integer> process)
    {
        int dayFlag = XwhTool.getCurrentDateValue();
        ActivityWork.DayActivity dayActivity = getDayActivity(dayFlag);
        process.forEach((k, v) ->
        {
            Integer history = dayActivity.activityMap.get(k);
            if (history == null)
                history = 0;
            dayActivity.activityMap.put(k, history + v);
        });
    }

    /**
     * 获取进度信息
     *
     * @param activityType 日常任务数据
     * @return 进度
     */
    public Integer getProcess(OperatorBase.ActivityType activityType)
    {
        int dayFlag = XwhTool.getCurrentDateValue();
        ActivityWork.DayActivity dayActivity = getDayActivity(dayFlag);
        return dayActivity.activityMap.get(activityType);
    }

    /**
     * 领取任务
     *
     * @param workId 任务编号
     * @return 是否成功
     */
    public boolean receiveWork(int workId)
    {
        ActivityWork work = getWork();
        int dayFlag = XwhTool.getCurrentDateValue();
        ActivityWork.DayActivity dayActivity = getDayActivity(dayFlag);
        if (dayActivity.workList.contains(workId))
        {
            //已领取
            return false;
        }
        Vector<WorkActivity> activities = getWorkActivity();
        for (WorkActivity element : activities)
        {
            if (element.getWorkId() == workId)
            {
                Integer history = dayActivity.activityMap.get(OperatorBase.ActivityType.valueOf(element.getWorkType()));
                if (history == null || history < element.getWorkCount())
                    return false;
                work.activityValue += element.getActivityValue();
                dayActivity.workList.add(workId);
                RewardService rewardService = new RewardService(userCache);
                Vector<BasePro.RewardInfo> rewards = RewardService.getRewardData(element.getReward());
                rewardService.receiveReward(rewards, "activity-work-" + element.getWorkId());
                return true;
            }
        }
        //无该奖励
        return false;
    }

    /**
     * 領取周宝箱
     *
     * @param caseId 宝箱ID
     * @return 领取是否成功
     */
    public boolean receiveWeekActivity(int caseId)
    {
        ActivityWork work = getWork();
        if (work.caseIds.contains(caseId))
            //宝箱已经领取
            return false;
        Vector<WorkWeekActivity> activities = getWorkWeekActivity();
        for (WorkWeekActivity element : activities)
        {
            if (element.getId() == caseId)
            {
                if (work.activityValue < element.getActivityValue())
                    return false;
                work.caseIds.add(element.getId());
                RewardService rewardService = new RewardService(userCache);
                Vector<BasePro.RewardInfo> rewardInfos = RewardService.getRewardData(element.getReward());
                rewardService.receiveReward(rewardInfos, "activity-case-" + element.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * 签到 - 连签
     */
    public boolean signup()
    {
        ActivityWork work = getWork();
        int dayFlag = XwhTool.getCurrentDateValue();
        if (work.signup.contains(dayFlag))
            return false;
        work.signup.add(dayFlag);
        //进行清除
        //获取连签数据
        int dayTotal = work.signup.size();
        Vector<BasePro.RewardInfo> rewards = getSignReward(dayTotal);
        if (rewards != null)
        {
            RewardService rewardService = new RewardService(userCache);
            rewardService.receiveReward(rewards, "activity-sign-" + dayTotal);
        }
        return true;
    }

    @Override
    public void close()
    {
        try
        {
            String field = getActivityWorkRedis();
            ActivityWork work = getWork();
            String json = XwhTool.getJSONByFastJSON(work);
            userCache.hSet(field, json);
            UserActivityWork data = new UserActivityWork();
            data.setActivityJson(json);
            data.setActivityValue(work.activityValue);
            data.setDayFlag(XwhTool.getCurrentDateValue());
            data.setUserId(userCache.userId());
            data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            FishInfoDb.instance().saveOrUpdate(data, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取全部任务信息
     *
     * @return 任务信息
     */
    public Vector<WorkActivity> getWorkActivity()
    {
        Vector<WorkActivity> activities = FishInfoDb.instance().getCacheListByClass(WorkActivity.class);
        Vector<WorkActivity> list = new Vector<>(activities);
        switch (platform)
        {
            case huawei:
            {
                list.removeIf(e -> e.getWorkType().equals("share") || e.getWorkType().equals("video") || e.getWorkType().equals("invite"));
            }
            break;
            case vivo:
            case oppo:
            case xiaomi:
                list.removeIf(e -> e.getWorkType().equals("share") || e.getWorkType().equals("invite"));
                break;
        }
        Date now = new Date();
        return getWorkConfig(now, list);
    }

    /**
     * 获取周活躍獎勵信息
     *
     * @return 宝箱列表
     */
    public Vector<WorkWeekActivity> getWorkWeekActivity()
    {
        Vector<WorkWeekActivity> activities = FishInfoDb.instance().getCacheListByClass(WorkWeekActivity.class);
        Date now = new Date();
        return getWorkConfig(now, activities);
    }

    /**
     * 获取当天奖励信息
     *
     * @param dayFlag 时间戳
     * @return 当天奖励信息
     */
    public ActivityWork.DayActivity getDayActivity(int dayFlag)
    {
        Vector<ActivityWork.DayActivity> activities = getWork().activities;
        ActivityWork.DayActivity dayActivity = null;
        for (ActivityWork.DayActivity temp : activities)
        {
            if (temp.dayFlag == dayFlag)
            {
                dayActivity = temp;
                break;
            }
        }
        //进行重置数据
        if (dayActivity == null)
        {
            dayActivity = new ActivityWork.DayActivity();
            dayActivity.dayFlag = dayFlag;
            activities.add(dayActivity);
        }
        if (dayActivity.turntable == null)
            dayActivity.turntable = new HashMap<>();
        if (dayActivity.activityMap == null)
            dayActivity.activityMap = new HashMap<>();
        if (dayActivity.workList == null)
            dayActivity.workList = new Vector<>();
        return dayActivity;
    }

    /**
     * 获取玩家奖励信息
     *
     * @return 奖励任务
     */
    public ActivityWork getUserActivityWork()
    {
        String field = getActivityWorkRedis();
        return getUserActivityWork(field);
    }

    /**
     * 獲取活躍信息
     *
     * @return 当前任务进度
     */
    private ActivityWork getUserActivityWork(String field)
    {
        String json = userCache.getValue(field);
        ActivityWork activity = null;
        if (json != null)
        {
            activity = XwhTool.parseJSONByFastJSON(json, ActivityWork.class);
        }
        Calendar calendar = Calendar.getInstance();
        int weekFlag = calendar.get(Calendar.WEEK_OF_YEAR);
        if (activity == null || activity.weekFlag != weekFlag)
        {
            if (activity == null)
                activity = new ActivityWork();
            resetActivityWork(activity, weekFlag);
        }
        //进行重置签到数据
        if (!activity.signup.isEmpty())
        {
            int dayFlag = XwhTool.getCurrentDateValue();
            //当天已经签到
            if (activity.signup.contains(dayFlag))
                return activity;
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            int lastFlag = XwhTool.getDateValue(calendar.getTime());
            //检测昨天未签到，则重置
            if (!activity.signup.contains(lastFlag))
            {
                activity.signup.clear();
            }
        }
        return activity;
    }

    /**
     * 进行重置
     *
     * @param work     任务属性
     * @param weekFlag 周标签
     */
    private void resetActivityWork(ActivityWork work, int weekFlag)
    {
        work.activities = new Vector<>();
        work.activityValue = 0;
        work.weekFlag = weekFlag;
        work.caseIds = new Vector<>();
    }

    /**
     * 进行UI检测
     *
     * @return sky
     */
    public boolean uiFlag()
    {
        String field = getActivityWorkRedis();
        work = getUserActivityWork(field);
        if (work != null)
        {
            int dayFlag = XwhTool.getCurrentDateValue();
            if (!work.signup.contains(dayFlag))
                return true;
            int activity = work.activityValue;
            //宝箱是否满足
            Vector<WorkWeekActivity> weekActivities = getWorkWeekActivity();
            weekActivities.sort(Comparator.comparingInt(WorkWeekActivity::getActivityValue));
            for (WorkWeekActivity week : weekActivities)
            {
                //满足活跃条件值，并未领取宝箱
                if (week.getActivityValue() <= activity && !work.caseIds.contains(week.getId()))
                {
                    return true;
                } else if (week.getActivityValue() > activity)
                    break;
            }
            ActivityWork.DayActivity dayActivity = getDayActivity(dayFlag);
            if (dayActivity != null)
                return getActivityWorkProcess(dayActivity, null);
        }
        return false;
    }

    /**
     * 获取任务进度
     *
     * @return 进度是否ok
     */
    public boolean getActivityWorkProcess(ActivityWork.DayActivity element, Map<String, Integer> workProcess)
    {
        boolean flag = false;
        //判断进度任务
        for (WorkActivity activity : getWorkActivity())
        {
            int process = 0;
            OperatorBase.ActivityType type = OperatorBase.ActivityType.valueOf(activity.getWorkType());
            if (element.activityMap.containsKey(type))
            {
                process = element.activityMap.get(type);
                if (process >= activity.getWorkCount() && !element.workList.contains(activity.getWorkId()))
                    flag = true;
            }
            if (workProcess != null)
                workProcess.put(String.valueOf(activity.getWorkId()), process);
        }
        return flag;
    }

    /**
     * 获取玩家转盘使用情况
     */
    private Map<Integer, Integer> getTurntableCount()
    {
        int dayFlag = XwhTool.getCurrentDateValue();
        ActivityWork.DayActivity dayActivity = getDayActivity(dayFlag);
        return dayActivity.turntable;
    }

    /**
     * 消耗1次转盘
     *
     * @param type (0：免费  1：视频广告)
     */
    private int costTurntable(TurntableType type)
    {
        int dayFlag = XwhTool.getCurrentDateValue();
        ActivityWork.DayActivity activity = getDayActivity(dayFlag);
        Integer value = activity.turntable.get(type.getVal());
        if (value == null)
            value = 1;
        else
            value++;
        activity.turntable.put(type.getVal(), value);
        return value;
    }

    /**
     * 开始转盘
     *
     * @param val 消耗类型数值
     */
    public OperatorBase.ERROR playTurntable(int val, TurntableProto.ResponseImpl response)
    {
        try
        {
            TurntableType type = TurntableType.getType(val);
            BasePro.UserTurntable userTurntable = getUserTurntable();
            switch (type)
            {
                case free: //免费
                {
                    if (userTurntable.freecount <= 0)
                        return OperatorBase.ERROR.TURNTABLE_UN_COUNT;
                    response.freecount = costTurntable(type);
                }
                break;
                case ad://视频广告
                {
                    // 校验次数消耗
                    if (userTurntable.adcount <= 0)
                    {
                        return OperatorBase.ERROR.TURNTABLE_UN_COUNT;
                    }
                    response.adcount = costTurntable(type);
                }
                break;
                case cost:
                {
                    if (!turntableUnderConsumption())
                    {
                        return OperatorBase.ERROR.COIN_UNENOUGH;
                    }
                    costTurntable(type);
                }
                break;
                default:
                    break;
            }

            // 随机转盘
            WorkTurntable turntable = randomTurntable();
            if (turntable != null)
            {
                // 领取奖励
                RewardService rewardService = new RewardService(userCache);
                rewardService.receiveReward(new BasePro.RewardInfo(turntable.getType(), turntable.getProductId(), turntable.getNum()), "turntable");
                response.rewardid = turntable.getId();
                return OperatorBase.ERROR.SUCCESS;
            }
            return OperatorBase.ERROR.TURNTABLE_IS_NULL;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
            return OperatorBase.ERROR.ERROR;
        }
    }

    /**
     * 转盘不足消耗
     */
    private boolean turntableUnderConsumption()
    {
        boolean result = false;
        try
        {
            TurntableConfig config = getTurntableConfig();
            if (config == null)
                return false;
            CostType costType = config.condition.costtype;
            int num = config.condition.num;
            switch (costType)
            {
                case pearl:
                    return result = !userCache.incrPearl(-num);
                case shell:
                    return result = userCache.incrShell(-num);
                default:
                    return false;
            }
        } finally
        {
            if (!result)
                new FlushService(userCache).addFlag(FlushService.FlushType.user);
        }
    }

    /**
     * 随机转盘
     */
    private WorkTurntable randomTurntable()
    {
        try
        {
            TurnTableHit rate = new TurnTableHit(userCache);
            return rate.hit();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 获取转盘数据
     */
    public ResponseImpl getTurntableData()
    {
        ResponseImpl response = new ResponseImpl();
        OperatorBase.updateResponseCode(OperatorBase.ERROR.SUCCESS, response);
        response.status = true;

        try
        {
            Vector<Turntable> turntables = new Vector<>();

            // 转盘数据
            Vector<WorkTurntable> workTurntablesCache = getWorkTurntablesCache();
            for (WorkTurntable item : workTurntablesCache)
            {
                if (!item.isState())
                    continue;

                Turntable turntable = new Turntable();
                turntable.icon = item.getIcon();
                turntable.id = item.getId();
                turntable.name = item.getName();
                turntable.num = item.getNum();
                turntables.add(turntable);
            }
            TurntableConfig config = getTurntableConfig();
            if (config != null)
                response.condition = config.condition;
            response.turntables = turntables;
        } catch (Exception e)
        {
            OperatorBase.updateResponseCode(OperatorBase.ERROR.ERROR, response);
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return response;
    }

    /**
     * 用户的转盘信息
     *
     * @return 玩家转盘数据
     */
    public BasePro.UserTurntable getUserTurntable()
    {
        BasePro.UserTurntable instance = new BasePro.UserTurntable();
        // 拥有转盘次数
        TurntableConfig config = getTurntableConfig();
        if (config == null)
            return instance;
        instance.adcount = config.ad;
        instance.freecount = config.free;
        //用户当天转盘使用次数
        Map<Integer, Integer> cache = getTurntableCount();
        if (cache.containsKey(ad.getVal()))
        {
            instance.adcount -= cache.get(ad.getVal());
        }
        if (cache.containsKey(free.getVal()))
        {
            instance.freecount -= cache.get(free.getVal());
        }
        return instance;
    }

    /**
     * 获取大转盘配置信息
     *
     * @return config
     */
    private static TurntableConfig getTurntableConfig()
    {
        // 转盘条件
        String text = Systemstatusinfo.getText("turntable_config");
        return XwhTool.parseJSONByFastJSON(text, TurntableConfig.class);
    }
}
