package com.fish.protocols;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PlayUserVO
{
    //用户编号
    private Long userId;
    //新增时间
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Timestamp register;
    //昵称
    private String nickName;
    //
    //珍珠数
    private Long pearl;
    //
    //贝壳数
    private Long shell;
    //
    //普通图鉴数
    private Integer book;
    //
    //充值金额
    private BigDecimal payCost;
    //
    //平台
    private String platform;
    //
    //设备
    private String device;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Timestamp getRegister()
    {
        return register;
    }

    public void setRegister(Timestamp register)
    {
        this.register = register;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public Long getPearl()
    {
        return pearl;
    }

    public void setPearl(Long pearl)
    {
        this.pearl = pearl;
    }

    public Long getShell()
    {
        return shell;
    }

    public void setShell(Long shell)
    {
        this.shell = shell;
    }

    public Integer getBook()
    {
        return book;
    }

    public void setBook(Integer book)
    {
        this.book = book;
    }

    public BigDecimal getPayCost()
    {
        return payCost;
    }

    public void setPayCost(BigDecimal payCost)
    {
        this.payCost = payCost;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }
}
