package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_info")
public class UserInfo
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "nickName")
    private String nickName;
    @Column(name = "icon")
    private String icon;
    @Column(name = "platform")
    private String platform;
    @Column(name = "device")
    private String device;
    @Column(name = "registTime")
    private java.sql.Timestamp registTime;
    @Column(name = "loginTime")
    private java.sql.Timestamp loginTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }


    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }


    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }


    public java.sql.Timestamp getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(java.sql.Timestamp registTime)
    {
        this.registTime = registTime;
    }


    public java.sql.Timestamp getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(java.sql.Timestamp loginTime)
    {
        this.loginTime = loginTime;
    }

}
