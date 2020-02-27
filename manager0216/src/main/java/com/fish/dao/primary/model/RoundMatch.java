package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/**
 * @author
 * @pragram: RoundMatch
 * @description: round_match实体类
 * @create:
 */
public class RoundMatch {
    private Integer ddcode;

    private String ddname;

    private Boolean ddstate;

    private Integer ddgame;

    private String ddappid;

    private String ddround;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date ddstart;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date ddend;

    private String ddres;

    private String ddmatchversion;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtime;

    /* 赛制时长 */
    private String roundLength;
    /* 产品名称 */
    private String appName;
    //界面选择产品代号
    private String appNameSelect;
    //界面选择赛制代号
    private String roundSelect;
    //界面选择游戏代号
    private String gameCodeSelect;
    //赛制奖励
    private String ddreward;
    //游戏名称
    private String gameName;
    //赛场名称
    private String roundName;
    //图片跳转
    private String jumpDirect;

    private String gamePicture0;
    private String gamePicture1;

    public String getGamePicture0() {
        return gamePicture0;
    }

    public void setGamePicture0(String gamePicture0) {
        this.gamePicture0 = gamePicture0;
    }

    public String getGamePicture1() {
        return gamePicture1;
    }

    public void setGamePicture1(String gamePicture1) {
        this.gamePicture1 = gamePicture1;
    }

    public String getRoundLength() {
        return roundLength;
    }

    public void setRoundLength(String roundLength) {
        this.roundLength = roundLength;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppNameSelect() {
        return appNameSelect;
    }

    public void setAppNameSelect(String appNameSelect) {
        this.appNameSelect = appNameSelect;
    }

    public String getRoundSelect() {
        return roundSelect;
    }

    public void setRoundSelect(String roundSelect) {
        this.roundSelect = roundSelect;
    }

    public String getGameCodeSelect() {
        return gameCodeSelect;
    }

    public void setGameCodeSelect(String gameCodeSelect) {
        this.gameCodeSelect = gameCodeSelect;
    }

    public String getDdreward() {
        return ddreward;
    }

    public void setDdreward(String ddreward) {
        this.ddreward = ddreward;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getJumpDirect() {
        return jumpDirect;
    }

    public void setJumpDirect(String jumpDirect) {
        this.jumpDirect = jumpDirect;
    }

    public Integer getDdcode() {
        return ddcode;
    }

    public void setDdcode(Integer ddcode) {
        this.ddcode = ddcode;
    }

    public String getDdname() {
        return ddname;
    }

    public void setDdname(String ddname) {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public Boolean getDdstate() {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate) {
        this.ddstate = ddstate;
    }

    public Integer getDdgame() {
        return ddgame;
    }

    public void setDdgame(Integer ddgame) {
        this.ddgame = ddgame;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdround() {
        return ddround;
    }

    public void setDdround(String ddround) {
        this.ddround = ddround == null ? null : ddround.trim();
    }

    public Date getDdstart() {
        return ddstart;
    }

    public void setDdstart(Date ddstart) {
        this.ddstart = ddstart;
    }

    public Date getDdend() {
        return ddend;
    }

    public void setDdend(Date ddend) {
        this.ddend = ddend;
    }

    public String getDdres() {
        return ddres;
    }

    public void setDdres(String ddres) {
        this.ddres = ddres == null ? null : ddres.trim();
    }

    public String getDdmatchversion() {
        return ddmatchversion;
    }

    public void setDdmatchversion(String ddmatchversion) {
        this.ddmatchversion = ddmatchversion == null ? null : ddmatchversion.trim();
    }

    public Date getDdtime() {
        return ddtime;
    }

    public void setDdtime(Date ddtime) {
        this.ddtime = ddtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddname=").append(ddname);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddgame=").append(ddgame);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddround=").append(ddround);
        sb.append(", ddstart=").append(ddstart);
        sb.append(", ddend=").append(ddend);
        sb.append(", ddres=").append(ddres);
        sb.append(", ddmatchversion=").append(ddmatchversion);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}