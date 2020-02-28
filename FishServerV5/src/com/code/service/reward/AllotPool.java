package com.code.service.reward;

import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

/**
 * 分配池子数据
 */
public class AllotPool
{

    /**
     * 池子数据
     */
    private Queue<String> rewardPool = new ConcurrentLinkedQueue<>();
    //配置信息
    private Map<String, Integer> config = new HashMap<>();

    /**
     * 分配池子数据
     *
     * @param json 剩余池子量
     */
    public AllotPool(String json)
    {
        if (json != null)
        {
            Vector<String> pool = XwhTool.parseJSONByFastJSON(json, new TypeToken<Vector<String>>()
            {
            }.getType());
            if (pool != null)
                rewardPool = new ConcurrentLinkedQueue<>(pool);
        }
    }

    /**
     * 奖池分配数据
     *
     * @param config 配置信息
     */
    public void initConfig(Map<String, Integer> config)
    {
        this.config = config;
    }

    /**
     * 进行抽取池子数据
     *
     * @return 命中结果
     */
    public String pool()
    {
        String hit = rewardPool.poll();
        if (hit == null)
        {
            initRewardPool();
            return pool();
        }
        return hit;
    }

    /**
     * 获取剩余数据
     */
    public Vector<String> getSurplus()
    {
        return new Vector<>(rewardPool);
    }

    /**
     * 进行初始构造池子数据
     */
    private void initRewardPool()
    {
        //池子为配置中总和
        Vector<String> pools = new Vector<>();
        config.forEach((k, v) -> IntStream.range(0, v).mapToObj(i -> k).forEach(pools::add));
        Collections.shuffle(pools);
        rewardPool = new ConcurrentLinkedQueue<>(pools);
    }
}
