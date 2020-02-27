package com.code.dao.entity.record;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_angling")
public class RecordAngling
{

    @Column(name = "dayFlag")
    private int dayFlag;
    @Column(name = "userId")
    private long userId;
    @Column(name = "dayCount")
    private int dayCount;
    @Column(name = "total")
    private int total;
    @Column(name = "fishCount")
    private int fishCount;
    @Column(name = "costShell")
    private long costShell;
    @Column(name = "insert_time")
    private java.sql.Timestamp insertTime;


    public int getDayFlag()
    {
        return dayFlag;
    }

    public void setDayFlag(int dayFlag)
    {
        this.dayFlag = dayFlag;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public int getDayCount()
    {
        return dayCount;
    }

    public void setDayCount(int dayCount)
    {
        this.dayCount = dayCount;
    }


    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }


    public int getFishCount()
    {
        return fishCount;
    }

    public void setFishCount(int fishCount)
    {
        this.fishCount = fishCount;
    }


    public long getCostShell()
    {
        return costShell;
    }

    public void setCostShell(long costShell)
    {
        this.costShell = costShell;
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
