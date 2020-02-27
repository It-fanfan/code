package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductRemain {
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date newDay;//日期
    private String appId;//appId
    private Integer newUserCount;//新增用户数
    private Integer acUserCount;//活跃用户数
    private Integer remain1;//1日留存
    private Integer remain3;//3日留存

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
}