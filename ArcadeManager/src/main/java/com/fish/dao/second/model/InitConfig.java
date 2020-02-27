package com.fish.dao.second.model;

import java.util.Date;

public class InitConfig {
    private Long id;

    private String ddversion;

    private String ddappid;

    private Boolean ddjokelogo;

    private String ddgameurl;

    private Integer ddcode;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDdversion() {
        return ddversion;
    }

    public void setDdversion(String ddversion) {
        this.ddversion = ddversion == null ? null : ddversion.trim();
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public Boolean getDdjokelogo() {
        return ddjokelogo;
    }

    public void setDdjokelogo(Boolean ddjokelogo) {
        this.ddjokelogo = ddjokelogo;
    }

    public String getDdgameurl() {
        return ddgameurl;
    }

    public void setDdgameurl(String ddgameurl) {
        this.ddgameurl = ddgameurl == null ? null : ddgameurl.trim();
    }

    public Integer getDdcode() {
        return ddcode;
    }

    public void setDdcode(Integer ddcode) {
        this.ddcode = ddcode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddversion=").append(ddversion);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddjokelogo=").append(ddjokelogo);
        sb.append(", ddgameurl=").append(ddgameurl);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}