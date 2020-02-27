package com.code.dao.entity.work;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.code.service.work.WorkConfig;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "work_activity")
public class WorkActivity implements WorkConfig
{
    @PrimaryKey
    @Column(name = "workId")
    private int workId;
    @ReadOnly
    @Column(name = "state")
    private boolean state;
    @Column(name = "workType")
    private String workType;
    @Column(name = "activityValue")
    private int activityValue;
    @Column(name = "reward")
    private String reward;
    @Column(name = "info")
    private String info;
    @Column(name = "workCount")
    private int workCount;
    @ReadOnly
    @Column(name = "startDate")
    private java.sql.Date startDate;
    @ReadOnly
    @Column(name = "endDate")
    private java.sql.Date endDate;


    public int getWorkId()
    {
        return workId;
    }

    public void setWorkId(int workId)
    {
        this.workId = workId;
    }


    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }


    public String getWorkType()
    {
        return workType;
    }

    public void setWorkType(String workType)
    {
        this.workType = workType;
    }


    public int getActivityValue()
    {
        return activityValue;
    }

    public void setActivityValue(int activityValue)
    {
        this.activityValue = activityValue;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public int getWorkCount()
    {
        return workCount;
    }

    public void setWorkCount(int workCount)
    {
        this.workCount = workCount;
    }

    public java.sql.Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate)
    {
        this.startDate = startDate;
    }


    public java.sql.Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate)
    {
        this.endDate = endDate;
    }

}
