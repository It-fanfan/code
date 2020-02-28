package com.fish.dao.primary.model;

import org.springframework.stereotype.Component;

@Component
public class RankingResult
{

    private String uid;
    private String userName;
    private String matchId;
    private String roundName;

    private Long ranking;

    private Long mark;

    private String awardType;
    private Integer awardTotal;
    private String award;
    private String periodTime;

    public String getPeriodTime()
    {
        return periodTime;
    }

    public void setPeriodTime(String periodTime)
    {
        this.periodTime = periodTime;
    }

    public String getAwardType()
    {
        return awardType;
    }

    public void setAwardType(String awardType)
    {
        this.awardType = awardType;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getMatchId()
    {
        return matchId;
    }

    public void setMatchId(String matchId)
    {
        this.matchId = matchId;
    }

    public String getRoundName()
    {
        return roundName;
    }

    public void setRoundName(String roundName)
    {
        this.roundName = roundName;
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

    public String getType()
    {
        return awardType;
    }

    public void setType(String awardType)
    {
        this.awardType = awardType;
    }

    public Integer getAwardTotal()
    {
        return awardTotal;
    }

    public void setAwardTotal(Integer awardTotal)
    {
        this.awardTotal = awardTotal;
    }

    public String getAward()
    {
        return award;
    }

    public void setAward(String award)
    {

        this.award = award;
    }

    @Override
    public String toString()
    {
        return "RankingResult{" +
                "uid='" + uid + '\'' +
                ", matchId='" + matchId + '\'' +
                ", roundName='" + roundName + '\'' +
                ", ranking=" + ranking +
                ", mark=" + mark +
                ", awardType='" + awardType + '\'' +
                ", awardTotal=" + awardTotal +
                ", award='" + award + '\'' +
                '}';
    }
}