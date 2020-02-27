package com.code.dao.entity.achievement;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_achievement_get")
public class UserAchievementGet
{

    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @PrimaryKey
    @Column(name = "achievementId")
    private int achievementId;
    @Column(name = "getTime")
    private java.sql.Timestamp getTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public int getAchievementId()
    {
        return achievementId;
    }

    public void setAchievementId(int achievementId)
    {
        this.achievementId = achievementId;
    }


    public java.sql.Timestamp getGetTime()
    {
        return getTime;
    }

    public void setGetTime(java.sql.Timestamp getTime)
    {
        this.getTime = getTime;
    }

}
