package com.code.service.rate;

import com.code.cache.UserCache;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.protocols.core.AnglingBase;
import com.code.service.reward.AllotPool;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AnglingRewardHit
{

    private static final Logger LOG = LoggerFactory.getLogger(AnglingRewardHit.class);
    private AllotPool pool;
    private UserCache userCache;

    public AnglingRewardHit(UserCache userCache)
    {
        this.userCache = userCache;
        String json = userCache.getValue(getName());
        pool = new AllotPool(json);
        pool.initConfig(getConfig());
    }


    public String getName()
    {
        return "angling_reward_rate";
    }

    /**
     * 设置配置参数数据
     */
    public Map<String, Integer> getConfig()
    {
        String json = Systemstatusinfo.getText(getName());
        Vector<RewardRate> rates = new Vector<>();
        if (json != null)
        {
            rates = XwhTool.parseJSONByFastJSON(json, new TypeToken<Vector<RewardRate>>()
            {
            }.getType());
        } else
        {
            AnglingBase.AnglingExtraType[] rewardTypes = AnglingBase.AnglingExtraType.values();
            for (AnglingBase.AnglingExtraType type : rewardTypes)
            {
                rates.add(new RewardRate(type, (int) type.getValue()));
            }
        }
        Map<String, Integer> hash = new HashMap<>();
        rates.forEach(element -> hash.put(element.type.name(), element.rate));
        return hash;
    }

    /**
     * 进行命中参数
     */
    public AnglingBase.AnglingExtraType hit()
    {
        try
        {
            String hit = pool.pool();
            if (hit != null)
            {
                return AnglingBase.AnglingExtraType.valueOf(hit);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            String json = XwhTool.getJSONByFastJSON(pool.getSurplus());
            userCache.hSet(getName(), json);
        }
        return AnglingBase.AnglingExtraType.none;
    }

    public static class RewardRate extends Rate
    {
        public AnglingBase.AnglingExtraType type;

        public RewardRate()
        {
            super();
        }

        private RewardRate(AnglingBase.AnglingExtraType type, int rate)
        {
            this.type = type;
            this.rate = rate;
        }
    }

}
