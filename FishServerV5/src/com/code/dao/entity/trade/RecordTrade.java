package com.code.dao.entity.trade;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_trade")
public class RecordTrade
{

    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "wealthType")
    private String wealthType;
    @Column(name = "trade")
    private long trade;
    @Column(name = "newest")
    private long newest;
    @Column(name = "updateTime")
    private java.sql.Timestamp updateTime;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getWealthType()
    {
        return wealthType;
    }

    public void setWealthType(String wealthType)
    {
        this.wealthType = wealthType;
    }


    public long getTrade()
    {
        return trade;
    }

    public void setTrade(long trade)
    {
        this.trade = trade;
    }


    public long getNewest()
    {
        return newest;
    }

    public void setNewest(long newest)
    {
        this.newest = newest;
    }


    public java.sql.Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(java.sql.Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

}
