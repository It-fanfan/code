package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RoundRecord extends RoundRecordKey
{
    private String ddname;

    private Integer ddgame;

    private String ddround;

    private Date ddstart;

    private Date ddend;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddsubmit;

    private Integer ddresult;

    private Date ddtime;

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public Integer getDdgame()
    {
        return ddgame;
    }

    public void setDdgame(Integer ddgame)
    {
        this.ddgame = ddgame;
    }

    public String getDdround()
    {
        return ddround;
    }

    public void setDdround(String ddround)
    {
        this.ddround = ddround == null ? null : ddround.trim();
    }

    public Date getDdstart()
    {
        return ddstart;
    }

    public void setDdstart(Date ddstart)
    {
        this.ddstart = ddstart;
    }

    public Date getDdend()
    {
        return ddend;
    }

    public void setDdend(Date ddend)
    {
        this.ddend = ddend;
    }

    public Date getDdsubmit()
    {
        return ddsubmit;
    }

    public void setDdsubmit(Date ddsubmit)
    {
        this.ddsubmit = ddsubmit;
    }

    public Integer getDdresult()
    {
        return ddresult;
    }

    public void setDdresult(Integer ddresult)
    {
        this.ddresult = ddresult;
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
        sb.append(", ddname=").append(ddname);
        sb.append(", ddgame=").append(ddgame);
        sb.append(", ddround=").append(ddround);
        sb.append(", ddstart=").append(ddstart);
        sb.append(", ddend=").append(ddend);
        sb.append(", ddsubmit=").append(ddsubmit);
        sb.append(", ddresult=").append(ddresult);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}