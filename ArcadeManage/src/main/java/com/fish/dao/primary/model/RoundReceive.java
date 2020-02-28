package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RoundReceive
{
    private String userName;
    private String gameName;
    private String roudName;
    private String roudTime;
    private Long id;

    private String dduid;

    private Integer ddmcode;

    private Boolean ddgroup;

    private Integer ddmindex;

    private Integer ddgcode;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddmstart;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddmend;

    private Long ddmark;

    private Integer ddranking;

    private String ddtype;

    private Integer ddtotal;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtime;

    public String getRoudTime()
    {
        return roudTime;
    }

    public void setRoudTime(String roudTime)
    {
        this.roudTime = roudTime;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getRoudName()
    {
        return roudName;
    }

    public void setRoudName(String roudName)
    {
        this.roudName = roudName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDduid()
    {
        return dduid;
    }

    public void setDduid(String dduid)
    {
        this.dduid = dduid == null ? null : dduid.trim();
    }

    public Integer getDdmcode()
    {
        return ddmcode;
    }

    public void setDdmcode(Integer ddmcode)
    {
        this.ddmcode = ddmcode;
    }

    public Boolean getDdgroup()
    {
        return ddgroup;
    }

    public void setDdgroup(Boolean ddgroup)
    {
        this.ddgroup = ddgroup;
    }

    public Integer getDdmindex()
    {
        return ddmindex;
    }

    public void setDdmindex(Integer ddmindex)
    {
        this.ddmindex = ddmindex;
    }

    public Integer getDdgcode()
    {
        return ddgcode;
    }

    public void setDdgcode(Integer ddgcode)
    {
        this.ddgcode = ddgcode;
    }

    public Date getDdmstart()
    {
        return ddmstart;
    }

    public void setDdmstart(Date ddmstart)
    {
        this.ddmstart = ddmstart;
    }

    public Date getDdmend()
    {
        return ddmend;
    }

    public void setDdmend(Date ddmend)
    {
        this.ddmend = ddmend;
    }

    public Long getDdmark()
    {
        return ddmark;
    }

    public void setDdmark(Long ddmark)
    {
        this.ddmark = ddmark;
    }

    public Integer getDdranking()
    {
        return ddranking;
    }

    public void setDdranking(Integer ddranking)
    {
        this.ddranking = ddranking;
    }

    public String getDdtype()
    {
        return ddtype;
    }

    public void setDdtype(String ddtype)
    {
        this.ddtype = ddtype == null ? null : ddtype.trim();
    }

    public Integer getDdtotal()
    {
        return ddtotal;
    }

    public void setDdtotal(Integer ddtotal)
    {
        this.ddtotal = ddtotal;
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dduid=").append(dduid);
        sb.append(", ddmcode=").append(ddmcode);
        sb.append(", ddgroup=").append(ddgroup);
        sb.append(", ddmindex=").append(ddmindex);
        sb.append(", ddgcode=").append(ddgcode);
        sb.append(", ddmstart=").append(ddmstart);
        sb.append(", ddmend=").append(ddmend);
        sb.append(", ddmark=").append(ddmark);
        sb.append(", ddranking=").append(ddranking);
        sb.append(", ddtype=").append(ddtype);
        sb.append(", ddtotal=").append(ddtotal);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}