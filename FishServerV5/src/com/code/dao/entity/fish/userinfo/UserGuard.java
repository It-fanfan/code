package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_guard")
public class UserGuard
{

    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "friendGuard")
    private String friendGuard;
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


    public String getFriendGuard()
    {
        return friendGuard;
    }

    public void setFriendGuard(String friendGuard)
    {
        this.friendGuard = friendGuard;
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
