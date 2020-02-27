package com.fish.dao.second.model;

import java.util.Date;

public class WxConfig {
    private String ddappid;

    private String ddappsecret;

    private String ddmchid;

    private String ddp12password;

    private String ddkey;

    private String ddp12;

    private String ddtoken;

    private String ddaccesstoken;

    private Integer ddexpiresin;

    private Date ddtokentime;

    private String ddmediaid;

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdappsecret() {
        return ddappsecret;
    }

    public void setDdappsecret(String ddappsecret) {
        this.ddappsecret = ddappsecret == null ? null : ddappsecret.trim();
    }

    public String getDdmchid() {
        return ddmchid;
    }

    public void setDdmchid(String ddmchid) {
        this.ddmchid = ddmchid == null ? null : ddmchid.trim();
    }

    public String getDdp12password() {
        return ddp12password;
    }

    public void setDdp12password(String ddp12password) {
        this.ddp12password = ddp12password == null ? null : ddp12password.trim();
    }

    public String getDdkey() {
        return ddkey;
    }

    public void setDdkey(String ddkey) {
        this.ddkey = ddkey == null ? null : ddkey.trim();
    }

    public String getDdp12() {
        return ddp12;
    }

    public void setDdp12(String ddp12) {
        this.ddp12 = ddp12 == null ? null : ddp12.trim();
    }

    public String getDdtoken() {
        return ddtoken;
    }

    public void setDdtoken(String ddtoken) {
        this.ddtoken = ddtoken == null ? null : ddtoken.trim();
    }

    public String getDdaccesstoken() {
        return ddaccesstoken;
    }

    public void setDdaccesstoken(String ddaccesstoken) {
        this.ddaccesstoken = ddaccesstoken == null ? null : ddaccesstoken.trim();
    }

    public Integer getDdexpiresin() {
        return ddexpiresin;
    }

    public void setDdexpiresin(Integer ddexpiresin) {
        this.ddexpiresin = ddexpiresin;
    }

    public Date getDdtokentime() {
        return ddtokentime;
    }

    public void setDdtokentime(Date ddtokentime) {
        this.ddtokentime = ddtokentime;
    }

    public String getDdmediaid() {
        return ddmediaid;
    }

    public void setDdmediaid(String ddmediaid) {
        this.ddmediaid = ddmediaid == null ? null : ddmediaid.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddappsecret=").append(ddappsecret);
        sb.append(", ddmchid=").append(ddmchid);
        sb.append(", ddp12password=").append(ddp12password);
        sb.append(", ddkey=").append(ddkey);
        sb.append(", ddp12=").append(ddp12);
        sb.append(", ddtoken=").append(ddtoken);
        sb.append(", ddaccesstoken=").append(ddaccesstoken);
        sb.append(", ddexpiresin=").append(ddexpiresin);
        sb.append(", ddtokentime=").append(ddtokentime);
        sb.append(", ddmediaid=").append(ddmediaid);
        sb.append("]");
        return sb.toString();
    }
}