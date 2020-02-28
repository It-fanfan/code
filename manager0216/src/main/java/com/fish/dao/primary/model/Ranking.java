package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Ranking
{
    private String roundName;
    private String gameName;
    private String formatName;

    private String award;
    private String userName;
    private String periodTime;

    private Long id;

    private String matchid;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date matchdate;

    private Integer gamecode;

    private Integer matchindex;

    private String uid;

    private Long ranking;

    private Long mark;

    private String awardtype;

    private Integer awardtotal;

    private Date inserttime;

    private Boolean ddgroup;

    public String getRoundName()
    {
        return roundName;
    }

    public void setRoundName(String roundName)
    {
        this.roundName = roundName;
    }

    public String getPeriodTime()
    {
        return periodTime;
    }

    public void setPeriodTime(String periodTime)
    {
        this.periodTime = periodTime;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getFormatName()
    {
        return formatName;
    }

    public void setFormatName(String formatName)
    {
        this.formatName = formatName;
    }

    public String getAward()
    {
        return award;
    }

    public void setAward(String award)
    {
        this.award = award;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMatchid()
    {
        return matchid;
    }

    public void setMatchid(String matchid)
    {
        this.matchid = matchid == null ? null : matchid.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getMatchdate()
    {
        return matchdate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setMatchdate(Date matchdate)
    {
        this.matchdate = matchdate;
    }

    public Integer getGamecode()
    {
        return gamecode;
    }

    public void setGamecode(Integer gamecode)
    {
        this.gamecode = gamecode;
    }

    public Integer getMatchindex()
    {
        return matchindex;
    }

    public void setMatchindex(Integer matchindex)
    {
        this.matchindex = matchindex;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid == null ? null : uid.trim();
    }

    public Long getRanking()
    {
        return ranking;
    }

    public void setRanking(Long ranking)
    {
        this.ranking = ranking;
    }

    public Long getMark()
    {
        return mark;
    }

    public void setMark(Long mark)
    {
        this.mark = mark;
    }

    public String getAwardtype()
    {
        return awardtype;
    }

    public void setAwardtype(String awardtype)
    {
        this.awardtype = awardtype == null ? null : awardtype.trim();
    }

    public Integer getAwardtotal()
    {
        return awardtotal;
    }

    public void setAwardtotal(Integer awardtotal)
    {
        this.awardtotal = awardtotal;
    }

    public Date getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Date inserttime)
    {
        this.inserttime = inserttime;
    }

    public Boolean getDdgroup()
    {
        return ddgroup;
    }

    public void setDdgroup(Boolean ddgroup)
    {
        this.ddgroup = ddgroup;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", matchid=").append(matchid);
        sb.append(", matchdate=").append(matchdate);
        sb.append(", gamecode=").append(gamecode);
        sb.append(", matchindex=").append(matchindex);
        sb.append(", uid=").append(uid);
        sb.append(", ranking=").append(ranking);
        sb.append(", mark=").append(mark);
        sb.append(", awardtype=").append(awardtype);
        sb.append(", awardtotal=").append(awardtotal);
        sb.append(", inserttime=").append(inserttime);
        sb.append(", ddgroup=").append(ddgroup);
        sb.append("]");
        return sb.toString();
    }
}