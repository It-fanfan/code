package com.fish.dao.second.model;

public class OperateUserInfo {
    private Long id;

    private String ddappid;

    private String ddusername;

    private String ddpassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdusername() {
        return ddusername;
    }

    public void setDdusername(String ddusername) {
        this.ddusername = ddusername == null ? null : ddusername.trim();
    }

    public String getDdpassword() {
        return ddpassword;
    }

    public void setDdpassword(String ddpassword) {
        this.ddpassword = ddpassword == null ? null : ddpassword.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddusername=").append(ddusername);
        sb.append(", ddpassword=").append(ddpassword);
        sb.append("]");
        return sb.toString();
    }
}