package com.fish.dao.primary.model;

import java.util.List;

public class ArcadeGames {
    private Integer id;

    private Integer ddcode;

    private String ddname128u;

    private Integer ddsinglecoin;

    private Integer ddmulticoin;

    private Integer ddmaxplayer;

    private Integer ddispk;

    private Integer ddavailable;

    private String ddtitle2048u;

    private String ddround1024a;

    private String ddrespath2048u;

    private Integer ddresversion;

    private Integer ddrescount;

    private Integer ddrolecount;

    private Integer ddrocker;

    private Integer ddengine;

    private Integer ddresolution;

    private String ddappid;

    private Integer ddautoselect;

    private String ddfriendurl;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDdcode() {
        return ddcode;
    }

    public void setDdcode(Integer ddcode) {
        this.ddcode = ddcode;
    }

    public String getDdname128u() {
        return ddname128u;
    }

    public void setDdname128u(String ddname128u) {
        this.ddname128u = ddname128u == null ? null : ddname128u.trim();
    }

    public Integer getDdsinglecoin() {
        return ddsinglecoin;
    }

    public void setDdsinglecoin(Integer ddsinglecoin) {
        this.ddsinglecoin = ddsinglecoin;
    }

    public Integer getDdmulticoin() {
        return ddmulticoin;
    }

    public void setDdmulticoin(Integer ddmulticoin) {
        this.ddmulticoin = ddmulticoin;
    }

    public Integer getDdmaxplayer() {
        return ddmaxplayer;
    }

    public void setDdmaxplayer(Integer ddmaxplayer) {
        this.ddmaxplayer = ddmaxplayer;
    }

    public Integer getDdispk() {
        return ddispk;
    }

    public void setDdispk(Integer ddispk) {
        this.ddispk = ddispk;
    }

    public Integer getDdavailable() {
        return ddavailable;
    }

    public void setDdavailable(Integer ddavailable) {
        this.ddavailable = ddavailable;
    }

    public String getDdtitle2048u() {
        return ddtitle2048u;
    }

    public void setDdtitle2048u(String ddtitle2048u) {
        this.ddtitle2048u = ddtitle2048u == null ? null : ddtitle2048u.trim();
    }

    public String getDdround1024a() {
        return ddround1024a;
    }

    public void setDdround1024a(String ddround1024a) {
        this.ddround1024a = ddround1024a == null ? null : ddround1024a.trim();
    }

    public String getDdrespath2048u() {
        return ddrespath2048u;
    }

    public void setDdrespath2048u(String ddrespath2048u) {
        this.ddrespath2048u = ddrespath2048u == null ? null : ddrespath2048u.trim();
    }

    public Integer getDdresversion() {
        return ddresversion;
    }

    public void setDdresversion(Integer ddresversion) {
        this.ddresversion = ddresversion;
    }

    public Integer getDdrescount() {
        return ddrescount;
    }

    public void setDdrescount(Integer ddrescount) {
        this.ddrescount = ddrescount;
    }

    public Integer getDdrolecount() {
        return ddrolecount;
    }

    public void setDdrolecount(Integer ddrolecount) {
        this.ddrolecount = ddrolecount;
    }

    public Integer getDdrocker() {
        return ddrocker;
    }

    public void setDdrocker(Integer ddrocker) {
        this.ddrocker = ddrocker;
    }

    public Integer getDdengine() {
        return ddengine;
    }

    public void setDdengine(Integer ddengine) {
        this.ddengine = ddengine;
    }

    public Integer getDdresolution() {
        return ddresolution;
    }

    public void setDdresolution(Integer ddresolution) {
        this.ddresolution = ddresolution;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public Integer getDdautoselect() {
        return ddautoselect;
    }

    public void setDdautoselect(Integer ddautoselect) {
        this.ddautoselect = ddautoselect;
    }

    public String getDdfriendurl() {
        return ddfriendurl;
    }

    public void setDdfriendurl(String ddfriendurl) {
        this.ddfriendurl = ddfriendurl == null ? null : ddfriendurl.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddname128u=").append(ddname128u);
        sb.append(", ddsinglecoin=").append(ddsinglecoin);
        sb.append(", ddmulticoin=").append(ddmulticoin);
        sb.append(", ddmaxplayer=").append(ddmaxplayer);
        sb.append(", ddispk=").append(ddispk);
        sb.append(", ddavailable=").append(ddavailable);
        sb.append(", ddtitle2048u=").append(ddtitle2048u);
        sb.append(", ddround1024a=").append(ddround1024a);
        sb.append(", ddrespath2048u=").append(ddrespath2048u);
        sb.append(", ddresversion=").append(ddresversion);
        sb.append(", ddrescount=").append(ddrescount);
        sb.append(", ddrolecount=").append(ddrolecount);
        sb.append(", ddrocker=").append(ddrocker);
        sb.append(", ddengine=").append(ddengine);
        sb.append(", ddresolution=").append(ddresolution);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddautoselect=").append(ddautoselect);
        sb.append(", ddfriendurl=").append(ddfriendurl);
        sb.append("]");
        return sb.toString();
    }
}