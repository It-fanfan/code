package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_info_huawei")
public class UserInfoHuawei
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "openId")
    private String openId;
    @Column(name = "unionId")
    private String unionId;
    @Column(name = "invite")
    private long invite;
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


    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }


    public String getUnionId()
    {
        return unionId;
    }

    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public long getInvite()
    {
        return invite;
    }

    public void setInvite(long invite)
    {
        this.invite = invite;
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
