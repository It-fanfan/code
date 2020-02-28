package com.code.dao.entity.fish.userinfo;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserExt
{

    @Column(name = "openid")
    private String openid;
    @Column(name = "unionid")
    private String unionid;
    @Column(name = "level")
    private int level;
    @Column(name = "modifty_time")
    private java.sql.Timestamp modiftyTime;
    @Column(name = "openGId")
    private String openGId;


    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }


    public String getUnionid()
    {
        return unionid;
    }

    public void setUnionid(String unionid)
    {
        this.unionid = unionid;
    }


    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public java.sql.Timestamp getModiftyTime()
    {
        return modiftyTime;
    }

    public void setModiftyTime(java.sql.Timestamp modiftyTime)
    {
        this.modiftyTime = modiftyTime;
    }


    public String getOpenGId()
    {
        return openGId;
    }

    public void setOpenGId(String openGId)
    {
        this.openGId = openGId;
    }

}
