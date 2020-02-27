package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_order_ext")
public class UserOrderExt
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "lastFinish")
    private long lastFinish;
    @Column(name = "lastRefresh")
    private long lastRefresh;
    @Column(name = "orderInfo")
    private String orderInfo;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public long getLastFinish()
    {
        return lastFinish;
    }

    public void setLastFinish(long lastFinish)
    {
        this.lastFinish = lastFinish;
    }


    public long getLastRefresh()
    {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh)
    {
        this.lastRefresh = lastRefresh;
    }


    public String getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo)
    {
        this.orderInfo = orderInfo;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

}
