package com.code.dao.entity.achievement;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_achievement")
public class UserAchievement
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @PrimaryKey
    @Column(name = "achievementType")
    private String achievementType;
    @Column(name = "process")
    private String process;
    @Column(name = "updateTime")
    private java.sql.Timestamp updateTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getAchievementType()
    {
        return achievementType;
    }

    public void setAchievementType(String achievementType)
    {
        this.achievementType = achievementType;
    }


    public String getProcess()
    {
        return process;
    }

    public void setProcess(String process)
    {
        this.process = process;
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
