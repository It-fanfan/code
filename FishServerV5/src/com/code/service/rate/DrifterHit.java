package com.code.service.rate;

import com.code.cache.UserCache;
import com.code.protocols.core.angling.DrifterConfig;
import com.code.service.reward.AllotPool;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 漂流瓶命中
 */
public class DrifterHit
{
    private static final Logger LOG = LoggerFactory.getLogger(TurnTableHit.class);
    private AllotPool pool;
    private UserCache userCache;
    private Map<String, DrifterConfig.DrifterReward> rewardCache;

    public DrifterHit(UserCache userCache)
    {
        this.userCache = userCache;
        String json = userCache.getValue(getName());
        pool = new AllotPool(json);

    }

    public void initConfig(Vector<DrifterConfig.DrifterReward> rewards)
    {
        rewardCache = new HashMap<>();
        Map<String, Integer> config = new HashMap<>();
        rewards.forEach(reward ->
        {
            config.put(reward.type.name(), reward.rate);
            rewardCache.put(reward.type.name(), reward);
        });
        pool.initConfig(config);
    }


    public String getName()
    {
        return "drifter-rate";
    }

    /**
     * 进行命中参数
     */
    public DrifterConfig.DrifterReward hit()
    {
        try
        {
            String hit = pool.pool();
            if (hit != null && rewardCache != null)
            {
                return rewardCache.get(hit);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            String json = XwhTool.getJSONByFastJSON(pool.getSurplus());
            userCache.hSet(getName(), json);
        }
        return null;
    }

}
