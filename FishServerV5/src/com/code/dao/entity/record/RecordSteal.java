package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_steal")
public class RecordSteal
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "type")
    private String type;
    @Column(name = "value")
    private String value;
    @Column(name = "stealName")
    private String stealName;
    @Column(name = "stealUserId")
    private long stealUserId;
    @Column(name = "success")
    private boolean success;
    @Column(name = "updateTime")
    private long updateTime;


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


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }


    public long getStealUserId()
    {
        return stealUserId;
    }

    public void setStealUserId(long stealUserId)
    {
        this.stealUserId = stealUserId;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getStealName()
    {
        return stealName;
    }

    public void setStealName(String stealName)
    {
        this.stealName = stealName;
    }
}
