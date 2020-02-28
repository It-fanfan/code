package com.code.dao.entity.fish.userinfo;

import com.annotation.Comments;
import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_activity_work")
public class UserActivityWork
{
    @PrimaryKey
    @Column(name = "userId")
    @Comments(name = "玩家编号")
    private long userId;
    @PrimaryKey
    @Column(name = "dayFlag")
    @Comments(name = "日期编号")
    private int dayFlag;
    @Column(name = "activityValue")
    @Comments(name = "活跃值")
    private int activityValue;
    @Column(name = "activityJson")
    @Comments(name = "活跃详情信息")
    private String activityJson;
    @Column(name = "updateTime")
    @Comments(name = "更新时间")
    private java.sql.Timestamp updateTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public int getDayFlag()
    {
        return dayFlag;
    }

    public void setDayFlag(int dayFlag)
    {
        this.dayFlag = dayFlag;
    }


    public int getActivityValue()
    {
        return activityValue;
    }

    public void setActivityValue(int activityValue)
    {
        this.activityValue = activityValue;
    }


    public String getActivityJson()
    {
        return activityJson;
    }

    public void setActivityJson(String activityJson)
    {
        this.activityJson = activityJson;
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
