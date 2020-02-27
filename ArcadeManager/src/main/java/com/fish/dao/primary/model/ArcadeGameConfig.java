package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
public class ArcadeGameConfig {
    private Long id;
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date configDate;

    private String productName;

    private String orName;

    private String programType;

    private String gameType;

    private String appId;

    private String session;

    private String awardShow;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    public Date getConfigDate() {
        return configDate;
    }
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    public void setConfigDate(Date configDate) {
        this.configDate = configDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getOrName() {
        return orName;
    }

    public void setOrName(String orName) {
        this.orName = orName == null ? null : orName.trim();
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType == null ? null : programType.trim();
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType == null ? null : gameType.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session == null ? null : session.trim();
    }

    public String getAwardShow() {
        return awardShow;
    }

    public void setAwardShow(String awardShow) {
        this.awardShow = awardShow == null ? null : awardShow.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configDate=").append(configDate);
        sb.append(", productName=").append(productName);
        sb.append(", orName=").append(orName);
        sb.append(", programType=").append(programType);
        sb.append(", gameType=").append(gameType);
        sb.append(", appId=").append(appId);
        sb.append(", session=").append(session);
        sb.append(", awardShow=").append(awardShow);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}