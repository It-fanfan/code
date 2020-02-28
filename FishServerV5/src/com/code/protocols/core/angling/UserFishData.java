package com.code.protocols.core.angling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.code.protocols.core.AnglingBase.FishResult;

/**
 * 玩家鱼数据节点
 */
public class UserFishData
{
    //鱼数量
    public int sum;
    //鱼总数
    public int total;
    //鱼类型数据
    public Map<Integer, Integer> ftIds = new ConcurrentHashMap<>();

    /**
     * 添加一条鱼信息
     *
     * @param result 鱼信息
     */
    public void addFish(FishResult result)
    {
        int sum = result.sum;
        this.sum += sum;
        this.total += sum;
        Integer value = this.ftIds.get(result.ftid);
        if (value == null)
            value = 0;
        value += sum;
        this.ftIds.put(result.ftid, value);
    }

    /**
     * 获取种类数据
     *
     * @param ftId 鱼类型
     * @return 总数
     */
    public int getTotal(int ftId)
    {
        if (ftIds.containsKey(ftId))
            return ftIds.get(ftId);
        return 0;
    }
}
