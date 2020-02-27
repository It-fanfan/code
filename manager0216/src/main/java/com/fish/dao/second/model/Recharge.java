package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Recharge
{

    private BigDecimal ddrmbed;//已提现金额
    private Integer remainAmount;//剩余金额
    private String productName;//产品名称
    private Integer programType;
    private String ddRechargeOpenId;
    private String ddRechargeAppId;
    private String userName;//玩家昵称
    private String ddRechargeUid;

    private String ddopenid;

    private String ddid;

    private String dduid;

    private String ddappid;

    private BigDecimal ddrmb;//提现金额

    private String ddtip;

    private Integer ddstatus;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtrans;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtimes;


    public BigDecimal getDdrmbed()
    {
        return ddrmbed;
    }

    public void setDdrmbed(BigDecimal ddrmbed)
    {
        this.ddrmbed = ddrmbed;
    }

    public Integer getRemainAmount()
    {
        return remainAmount;
    }

    public void setRemainAmount(Integer remainAmount)
    {
        this.remainAmount = remainAmount;
    }

    public String getDdRechargeUid()
    {
        return ddRechargeUid;
    }

    public void setDdRechargeUid(String ddRechargeUid)
    {
        this.ddRechargeUid = ddRechargeUid;
    }

    public String getDdRechargeOpenId()
    {
        return ddRechargeOpenId;
    }

    public void setDdRechargeOpenId(String ddRechargeOpenId)
    {
        this.ddRechargeOpenId = ddRechargeOpenId;
    }

    public String getDdRechargeAppId()
    {
        return ddRechargeAppId;
    }

    public void setDdRechargeAppId(String ddRechargeAppId)
    {
        this.ddRechargeAppId = ddRechargeAppId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getProgramType()
    {
        return programType;
    }

    public void setProgramType(Integer programType)
    {
        this.programType = programType;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDdopenid()
    {
        return ddopenid;
    }

    public void setDdopenid(String ddopenid)
    {
        this.ddopenid = ddopenid;
    }

    public String getDdid()
    {
        return ddid;
    }

    public void setDdid(String ddid)
    {
        this.ddid = ddid == null ? null : ddid.trim();
    }

    public String getDduid()
    {
        return dduid;
    }

    public void setDduid(String dduid)
    {
        this.dduid = dduid == null ? null : dduid.trim();
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public BigDecimal getDdrmb()
    {
        return ddrmb;
    }

    public void setDdrmb(BigDecimal ddrmb)
    {
        this.ddrmb = ddrmb;
    }

    public String getDdtip()
    {
        return ddtip;
    }

    public void setDdtip(String ddtip)
    {
        this.ddtip = ddtip == null ? null : ddtip.trim();
    }

    public Integer getDdstatus()
    {
        return ddstatus;
    }

    public void setDdstatus(Integer ddstatus)
    {
        this.ddstatus = ddstatus;
    }

    public Date getDdtrans()
    {
        return ddtrans;
    }

    public void setDdtrans(Date ddtrans)
    {
        this.ddtrans = ddtrans;
    }

    public Date getDdtimes()
    {
        return ddtimes;
    }

    public void setDdtimes(Date ddtimes)
    {
        this.ddtimes = ddtimes;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddid=").append(ddid);
        sb.append(", dduid=").append(dduid);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddrmb=").append(ddrmb);
        sb.append(", ddtip=").append(ddtip);
        sb.append(", ddstatus=").append(ddstatus);
        sb.append(", ddtrans=").append(ddtrans);
        sb.append(", ddtimes=").append(ddtimes);
        sb.append("]");
        return sb.toString();
    }


}