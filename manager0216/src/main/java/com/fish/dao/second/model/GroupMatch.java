package com.fish.dao.second.model;

import java.util.Date;

public class GroupMatch
{
    private String ddid;

    private Integer ddstate;

    private Integer ddgame;

    private String ddmatch;

    private Integer ddstart;

    private Integer ddend;

    private Date ddtime;

    public String getDdid()
    {
        return ddid;
    }

    public void setDdid(String ddid)
    {
        this.ddid = ddid == null ? null : ddid.trim();
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

    public String getDdmatch()
    {
        return ddmatch;
    }

    public void setDdmatch(String ddmatch)
    {
        this.ddmatch = ddmatch == null ? null : ddmatch.trim();
    }

    public Integer getDdstart()
    {
        return ddstart;
    }

    public void setDdstart(Integer ddstart)
    {
        this.ddstart = ddstart;
    }

    public Integer getDdend()
    {
        return ddend;
    }

    public void setDdend(Integer ddend)
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
        sb.append(", ddid=").append(ddid);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddgame=").append(ddgame);
        sb.append(", ddmatch=").append(ddmatch);
        sb.append(", ddstart=").append(ddstart);
        sb.append(", ddend=").append(ddend);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}