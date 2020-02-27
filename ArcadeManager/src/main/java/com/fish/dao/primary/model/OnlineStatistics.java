package com.fish.dao.primary.model;

public class OnlineStatistics {
    private Integer id;

    private Long ddonlinecount;

    private Long ddgameroomcount;

    private Long ddremainroomcount;

    private String ddgamedistribution;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDdonlinecount() {
        return ddonlinecount;
    }

    public void setDdonlinecount(Long ddonlinecount) {
        this.ddonlinecount = ddonlinecount;
    }

    public Long getDdgameroomcount() {
        return ddgameroomcount;
    }

    public void setDdgameroomcount(Long ddgameroomcount) {
        this.ddgameroomcount = ddgameroomcount;
    }

    public Long getDdremainroomcount() {
        return ddremainroomcount;
    }

    public void setDdremainroomcount(Long ddremainroomcount) {
        this.ddremainroomcount = ddremainroomcount;
    }

    public String getDdgamedistribution() {
        return ddgamedistribution;
    }

    public void setDdgamedistribution(String ddgamedistribution) {
        this.ddgamedistribution = ddgamedistribution == null ? null : ddgamedistribution.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddonlinecount=").append(ddonlinecount);
        sb.append(", ddgameroomcount=").append(ddgameroomcount);
        sb.append(", ddremainroomcount=").append(ddremainroomcount);
        sb.append(", ddgamedistribution=").append(ddgamedistribution);
        sb.append("]");
        return sb.toString();
    }
}