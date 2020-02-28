package com.code.dao.entity.message;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "email")
public class Email
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "messageType")
    private String messageType;
    @Column(name = "userId")
    private long userId;
    @Column(name = "icon")
    private String icon;
    @Column(name = "title")
    private String title;
    @Column(name = "context")
    private String context;
    @Column(name = "reward")
    private String reward;
    @Column(name = "button")
    private String button;
    @Column(name = "parameters")
    private String parameters;
    @Column(name = "leaveType")
    private String leaveType;
    @Column(name = "times")
    private Timestamp times;

    public Email()
    {
    }

    public Email(String messageType, long userId, String icon, String title, String context, String reward, String button, String leave, Timestamp times)
    {
        this.messageType = messageType;
        this.userId = userId;
        this.icon = icon;
        this.title = title;
        this.context = context;
        this.reward = reward;
        this.button = button;
        this.leaveType = leave;
        this.times = times;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

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


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
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

    public Timestamp getTimes()
    {
        return times;
    }

    public void setTimes(Timestamp times)
    {
        this.times = times;
    }

    public String getParameters()
    {
        return parameters;
    }

    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }
}
