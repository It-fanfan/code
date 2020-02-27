package com.fish.dao.primary.model;

import java.util.List;

public class ArcadeGameSet {
    private Integer id;

    private Integer ddcode;

    private Boolean ddstate;

    private String ddappid;

    private String ddname128u;

    private String ddarrange512a;

    private String ddcontent512a;

    private String dddesc512u;

    private List<String> gameBox;

    public List<String> getGameBox() {
        return gameBox;
    }

    public void setGameBox(List<String> gameBox) {
        this.gameBox = gameBox;
    }

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

    public Boolean getDdstate() {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate) {
        this.ddstate = ddstate;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdname128u() {
        return ddname128u;
    }

    public void setDdname128u(String ddname128u) {
        this.ddname128u = ddname128u == null ? null : ddname128u.trim();
    }

    public String getDdarrange512a() {
        return ddarrange512a;
    }

    public void setDdarrange512a(String ddarrange512a) {
        this.ddarrange512a = ddarrange512a == null ? null : ddarrange512a.trim();
    }

    public String getDdcontent512a() {
        return ddcontent512a;
    }

    public void setDdcontent512a(String ddcontent512a) {
        this.ddcontent512a = ddcontent512a == null ? null : ddcontent512a.trim();
    }

    public String getDddesc512u() {
        return dddesc512u;
    }

    public void setDddesc512u(String dddesc512u) {
        this.dddesc512u = dddesc512u == null ? null : dddesc512u.trim();
    }

    @Override
    public String toString() {
        return "ArcadeGameSet{" +
                "id=" + id +
                ", ddcode=" + ddcode +
                ", ddstate=" + ddstate +
                ", ddappid='" + ddappid + '\'' +
                ", ddname128u='" + ddname128u + '\'' +
                ", ddarrange512a='" + ddarrange512a + '\'' +
                ", ddcontent512a='" + ddcontent512a + '\'' +
                ", dddesc512u='" + dddesc512u + '\'' +
                ", gameBox =" + gameBox +
                '}';
    }
}