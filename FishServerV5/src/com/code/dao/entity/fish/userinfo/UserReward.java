package com.code.dao.entity.fish.userinfo;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_reward")
public class UserReward
{

    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "rewardType")
    private String rewardType;
    @Column(name = "rewardIndex")
    private int rewardIndex;
    @Column(name = "price")
    private int price;
    @Column(name = "receiveTime")
    private java.sql.Timestamp receiveTime;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getRewardType()
    {
        return rewardType;
    }

    public void setRewardType(String rewardType)
    {
        this.rewardType = rewardType;
    }


    public int getRewardIndex()
    {
        return rewardIndex;
    }

    public void setRewardIndex(int rewardIndex)
    {
        this.rewardIndex = rewardIndex;
    }


    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }


    public java.sql.Timestamp getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(java.sql.Timestamp receiveTime)
    {
        this.receiveTime = receiveTime;
    }

}
