package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_email")
public class ConfigEmail
{
    @PrimaryKey
    @Column(name = "udpType")
    private String udpType;
    @Column(name = "title")
    private String title;
    @Column(name = "msg")
    private String msg;
    @Column(name = "button")
    private String button;
    @Column(name = "reward")
    private String reward;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    public String getUdpType()
    {
        return udpType;
    }

    public void setUdpType(String udpType)
    {
        this.udpType = udpType;
    }


    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public String getButton()
    {
        return button;
    }

    public void setButton(String button)
    {
        this.button = button;
    }

}
