package com.code.dao.sdk;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity(name = "record_pay")
public class RecordPay
{
    @PrimaryKey
    @Column(name = "orderId")
    private String orderId;
    @Column(name = "userId")
    private int userId;
    @Column(name = "goodsId")
    private int goodsId;
    @Column(name = "payType")
    private String payType;
    @Column(name = "payOrder")
    private String payOrder;
    @Column(name = "payError")
    private String payError;
    @Column(name = "platform")
    private String platform;
    @Column(name = "money")
    private BigDecimal money;
    @Column(name = "state")
    private boolean state;
    @Column(name = "transTime")
    private java.sql.Timestamp transTime;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }


    public int getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(int goodsId)
    {
        this.goodsId = goodsId;
    }


    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }


    public String getPayOrder()
    {
        return payOrder;
    }

    public void setPayOrder(String payOrder)
    {
        this.payOrder = payOrder;
    }


    public String getPayError()
    {
        return payError;
    }

    public void setPayError(String payError)
    {
        this.payError = payError;
    }


    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }


    public BigDecimal getMoney()
    {
        return money;
    }

    public void setMoney(BigDecimal money)
    {
        this.money = money;
    }


    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }


    public java.sql.Timestamp getTransTime()
    {
        return transTime;
    }

    public void setTransTime(java.sql.Timestamp transTime)
    {
        this.transTime = transTime;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

}
