package com.fish.dao.primary.model;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GameRound
{
    private Integer id;

    private Integer ddpriority;

    private Boolean ddstate;

    private Integer ddgame;

    private String ddround;

    private Date ddstart;

    private Date ddend;

    private Date times;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getDdpriority()
    {
        return ddpriority;
    }

    public void setDdpriority(Integer ddpriority)
    {
        this.ddpriority = ddpriority;
    }

    public Boolean getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate)
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

    public Date getTimes()
    {
        return times;
    }

    public void setTimes(Date times)
    {
        this.times = times;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddpriority=").append(ddpriority);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddgame=").append(ddgame);
        sb.append(", ddround=").append(ddround);
        sb.append(", ddstart=").append(ddstart);
        sb.append(", ddend=").append(ddend);
        sb.append(", times=").append(times);
        sb.append("]");
        return sb.toString();
    }
}