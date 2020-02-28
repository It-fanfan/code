package com.fish.protocols;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class MatchCost
{
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date ddtime;

    private String appId;

    private String gameCode;

    private BigDecimal coinCount;

    private BigDecimal videoCount;

    private BigDecimal coinTotal;

    private BigDecimal videoTotal;

    /**
     * 對比是否匹配
     *
     * @param cost 消耗参数
     * @return 匹配结果
     */
    public boolean compare(MatchCost cost)
    {
        if (getDdtime().compareTo(cost.getDdtime()) != 0)
            return false;
        if (appId != null && !getAppId().equals(cost.getAppId()))
            return false;
        return gameCode == null || getGameCode().equals(cost.getGameCode());
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public BigDecimal getCoinCount()
    {
        return coinCount;
    }

    public void setCoinCount(BigDecimal coinCount)
    {
        this.coinCount = coinCount;
    }

    public BigDecimal getVideoCount()
    {
        return videoCount;
    }

    public void setVideoCount(BigDecimal videoCount)
    {
        this.videoCount = videoCount;
    }

    public BigDecimal getCoinTotal()
    {
        return coinTotal;
    }

    public void setCoinTotal(BigDecimal coinTotal)
    {
        this.coinTotal = coinTotal;
    }

    public BigDecimal getVideoTotal()
    {
        return videoTotal;
    }

    public void setVideoTotal(BigDecimal videoTotal)
    {
        this.videoTotal = videoTotal;
    }

    public String getGameCode()
    {
        return gameCode;
    }

    public void setGameCode(String gameCode)
    {
        this.gameCode = gameCode;
    }
}
