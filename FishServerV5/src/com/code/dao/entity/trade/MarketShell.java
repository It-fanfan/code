package com.code.dao.entity.trade;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "market_shell")
public class MarketShell
{
    @PrimaryKey
    @Column(name = "dayFlag")
    private int dayFlag;
    @Column(name = "market")
    private long market;
    @Column(name = "cost")
    private long cost;
    @Column(name = "output")
    private long output;
    @Column(name = "trade")
    private long trade;
    @Column(name = "updateTime")
    private java.sql.Timestamp updateTime;


    public int getDayFlag()
    {
        return dayFlag;
    }

    public void setDayFlag(int dayFlag)
    {
        this.dayFlag = dayFlag;
    }


    public long getMarket()
    {
        return market;
    }

    public void setMarket(long market)
    {
        this.market = market;
    }


    public long getCost()
    {
        return cost;
    }

    public void setCost(long cost)
    {
        this.cost = cost;
    }


    public long getOutput()
    {
        return output;
    }

    public void setOutput(long output)
    {
        this.output = output;
    }


    public long getTrade()
    {
        return trade;
    }

    public void setTrade(long trade)
    {
        this.trade = trade;
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
