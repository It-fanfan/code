package com.code.dao.entity.trade;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "market_fish")
public class MarketFish
{
    @PrimaryKey
    @Column(name = "dayFlag")
    private int dayFlag;
    @Column(name = "market")
    private long market;
    @Column(name = "output")
    private long output;
    @Column(name = "input")
    private long input;
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


    public long getOutput()
    {
        return output;
    }

    public void setOutput(long output)
    {
        this.output = output;
    }


    public long getInput()
    {
        return input;
    }

    public void setInput(long input)
    {
        this.input = input;
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
