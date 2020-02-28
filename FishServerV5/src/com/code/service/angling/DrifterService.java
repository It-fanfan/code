package com.code.service.angling;

import com.code.cache.UserCache;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.core.angling.DrifterConfig;
import com.code.protocols.login.LoginRequest;
import com.code.service.goods.ShopService;
import com.code.service.rate.DrifterHit;
import com.code.service.reward.RewardService;
import com.code.service.work.WorkService;
import com.utils.XwhTool;

import java.util.Vector;

public class DrifterService
{

    private UserCache userCache;
    private LoginRequest.Platform platform;

    public DrifterService(UserCache userCache, LoginRequest.Platform platform)
    {
        this.userCache = userCache;
        this.platform = platform;
    }

    private static String getRedis()
    {
        return "drifter-data";
    }

    /**
     * 进行创建漂流瓶
     *
     * @param workService 日常服务
     */
    public AnglingBase.DrifterData createDrifter(WorkService workService)
    {
        DrifterConfig config = DrifterConfig.getInstance(platform);
        if (config == null || config.triggers == null)
            return null;
        DrifterConfig.Trigger trigger = null;
        int index = 0;
        for (DrifterConfig.Trigger temp : config.triggers)
        {
            index++;
            Integer process = workService.getProcess(temp.activitytype);
            if (process != null && process == temp.activitynum)
            {
                trigger = temp;
                break;
            }
        }
        if (trigger == null)
            return null;
        AnglingBase.DrifterData drifterData = new AnglingBase.DrifterData();
        drifterData.index = index;
        drifterData.trigger = trigger;
        DrifterHit hit = new DrifterHit(userCache);
        hit.initConfig(config.reward);
        drifterData.reward = hit.hit();
        UserDrifter userDrifter = getUserDrifter();
        userDrifter.drifterData.add(drifterData);
        saveUserDrifter(userDrifter);
        return drifterData;
    }

    /**
     * 获取漂流瓶信息
     *
     * @return 用户漂流瓶
     */
    private UserDrifter getUserDrifter()
    {
        String json = userCache.getValue(getRedis());
        UserDrifter drifter = XwhTool.parseJSONByFastJSON(json, UserDrifter.class);
        if (drifter == null)
            drifter = new UserDrifter();
        int dayFlag = XwhTool.getCurrentDateValue();
        if (drifter.dayFlag != dayFlag)
        {
            drifter.dayFlag = dayFlag;
            drifter.drifterData = new Vector<>();
        }
        return drifter;
    }

    /**
     * 进行保存玩家漂流瓶
     *
     * @param drifter 用户漂流瓶信息
     */
    private void saveUserDrifter(UserDrifter drifter)
    {
        userCache.hSet(getRedis(), XwhTool.getJSONByFastJSON(drifter));
    }

    /**
     * 领取漂流瓶奖励
     *
     * @param index 索引信息
     * @return 结果
     */
    public AnglingBase.ERROR receive(int index)
    {
        UserDrifter userDrifter = getUserDrifter();
        AnglingBase.DrifterData data = userDrifter.drifterData.stream().filter(element -> element.index == index).findFirst().orElse(null);
        if (data == null)
            return AnglingBase.ERROR.UNSATISFIED;
        ShopService shopService = new ShopService(userCache);
        if (shopService.costGoods(data.trigger.costnum, data.trigger.costtype))
        {
            return AnglingBase.ERROR.NO_ALLOW;
        }
        userDrifter.drifterData.removeElement(data);
        saveUserDrifter(userDrifter);
        new RewardService(userCache).receiveReward(data.reward, "drifter");
        return AnglingBase.ERROR.SUCCESS;
    }

    public static class UserDrifter
    {
        public int dayFlag;
        public Vector<AnglingBase.DrifterData> drifterData;
    }
}
