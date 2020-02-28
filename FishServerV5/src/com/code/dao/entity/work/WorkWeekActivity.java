package com.code.dao.entity.work;

import com.annotation.PrimaryKey;
import com.code.service.work.WorkConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Date;

@Entity(name = "work_week_activity")
public class WorkWeekActivity implements WorkConfig
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "state")
    private boolean state;
    @Column(name = "activityValue")
    private int activityValue;
    @Column(name = "reward")
    private String reward;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
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

    @Override
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
}
