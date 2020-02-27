package com.fish.protocols;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserPayVO
{
    //排名
    private Long ranking;
    //用户编号
    private Long userId;
    //昵称
    private String nickName;
    //累计付费金额
    private BigDecimal payCost;
    //近7天消费金额
    private BigDecimal lastSevenPay;
    //最喜爱商品
    private int loveGood;
    //水域信息
    private int basin;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Timestamp register;
    //在线时长
    private long online;

    public Long getRanking()
    {
        return ranking;
    }

    public void setRanking(Long ranking)
    {
        this.ranking = ranking;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public BigDecimal getPayCost()
    {
        return payCost;
    }

    public void setPayCost(BigDecimal payCost)
    {
        this.payCost = payCost;
    }

    public BigDecimal getLastSevenPay()
    {
        return lastSevenPay;
    }

    public void setLastSevenPay(BigDecimal lastSevenPay)
    {
        this.lastSevenPay = lastSevenPay;
    }

    public int getLoveGood()
    {
        return loveGood;
    }

    public void setLoveGood(int loveGood)
    {
        this.loveGood = loveGood;
    }

    public int getBasin()
    {
        return basin;
    }

    public void setBasin(int basin)
    {
        this.basin = basin;
    }

    public Timestamp getRegister()
    {
        return register;
    }

    public void setRegister(Timestamp register)
    {
        this.register = register;
    }

    public long getOnline()
    {
        return online;
    }

    public void setOnline(long online)
    {
        this.online = online;
    }
}
