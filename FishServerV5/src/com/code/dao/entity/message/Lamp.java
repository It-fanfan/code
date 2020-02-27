package com.code.dao.entity.message;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "lamp")
public class Lamp
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "state")
    private boolean state;
    @Column(name = "level")
    private int level;
    @Column(name = "describe")
    private String describe;
    @Column(name = "msg")
    private String msg;
    @Column(name = "startTime")
    private java.sql.Timestamp startTime;
    @Column(name = "endTime")
    private java.sql.Timestamp endTime;


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


    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }


    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }


    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public java.sql.Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(java.sql.Timestamp startTime)
    {
        this.startTime = startTime;
    }


    public java.sql.Timestamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime(java.sql.Timestamp endTime)
    {
        this.endTime = endTime;
    }

}
