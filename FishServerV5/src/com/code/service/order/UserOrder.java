package com.code.service.order;

import java.util.Vector;

/**
 * 玩家訂單數據
 */
public class UserOrder
{
    //記錄水域信息
    public int basinId;
    //上次接單时间
    public long lastFinish;
    //订单数据
    public Vector<Order> orders;
    //抽取卡包
    public Vector<Integer> packet;
}
