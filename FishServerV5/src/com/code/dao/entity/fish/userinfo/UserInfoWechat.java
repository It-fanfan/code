package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_info_wechat")
public class UserInfoWechat
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "openId")
    private String openId;
    @Column(name = "unionId")
    private String unionId;
    @Column(name = "sex")
    private int sex;
    @Column(name = "city")
    private String city;
    @Column(name = "province")
    private String province;
    @Column(name = "country")
    private String country;
    @Column(name = "invite")
    private long invite;
    @Column(name = "groupId")
    private String groupId;
    @Column(name = "loginTime")
    private java.sql.Timestamp loginTime;
    @Column(name = "inviteGroupId")
    private String inviteGroupId;


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


    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }


    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }


    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }


    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }


    public long getInvite()
    {
        return invite;
    }

    public void setInvite(long invite)
    {
        this.invite = invite;
    }


    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }


    public java.sql.Timestamp getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(java.sql.Timestamp loginTime)
    {
        this.loginTime = loginTime;
    }


    public String getInviteGroupId()
    {
        return inviteGroupId;
    }

    public void setInviteGroupId(String inviteGroupId)
    {
        this.inviteGroupId = inviteGroupId;
    }

}
