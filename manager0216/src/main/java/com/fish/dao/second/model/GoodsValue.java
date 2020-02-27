package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsValue
{
    private String coinNumber;
    private String headNumber;
    private String cashNumber;
    private String costDesc;
    private String gainDesc;

    private Integer ddid;
    private Boolean ddstate;

    private Boolean ddfrist;
    private String ddname;
    private String dddesc;
    private String ddcosttype;
    private BigDecimal ddprice;
    private String ddgoodstype;
    private Integer ddvalue;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inserttime;

    public Boolean getDdfrist() {
        return ddfrist;
    }

    public void setDdfrist(Boolean ddfrist) {
        this.ddfrist = ddfrist;
    }

    public String getGainDesc() {
        return gainDesc;
    }

    public void setGainDesc(String gainDesc) {
        this.gainDesc = gainDesc;
    }

    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }
    public String getCoinNumber()
    {
        return coinNumber;
    }

    public void setCoinNumber(String coinNumber)
    {
        this.coinNumber = coinNumber;
    }

    public String getHeadNumber()
    {
        return headNumber;
    }

    public void setHeadNumber(String headNumber)
    {
        this.headNumber = headNumber;
    }

    public String getCashNumber()
    {
        return cashNumber;
    }

    public void setCashNumber(String cashNumber)
    {
        this.cashNumber = cashNumber;
    }

    public Integer getDdid()
    {
        return ddid;
    }

    public void setDdid(Integer ddid)
    {
        this.ddid = ddid;
    }

    public Boolean getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate)
    {
        this.ddstate = ddstate;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public String getDddesc()
    {
        return dddesc;
    }

    public void setDddesc(String dddesc)
    {
        this.dddesc = dddesc == null ? null : dddesc.trim();
    }

    public String getDdcosttype()
    {
        return ddcosttype;
    }

    public void setDdcosttype(String ddcosttype)
    {
        this.ddcosttype = ddcosttype == null ? null : ddcosttype.trim();
    }

    public BigDecimal getDdprice()
    {
        return ddprice;
    }

    public void setDdprice(BigDecimal ddprice)
    {
        this.ddprice = ddprice;
    }

    public String getDdgoodstype()
    {
        return ddgoodstype;
    }

    public void setDdgoodstype(String ddgoodstype)
    {
        this.ddgoodstype = ddgoodstype == null ? null : ddgoodstype.trim();
    }

    public Integer getDdvalue()
    {
        return ddvalue;
    }

    public void setDdvalue(Integer ddvalue)
    {
        this.ddvalue = ddvalue;
    }

    public Date getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Date inserttime)
    {
        this.inserttime = inserttime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddid=").append(ddid);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddname=").append(ddname);
        sb.append(", dddesc=").append(dddesc);
        sb.append(", ddcosttype=").append(ddcosttype);
        sb.append(", ddprice=").append(ddprice);
        sb.append(", ddgoodstype=").append(ddgoodstype);
        sb.append(", ddvalue=").append(ddvalue);
        sb.append(", inserttime=").append(inserttime);
        sb.append("]");
        return sb.toString();
    }
}