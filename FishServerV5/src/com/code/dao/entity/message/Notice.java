package com.code.dao.entity.message;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "notice")
public class Notice
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "state")
    private boolean state;
    @Column(name = "title")
    private String title;
    @Column(name = "msg")
    private String msg;
    @Column(name = "icon")
    private String icon;
    @Column(name = "reward")
    private String reward;
    @Column(name = "button")
    private String button;
    @Column(name = "leaveType")
    private String leaveType;
    @Column(name = "include")
    private int include;
    @Column(name = "userList")
    private String userList;
    @Column(name = "startTime")
    private java.sql.Timestamp startTime;
    @Column(name = "endTime")
    private java.sql.Timestamp endTime;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    public String getButton()
    {
        return button;
    }

    public void setButton(String button)
    {
        this.button = button;
    }

    public String getLeaveType()
    {
        return leaveType;
    }

    public void setLeaveType(String leaveType)
    {
        this.leaveType = leaveType;
    }

    public int getInclude()
    {
        return include;
    }

    public void setInclude(int include)
    {
        this.include = include;
    }

    public String getUserList()
    {
        return userList;
    }

    public void setUserList(String userList)
    {
        this.userList = userList;
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
