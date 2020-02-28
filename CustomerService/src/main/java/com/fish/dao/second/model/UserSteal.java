package com.fish.dao.second.model;

public class UserSteal extends UserStealKey {
    private Long stealuserid;

    private Long updatetime;

    public Long getStealuserid() {
        return stealuserid;
    }

    public void setStealuserid(Long stealuserid) {
        this.stealuserid = stealuserid;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", stealuserid=").append(stealuserid);
        sb.append(", updatetime=").append(updatetime);
        sb.append("]");
        return sb.toString();
    }
}