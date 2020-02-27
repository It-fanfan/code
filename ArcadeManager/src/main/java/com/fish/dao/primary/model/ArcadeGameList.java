package com.fish.dao.primary.model;

import java.util.Date;

public class ArcadeGameList {
    private Long id;

    private Boolean status;

    private String gameCode;

    private String gameName;

    private Long singalGold;

    private Long teamGlod;

    private String playerLimit;

    private Long roleNumber;

    private String keyScheme;

    private String engineScheme;

    private String screenScheme;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode == null ? null : gameCode.trim();
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    public Long getSingalGold() {
        return singalGold;
    }

    public void setSingalGold(Long singalGold) {
        this.singalGold = singalGold;
    }

    public Long getTeamGlod() {
        return teamGlod;
    }

    public void setTeamGlod(Long teamGlod) {
        this.teamGlod = teamGlod;
    }

    public String getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(String playerLimit) {
        this.playerLimit = playerLimit == null ? null : playerLimit.trim();
    }

    public Long getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(Long roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getKeyScheme() {
        return keyScheme;
    }

    public void setKeyScheme(String keyScheme) {
        this.keyScheme = keyScheme == null ? null : keyScheme.trim();
    }

    public String getEngineScheme() {
        return engineScheme;
    }

    public void setEngineScheme(String engineScheme) {
        this.engineScheme = engineScheme == null ? null : engineScheme.trim();
    }

    public String getScreenScheme() {
        return screenScheme;
    }

    public void setScreenScheme(String screenScheme) {
        this.screenScheme = screenScheme == null ? null : screenScheme.trim();
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
        sb.append(", status=").append(status);
        sb.append(", gameCode=").append(gameCode);
        sb.append(", gameName=").append(gameName);
        sb.append(", singalGold=").append(singalGold);
        sb.append(", teamGlod=").append(teamGlod);
        sb.append(", playerLimit=").append(playerLimit);
        sb.append(", roleNumber=").append(roleNumber);
        sb.append(", keyScheme=").append(keyScheme);
        sb.append(", engineScheme=").append(engineScheme);
        sb.append(", screenScheme=").append(screenScheme);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}