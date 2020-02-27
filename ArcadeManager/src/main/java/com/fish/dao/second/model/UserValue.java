package com.fish.dao.second.model;

import java.util.Date;

public class UserValue {
    private String dduid;

    private Integer ddawardmoney;

    private Integer ddawardcoin;

    private Integer ddcoincount;

    private Integer ddmoney;

    private Integer ddtotalpaymoney;

    private Date ddlogintime;

    public String getDduid() {
        return dduid;
    }

    public void setDduid(String dduid) {
        this.dduid = dduid == null ? null : dduid.trim();
    }

    public Integer getDdawardmoney() {
        return ddawardmoney;
    }

    public void setDdawardmoney(Integer ddawardmoney) {
        this.ddawardmoney = ddawardmoney;
    }

    public Integer getDdawardcoin() {
        return ddawardcoin;
    }

    public void setDdawardcoin(Integer ddawardcoin) {
        this.ddawardcoin = ddawardcoin;
    }

    public Integer getDdcoincount() {
        return ddcoincount;
    }

    public void setDdcoincount(Integer ddcoincount) {
        this.ddcoincount = ddcoincount;
    }

    public Integer getDdmoney() {
        return ddmoney;
    }

    public void setDdmoney(Integer ddmoney) {
        this.ddmoney = ddmoney;
    }

    public Integer getDdtotalpaymoney() {
        return ddtotalpaymoney;
    }

    public void setDdtotalpaymoney(Integer ddtotalpaymoney) {
        this.ddtotalpaymoney = ddtotalpaymoney;
    }

    public Date getDdlogintime() {
        return ddlogintime;
    }

    public void setDdlogintime(Date ddlogintime) {
        this.ddlogintime = ddlogintime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dduid=").append(dduid);
        sb.append(", ddawardmoney=").append(ddawardmoney);
        sb.append(", ddawardcoin=").append(ddawardcoin);
        sb.append(", ddcoincount=").append(ddcoincount);
        sb.append(", ddmoney=").append(ddmoney);
        sb.append(", ddtotalpaymoney=").append(ddtotalpaymoney);
        sb.append(", ddlogintime=").append(ddlogintime);
        sb.append("]");
        return sb.toString();
    }
}