package com.code.service.order;

import com.code.protocols.basic.BasePro;
import com.code.protocols.core.recover.OrderFish;

import java.util.Map;
import java.util.Vector;

//订单信息
public class Order
{
    //bossId
    public int bossId;
    //introduce
    public int introduce;
    //价格
    public int price;
    //訂單魚數據
    public Map<Integer, OrderFish> fishes;
    //上次刷新时间
    public long lastRefresh;
    //其他奖励数据
    public Vector<BasePro.RewardInfo> rewards;
}
