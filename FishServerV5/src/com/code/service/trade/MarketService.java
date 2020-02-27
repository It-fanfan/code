package com.code.service.trade;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.trade.MarketShell;
import com.code.dao.entity.trade.RecordTrade;
import com.utils.XwhTool;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 市场服务
 */
public class MarketService
{
    private static final Logger LOG = LoggerFactory.getLogger(MarketService.class);
    //交易日志
    private static final Logger TRADE_LOG = LoggerFactory.getLogger("tradeLog");

    /**
     * 添加一条交易记录
     *
     * @param userCache 玩家信息
     * @param value     交易数值
     */
    public static void addTradeRecord(UserCache userCache, long value)
    {
        addTradeRecord(userCache, value, WealthType.shell);
    }

    /**
     * 添加一条交易记录
     *
     * @param userCache 玩家信息
     * @param value     货币数
     */
    public static void addTradeRecord(UserCache userCache, long value, WealthType type)
    {
        try
        {
            RecordTrade record = new RecordTrade();
            record.setTrade(value);
            record.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            record.setUserId(userCache.userId());
            record.setWealthType(type.name());
            record.setNewest(type == WealthType.shell ? userCache.shell() : userCache.pearl());
            FishInfoDb.instance().saveOrUpdate(record, true);
            //一式两份，进行保存日志
            TRADE_LOG.info(XwhTool.getJSONByFastJSON(record));
            if (type == WealthType.shell)
                updateMarketTrade(value);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 更新市场交易信息
     *
     * @param shell 贝壳数
     */
    private static void updateMarketTrade(long shell)
    {
        try
        {
            String key = getRedisKey();
            Map<String, String> redis = RedisUtils.hgetall(key);
            MarketShell market = new MarketShell();
            if (redis != null)
            {
                redis.forEach((k, v) ->
                {
                    try
                    {
                        Field field = MarketShell.class.getDeclaredField(k);
                        field.setAccessible(true);
                        field.set(market, Long.valueOf(v));
                    } catch (Exception e)
                    {
                        LOG.error(Log4j.getExceptionInfo(e));
                    }
                });
            }
            market.setDayFlag(XwhTool.getCurrentDateValue());
            market.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            market.setMarket(RedisUtils.hincrby(key, "market", shell));
            if (shell > 0)
            {
                //产出累计
                market.setOutput(RedisUtils.hincrby(key, "output", shell));
            } else
            {
                //消耗累计
                market.setCost(RedisUtils.hincrby(key, "cost", shell));
            }
            market.setTrade(RedisUtils.hincrby(key, "trade", 0));
            FishInfoDb.instance().saveOrUpdate(market, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 交易数据key
     *
     * @return 返回key
     */
    private static String getRedisKey()
    {
        return "market_trade";
    }

    public enum WealthType
    {
        shell,//贝壳数据
        pearl,//珍珠数据
    }
}
