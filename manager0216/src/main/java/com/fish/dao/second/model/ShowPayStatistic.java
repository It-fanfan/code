package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ShowPayStatistic
{

    private String productName;
    private Integer productType;

    private Integer payMoney;
    private Integer payUsers;
    private String payUp;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ddtrans;

    private String ddoid;

    private String ddappid;

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getDduid() {
        return dduid;
    }

    public void setDduid(String dduid) {
        this.dduid = dduid;
    }

    public Integer getDdgid() {
        return ddgid;
    }

    public void setDdgid(Integer ddgid) {
        this.ddgid = ddgid;
    }

    public String getDdtype() {
        return ddtype;
    }

    public void setDdtype(String ddtype) {
        this.ddtype = ddtype;
    }

    public String getDdaccount() {
        return ddaccount;
    }

    public void setDdaccount(String ddaccount) {
        this.ddaccount = ddaccount;
    }

    public String getDdorder() {
        return ddorder;
    }

    public void setDdorder(String ddorder) {
        this.ddorder = ddorder;
    }

    public String getDderror() {
        return dderror;
    }

    public void setDderror(String dderror) {
        this.dderror = dderror;
    }

    public BigDecimal getDdprice() {
        return ddprice;
    }

    public void setDdprice(BigDecimal ddprice) {
        this.ddprice = ddprice;
    }

    public Integer getDdstate() {
        return ddstate;
    }

    public void setDdstate(Integer ddstate) {
        this.ddstate = ddstate;
    }

    public Date getDdtime() {
        return ddtime;
    }

    public void setDdtime(Date ddtime) {
        this.ddtime = ddtime;
    }

    public String getDdoid() {
        return ddoid;
    }

    public void setDdoid(String ddoid) {
        this.ddoid = ddoid;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getPayUsers() {
        return payUsers;
    }

    public void setPayUsers(Integer payUsers) {
        this.payUsers = payUsers;
    }

    public String getPayUp() {
        return payUp;
    }

    public void setPayUp(String payUp) {
        this.payUp = payUp;
    }

    public Date getDdtrans() {
        return ddtrans;
    }

    public void setDdtrans(Date ddtrans) {
        this.ddtrans = ddtrans;
    }

    public Integer getProductType()
    {
        return productType;
    }

    public void setProductType(Integer productType)
    {
        this.productType = productType;
    }




    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

}