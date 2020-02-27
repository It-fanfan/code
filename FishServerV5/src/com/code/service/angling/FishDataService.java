package com.code.service.angling;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.userinfo.UserFish;
import com.code.dao.entity.trade.MarketFish;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.core.angling.UserFishData;
import com.utils.XwhTool;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static com.code.protocols.core.AnglingBase.FishResult;

/**
 * 鱼的信息
 */
public class FishDataService implements Closeable
{
    private static Logger LOG = LoggerFactory.getLogger(FishDataService.class);
    private UserCache userCache;
    private UserFishData userFishData = null;
    private SQLQueue queue = new SQLQueue();

    //缓存记录当前市场中拥有鱼数据

    public FishDataService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 鱼redis
     */
    private static String getMarketFishRedis()
    {
        return "market_fish";
    }

    /**
     * 更新市场增量
     *
     * @param value 增量值
     * @return 当前累计值
     */
    private static void updateMarketTrade(long value)
    {
        try
        {
            String key = getMarketFishRedis();
            MarketFish market = new MarketFish();
            market.setDayFlag(XwhTool.getCurrentDateValue());
            market.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            market.setMarket(RedisUtils.hincrby(key, "market", value));
            if (value > 0)
            {
                //产出累计
                market.setOutput(RedisUtils.hincrby(key, "output", value));
            } else
            {
                //消耗累计
                market.setInput(RedisUtils.hincrby(key, "input", value));
            }
            FishInfoDb.instance().saveOrUpdate(market, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取鱼配置信息
     *
     * @param ftId 鱼编号
     * @return 鱼种类
     */
    public static ConfigFish getConfigFish(int ftId)
    {
        return (ConfigFish) FishInfoDao.instance().getCacheKey(ConfigFish.class, new String[]{"ftId", String.valueOf(ftId)});
    }

    /**
     * 获取玩家鱼属性
     *
     * @param userCache 玩家信息
     * @return 属性总值
     */
    private static UserFishData getUserFishData(UserCache userCache)
    {
        String userJson = userCache.getValue(getUserFishRedis());
        UserFishData userFishData = null;
        if (userJson != null)
        {
            userFishData = XwhTool.parseJSONByFastJSON(userJson, UserFishData.class);
        }
        if (userFishData == null)
        {
            userFishData = new UserFishData();
            userFishData.ftIds = new HashMap<>();
        }
        return userFishData;
    }

    /**
     * 记录玩家鱼数据参数
     */
    private static String getUserFishRedis()
    {
        return "fish-data";
    }

    /**
     * 通过已知对象{ftid,color,skin}队列来生成鱼数据
     *
     * @param results {ftId,color,skin}
     * @return 鱼对象
     */
    int getFishData(Vector<FishResult> results)
    {
        if (results == null || results.isEmpty())
        {
            return 0;
        }
        UserFishData userFishData = getUserFishData();
        AtomicInteger atomic = new AtomicInteger();
        results.forEach(result ->
        {
            ConfigFish config = getConfigFish(result.ftid);
            if (config == null)
                return;
            int allowLimit = config.getAllowLimit();
            int sub = userFishData.getTotal(result.ftid) + result.sum - allowLimit;
            if (sub > 0)
            {
                result.sum -= sub;
            }
            if (result.sum > 0)
            {
                atomic.addAndGet(result.sum);
                userFishData.addFish(result);
            }
        });
        //更新市场数据
        updateMarketTrade(atomic.longValue());
        putUserFishData(userFishData);
        return atomic.intValue();
    }

    /**
     * 获取玩家鱼数据
     *
     * @return data
     */
    public Vector<FishResult> getUserFishes()
    {
        Vector<FishResult> _temp = new Vector<>();
        getUserFishData().ftIds.forEach((k, v) -> _temp.add(new FishResult(k, v)));
        return _temp;
    }

    /**
     * 移除鱼数据
     *
     * @param results 移除鱼数据
     */
    public void removeFishData(Vector<FishResult> results)
    {
        UserFishData userFishData = getUserFishData();
        AtomicInteger atomic = new AtomicInteger();
        results.forEach(result ->
        {
            int count = userFishData.getTotal(result.ftid);
            if (count >= result.sum)
            {
                //更新数值
                userFishData.ftIds.put(result.ftid, count - result.sum);
                userFishData.sum -= result.sum;
                atomic.addAndGet(result.sum);
            }
        });
        //更新市场数据
        updateMarketTrade(-atomic.longValue());
        putUserFishData(userFishData);
    }

    /**
     * 进行更新玩家鱼数据信息
     */
    private void putUserFishData(UserFishData userFishData)
    {
        String json = XwhTool.getJSONByFastJSON(userFishData);
        userCache.hSet(getUserFishRedis(), json);
        synDatabase(userCache, userFishData);
    }

    /**
     * 获取玩家鱼汇总数据
     *
     * @return 鱼汇总值
     */
    private UserFishData getUserFishData()
    {
        if (userFishData != null)
            return userFishData;
        return userFishData = getUserFishData(userCache);
    }

    /**
     * 获取所有鱼信息
     *
     * @return 鱼数量
     */
    public long getSum()
    {
        UserFishData userFishData = getUserFishData();
        return userFishData.sum;
    }

    /**
     * 同步数据库处理
     *
     * @param fishData 鱼数据信息
     */
    private void synDatabase(UserCache userCache, UserFishData fishData)
    {
        try
        {
            long userId = userCache.userId();
            int total = fishData.total, sum = fishData.sum;
            UserFish userFish = new UserFish(userId, total, sum, XwhTool.getJSONByFastJSON(fishData));
            userFish.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (queue.userFishes == null)
                queue.userFishes = new Vector<>();
            queue.userFishes.add(userFish);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 进行对象销毁，进行处理鱼数据相关SQL
     */
    public void close()
    {
        try
        {
            if (queue == null)
                return;
            if (queue.userFishes != null && !queue.userFishes.isEmpty())
            {
                FishInfoDb.instance().saveOrUpdate(queue.userFishes, true);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            queue = new SQLQueue();
        }
    }

    protected void finalize()
    {
        System.out.println("執行結束!");
        close();
    }

    /**
     * SQL队列数据
     */
    private static class SQLQueue
    {
        //保存队列
        private Vector<UserFish> userFishes;
    }
}
