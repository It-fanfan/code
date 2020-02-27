package com.code.service.rate;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.work.WorkTurntable;
import com.code.service.reward.AllotPool;
import com.code.service.work.WorkService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TurnTableHit
{
    private static final Logger LOG = LoggerFactory.getLogger(TurnTableHit.class);
    private AllotPool pool;
    private UserCache userCache;

    public TurnTableHit(UserCache userCache)
    {
        this.userCache = userCache;
        String json = userCache.getValue(getName());
        pool = new AllotPool(json);
        pool.initConfig(getConfig());
    }


    public String getName()
    {
        return "turntable-rate";
    }

    /**
     * 设置配置参数数据
     */
    public Map<String, Integer> getConfig()
    {
        //转盘配置信息
        Vector<WorkTurntable> config = WorkService.getWorkTurntablesCache();
        Map<String, Integer> hash = new HashMap<>();
        config.forEach(element -> hash.put(String.valueOf(element.getId()), element.getRate()));
        LOG.error("转盘配置数据:" + hash);
        return hash;
    }

    /**
     * 进行命中参数
     */
    public WorkTurntable hit()
    {
        try
        {
            String hit = pool.pool();
            if (hit != null)
            {
                return (WorkTurntable) FishInfoDb.instance().getCacheKey(WorkTurntable.class, new String[]{"id", hit});
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
