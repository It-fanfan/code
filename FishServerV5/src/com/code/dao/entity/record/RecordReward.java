package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_reward")
public class RecordReward
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "rewardTotal")
    private int rewardTotal;
    @Column(name = "source")
    private String source;
    @Column(name = "rewardData")
    private String rewardData;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


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


    public int getRewardTotal()
    {
        return rewardTotal;
    }

    public void setRewardTotal(int rewardTotal)
    {
        this.rewardTotal = rewardTotal;
    }


    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }


    public String getRewardData()
    {
        return rewardData;
    }

    public void setRewardData(String rewardData)
    {
        this.rewardData = rewardData;
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
