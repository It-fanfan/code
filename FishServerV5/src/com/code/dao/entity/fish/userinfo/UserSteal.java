package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_steal")
public class UserSteal
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @PrimaryKey
    @Column(name = "ftId")
    private int ftId;
    @Column(name = "stealUserId")
    private long stealUserId;
    @Column(name = "updateTime")
    private long updateTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public int getFtId()
    {
        return ftId;
    }

    public void setFtId(int ftId)
    {
        this.ftId = ftId;
    }

    public long getStealUserId()
    {
        return stealUserId;
    }

    public void setStealUserId(long stealUserId)
    {
        this.stealUserId = stealUserId;
    }


    public long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }

}
