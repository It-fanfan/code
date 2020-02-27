package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ProductShow {
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date newDay;
    private String appId;//appId
    private Integer newUserCount;//新增用户数
    private Integer acUserCount;//活跃用户数
    private Integer remain1;//1日留存
    private Integer remain3;//3日留存
    private Integer enterCount;//用户进入总次数
    private Integer payUserCount;//付费用户总人数
    private String onlineAvg;//人均在线时长
    private BigDecimal adRevenue;//广告收益
    private BigDecimal inRevenue;//内购收益
    private BigDecimal inArpu;//内购ARPU

    public Date getNewDay() {
        return newDay;
    }

    public void setNewDay(Date newDay) {
        this.newDay = newDay;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Integer getAcUserCount() {
        return acUserCount;
    }

    public void setAcUserCount(Integer acUserCount) {
        this.acUserCount = acUserCount;
    }

    public Integer getRemain1() {
        return remain1;
    }

    public void setRemain1(Integer remain1) {
        this.remain1 = remain1;
    }

    public Integer getRemain3() {
        return remain3;
    }

    public void setRemain3(Integer remain3) {
        this.remain3 = remain3;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

    public Integer getPayUserCount() {
        return payUserCount;
    }

    public void setPayUserCount(Integer payUserCount) {
        this.payUserCount = payUserCount;
    }

    public String getOnlineAvg() {
        return onlineAvg;
    }

    public void setOnlineAvg(String onlineAvg) {
        this.onlineAvg = onlineAvg;
    }

    public BigDecimal getAdRevenue() {
        return adRevenue;
    }

    public void setAdRevenue(BigDecimal adRevenue) {
        this.adRevenue = adRevenue;
    }

    public BigDecimal getInRevenue() {
        return inRevenue;
    }

    public void setInRevenue(BigDecimal inRevenue) {
        this.inRevenue = inRevenue;
    }

    public BigDecimal getInArpu() {
        return inArpu;
    }

    public void setInArpu(BigDecimal inArpu) {
        this.inArpu = inArpu;
    }
}
