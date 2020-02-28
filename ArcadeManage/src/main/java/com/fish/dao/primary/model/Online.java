package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Online
{
    private Long id;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date times;

    private Integer online;

    private Integer idleroom;

    private Integer buzyroom;

    private String gameinfo;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date inserttime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getTimes()
    {
        return times;
    }

    public void setTimes(Date times)
    {
        this.times = times;
    }

    public Integer getOnline()
    {
        return online;
    }

    public void setOnline(Integer online)
    {
        this.online = online;
    }

    public Integer getIdleroom()
    {
        return idleroom;
    }

    public void setIdleroom(Integer idleroom)
    {
        this.idleroom = idleroom;
    }

    public Integer getBuzyroom()
    {
        return buzyroom;
    }

    public void setBuzyroom(Integer buzyroom)
    {
        this.buzyroom = buzyroom;
    }

    public String getGameinfo()
    {
        return gameinfo;
    }

    public void setGameinfo(String gameinfo)
    {
        this.gameinfo = gameinfo == null ? null : gameinfo.trim();
    }

    public Date getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Date inserttime)
    {
        this.inserttime = inserttime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", times=").append(times);
        sb.append(", online=").append(online);
        sb.append(", idleroom=").append(idleroom);
        sb.append(", buzyroom=").append(buzyroom);
        sb.append(", gameinfo=").append(gameinfo);
        sb.append(", inserttime=").append(inserttime);
        sb.append("]");
        return sb.toString();
    }
}