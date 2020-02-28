package com.fish.dao.primary.model;

import java.util.Date;

public class RoundGroup
{
    private Integer ddcode;

    private Integer ddstate;

    private Integer ddgame;

    private String ddround;

    private Date ddstart;

    private Date ddend;

    private Date ddtime;

    public Integer getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(Integer ddcode)
    {
        this.ddcode = ddcode;
    }

    public Integer getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Integer ddstate)
    {
        this.ddstate = ddstate;
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
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddgame=").append(ddgame);
        sb.append(", ddround=").append(ddround);
        sb.append(", ddstart=").append(ddstart);
        sb.append(", ddend=").append(ddend);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}