package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AllCost
{
    private Long id;

    private String dduid;

    private String ddappid;

    private String ddtype;

    private Long ddhistory;

    private Long ddcurrent;

    private Integer ddvalue;

    private String ddcosttype;

    private String ddcostextra;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtime;

    private String nickname;

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

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdtype()
    {
        return ddtype;
    }

    public void setDdtype(String ddtype)
    {
        this.ddtype = ddtype == null ? null : ddtype.trim();
    }

    public Long getDdhistory()
    {
        return ddhistory;
    }

    public void setDdhistory(Long ddhistory)
    {
        this.ddhistory = ddhistory;
    }

    public Long getDdcurrent()
    {
        return ddcurrent;
    }

    public void setDdcurrent(Long ddcurrent)
    {
        this.ddcurrent = ddcurrent;
    }

    public Integer getDdvalue()
    {
        return ddvalue;
    }

    public void setDdvalue(Integer ddvalue)
    {
        this.ddvalue = ddvalue;
    }

    public String getDdcosttype()
    {
        return ddcosttype;
    }

    public void setDdcosttype(String ddcosttype)
    {
        this.ddcosttype = ddcosttype == null ? null : ddcosttype.trim();
    }

    public String getDdcostextra()
    {
        return ddcostextra;
    }

    public void setDdcostextra(String ddcostextra)
    {
        this.ddcostextra = ddcostextra == null ? null : ddcostextra.trim();
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
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
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddtype=").append(ddtype);
        sb.append(", ddhistory=").append(ddhistory);
        sb.append(", ddcurrent=").append(ddcurrent);
        sb.append(", ddvalue=").append(ddvalue);
        sb.append(", ddcosttype=").append(ddcosttype);
        sb.append(", ddcostextra=").append(ddcostextra);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}