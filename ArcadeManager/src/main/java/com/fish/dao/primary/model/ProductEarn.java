package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Component
public class ProductEarn {
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date newDay;//时间
    private String appId;//appId
    private Integer enterCount;//用户进入总次数
    private Integer payUserCount;//付费用户总人数
    private String onlineAvg;//人均在线时长
    private BigDecimal adRevenue;//广告收益
    private BigDecimal inRevenue;//内购收益
    private BigDecimal inArpu;//内购ARPU

    @Override
    public String toString() {
        return "ProductEarn{" +
                "newDay=" + newDay +
                ", appId='" + appId + '\'' +
                ", enterCount=" + enterCount +
                ", payUserCount=" + payUserCount +
                ", onlineAvg='" + onlineAvg + '\'' +
                ", adRevenue=" + adRevenue +
                ", inRevenue=" + inRevenue +
                ", inArpu=" + inArpu +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEarn that = (ProductEarn) o;
        return Objects.equals(newDay, that.newDay) &&
                Objects.equals(appId, that.appId) &&
                Objects.equals(enterCount, that.enterCount) &&
                Objects.equals(payUserCount, that.payUserCount) &&
                Objects.equals(onlineAvg, that.onlineAvg) &&
                Objects.equals(adRevenue, that.adRevenue) &&
                Objects.equals(inRevenue, that.inRevenue) &&
                Objects.equals(inArpu, that.inArpu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newDay, appId, enterCount, payUserCount, onlineAvg, adRevenue, inRevenue, inArpu);
    }

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
