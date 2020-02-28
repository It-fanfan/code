package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShowRanking
{
    private Integer ddCode;//赛场编号
    private Boolean ddGroup;//群标签
    private Integer ddIndex;//赛场轮次

    private String appName;//产品名称
    private RankingResult rankingResult;//赛场结果
    private String gamesName;//游戏名称
    private Integer gamesCode;//游戏ID
    private String roundName;//赛制名称
    private String roundCode;//赛制编号
    private String roundLength;//时长

    private Integer ddNumber;//参赛人数
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date matchdate;//比赛日期
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date startTime;//开始时间
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date endTime;//结束时间

    public Integer getDdNumber()
    {
        return ddNumber;
    }

    public void setDdNumber(Integer ddNumber)
    {
        this.ddNumber = ddNumber;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getRoundCode()
    {
        return roundCode;
    }

    public void setRoundCode(String roundCode)
    {
        this.roundCode = roundCode;
    }

    public Integer getDdCode()
    {
        return ddCode;
    }

    public void setDdCode(Integer ddCode)
    {
        this.ddCode = ddCode;
    }

    public Boolean getDdGroup()
    {
        return ddGroup;
    }

    public void setDdGroup(Boolean ddGroup)
    {
        this.ddGroup = ddGroup;
    }

    public Integer getDdIndex()
    {
        return ddIndex;
    }

    public void setDdIndex(Integer ddIndex)
    {
        this.ddIndex = ddIndex;
    }

    public Integer getGamesCode()
    {
        return gamesCode;
    }

    public void setGamesCode(Integer gamesCode)
    {
        this.gamesCode = gamesCode;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public RankingResult getRankingResult()
    {
        return rankingResult;
    }

    public void setRankingResult(RankingResult rankingResult)
    {
        this.rankingResult = rankingResult;
    }

    public String getGamesName()
    {
        return gamesName;
    }

    public void setGamesName(String gamesName)
    {
        this.gamesName = gamesName;
    }

    public String getRoundName()
    {
        return roundName;
    }

    public void setRoundName(String roundName)
    {
        this.roundName = roundName;
    }

    public String getRoundLength()
    {
        return roundLength;
    }

    public void setRoundLength(String roundLength)
    {
        this.roundLength = roundLength;
    }

    public Date getMatchdate()
    {
        return matchdate;
    }

    public void setMatchdate(Date matchdate)
    {
        this.matchdate = matchdate;
    }
}