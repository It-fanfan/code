package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Orders
{
    private String ddid;

    private String dduid;

    private Integer ddgid;

    private String ddtype;

    private String ddaccount;

    private String ddorder;

    private String dderror;

    private BigDecimal ddprice;

    private Integer ddstate;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ddtime;

    private Date ddtrans;

    private String ddoid;

    private String ddappid;

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

    public Integer getDdgid()
    {
        return ddgid;
    }

    public void setDdgid(Integer ddgid)
    {
        this.ddgid = ddgid;
    }

    public String getDdtype()
    {
        return ddtype;
    }

    public void setDdtype(String ddtype)
    {
        this.ddtype = ddtype == null ? null : ddtype.trim();
    }

    public String getDdaccount()
    {
        return ddaccount;
    }

    public void setDdaccount(String ddaccount)
    {
        this.ddaccount = ddaccount == null ? null : ddaccount.trim();
    }

    public String getDdorder()
    {
        return ddorder;
    }

    public void setDdorder(String ddorder)
    {
        this.ddorder = ddorder == null ? null : ddorder.trim();
    }

    public String getDderror()
    {
        return dderror;
    }

    public void setDderror(String dderror)
    {
        this.dderror = dderror == null ? null : dderror.trim();
    }

    public BigDecimal getDdprice()
    {
        return ddprice;
    }

    public void setDdprice(BigDecimal ddprice)
    {
        this.ddprice = ddprice;
    }

    public Integer getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Integer ddstate)
    {
        this.ddstate = ddstate;
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
    }

    public Date getDdtrans()
    {
        return ddtrans;
    }

    public void setDdtrans(Date ddtrans)
    {
        this.ddtrans = ddtrans;
    }

    public String getDdoid()
    {
        return ddoid;
    }

    public void setDdoid(String ddoid)
    {
        this.ddoid = ddoid == null ? null : ddoid.trim();
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
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
        sb.append(", ddgid=").append(ddgid);
        sb.append(", ddtype=").append(ddtype);
        sb.append(", ddaccount=").append(ddaccount);
        sb.append(", ddorder=").append(ddorder);
        sb.append(", dderror=").append(dderror);
        sb.append(", ddprice=").append(ddprice);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddtime=").append(ddtime);
        sb.append(", ddtrans=").append(ddtrans);
        sb.append(", ddoid=").append(ddoid);
        sb.append(", ddappid=").append(ddappid);
        sb.append("]");
        return sb.toString();
    }
}