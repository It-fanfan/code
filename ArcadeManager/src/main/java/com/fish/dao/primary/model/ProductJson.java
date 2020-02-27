package com.fish.dao.primary.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Component
public class ProductJson {
    //进入时间
    private Timestamp times;
    //Appid
    private String appId;
    //userId
    private String userId;
    //参数类型：register,play,pay
    private String kind;
    //收益
    private BigDecimal cost;

    public Timestamp getTimes() {
        return times;
    }

    public void setTimes(Timestamp times) {
        this.times = times;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ProductJson{" +
                "times='" + times + '\'' +
                ", appId='" + appId + '\'' +
                ", userId='" + userId + '\'' +
                ", kind='" + kind + '\'' +
                ", cost=" + cost +
                '}';
    }


}
