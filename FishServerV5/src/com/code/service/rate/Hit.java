package com.code.service.rate;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 进行命中
 */
public interface Hit
{
    Map<String, Hit> cacheMap = new ConcurrentHashMap<>();

    /**
     * 重置数据参数
     *
     * @param key 计数值
     */
    static void resetData(String key)
    {
        if (cacheMap.containsKey(key))
        {
            cacheMap.get(key).resetData();
        }
    }

    default Hit getInstance()
    {
        String key = getName();
        if (cacheMap.containsKey(key))
            return cacheMap.get(key);
        cacheMap.put(key, this);
        return this;
    }

    Vector<Rate> rates();

    /**
     * 获取概率名称
     *
     * @return
     */
    String getName();

    /**
     * 设置概率数值
     */
    default Vector<Rate> getRates()
    {
        Vector<Rate> rates = rates();
        if (rates.isEmpty())
        {
            rates.addAll(resetRates());
        }
        return rates;
    }

    /**
     * 重置概率参数
     */
    default void resetData()
    {
        rates().clear();
    }

    /**
     * 重置概率集合
     *
     * @return
     */
    Vector<Rate> resetRates();

    /**
     * 获取概率总值
     *
     * @return 返回结果集
     */
    default long getTotal(Vector<Rate> rates)
    {
        long total = rates.stream().mapToLong(Rate::getRate).sum();
        if (total <= 0)
        {
            total = 100;
        }
        return total;
    }


    /**
     * 进行命中
     *
     * @return
     */
    default Rate hit()
    {
        Vector<Rate> rates = getRates();
        if (rates == null)
            return null;
        long total = getTotal(rates);
        long random = ThreadLocalRandom.current().nextLong(total);
        long index = 0;
        for (Rate rate : rates)
        {
            if (random >= index && random <= (rate.getRate() + index))
            {
                return rate;
            } else
            {
                index += rate.getRate();
            }
        }
        return null;
    }
}
