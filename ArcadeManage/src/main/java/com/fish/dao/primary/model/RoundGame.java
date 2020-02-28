package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RoundGame
{
    private String roundSelect;
    private String gameCodeSelect;

    private String ddreward;
    private String gameName;
    private String roundName;

    private Integer ddcode;

    private String ddname;

    private Boolean ddstate;

    private Integer ddgame;

    private String ddround;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date ddstart;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date ddend;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date times;
    private String jumpDirect;//图片跳转

    public String getJumpDirect()
    {
        return jumpDirect;
    }

    public void setJumpDirect(String jumpDirect)
    {
        this.jumpDirect = jumpDirect;
    }

    public String getRoundSelect()
    {
        return roundSelect;
    }

    public void setRoundSelect(String roundSelect)
    {
        this.roundSelect = roundSelect;
    }

    public String getGameCodeSelect()
    {
        return gameCodeSelect;
    }

    public void setGameCodeSelect(String gameCodeSelect)
    {
        this.gameCodeSelect = gameCodeSelect;
    }

    public String getDdreward()
    {
        return ddreward;
    }

    public void setDdreward(String ddreward)
    {
        this.ddreward = ddreward;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getRoundName()
    {
        return roundName;
    }

    public void setRoundName(String roundName)
    {
        this.roundName = roundName;
    }

    public Integer getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(Integer ddcode)
    {
        this.ddcode = ddcode;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
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
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddname=").append(ddname);
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