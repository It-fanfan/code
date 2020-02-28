package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "user_fish_ext")
public class UserFish
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "total")
    private int total;
    @Column(name = "counts")
    private int counts;
    @Column(name = "fishInfo")
    private String fishInfo;
    @Column(name = "updateTime")
    private Timestamp updateTime;

    public UserFish()
    {
    }

    public UserFish(long userId, int total, int counts, String fishInfo)
    {
        this.userId = userId;
        this.total = total;
        this.counts = counts;
        this.fishInfo = fishInfo;
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getCounts()
    {
        return counts;
    }

    public void setCounts(int counts)
    {
        this.counts = counts;
    }

    public String getFishInfo()
    {
        return fishInfo;
    }

    public void setFishInfo(String fishInfo)
    {
        this.fishInfo = fishInfo;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }
}
