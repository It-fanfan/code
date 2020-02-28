package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SupplementOrder
{
    private Long id;

    private String userid;

    private String username;

    private String appid;

    private String appname;

    private Integer programType;

    private Integer coinCount;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username == null ? null : username.trim();
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getAppname()
    {
        return appname;
    }

    public void setAppname(String appname)
    {
        this.appname = appname == null ? null : appname.trim();
    }

    public Integer getProgramType()
    {
        return programType;
    }

    public void setProgramType(Integer programType)
    {
        this.programType = programType;
    }

    public Integer getCoinCount()
    {
        return coinCount;
    }

    public void setCoinCount(Integer coinCount)
    {
        this.coinCount = coinCount;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", username=").append(username);
        sb.append(", appid=").append(appid);
        sb.append(", appname=").append(appname);
        sb.append(", programType=").append(programType);
        sb.append(", coinCount=").append(coinCount);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}