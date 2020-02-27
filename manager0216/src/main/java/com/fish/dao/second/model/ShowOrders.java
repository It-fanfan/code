package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ShowOrders
{
    private Orders orders;
    private String productName;

    private Integer productType;
    private String originName;

    private String goodsName;
    private String ddDesc;
    private String userName;


    private BigDecimal ordersPrice;
    private Integer ordersState;
    private String ordersUid;
    private String ordersId;
    private String ordersOder;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date ordersTime;


    public String getOrdersOder()
    {
        return ordersOder;
    }

    public void setOrdersOder(String ordersOder)
    {
        this.ordersOder = ordersOder;
    }

    public BigDecimal getOrdersPrice()
    {
        return ordersPrice;
    }

    public void setOrdersPrice(BigDecimal ordersPrice)
    {
        this.ordersPrice = ordersPrice;
    }

    public Integer getOrdersState()
    {
        return ordersState;
    }

    public void setOrdersState(Integer ordersState)
    {
        this.ordersState = ordersState;
    }

    public String getOrdersUid()
    {
        return ordersUid;
    }

    public void setOrdersUid(String ordersUid)
    {
        this.ordersUid = ordersUid;
    }

    public String getOrdersId()
    {
        return ordersId;
    }

    public void setOrdersId(String ordersId)
    {
        this.ordersId = ordersId;
    }

    public Date getOrdersTime()
    {
        return ordersTime;
    }

    public void setOrdersTime(Date ordersTime)
    {
        this.ordersTime = ordersTime;
    }

    public Integer getProductType()
    {
        return productType;
    }

    public void setProductType(Integer productType)
    {
        this.productType = productType;
    }

    public String getDdDesc()
    {
        return ddDesc;
    }

    public void setDdDesc(String ddDesc)
    {
        this.ddDesc = ddDesc;
    }

    public Orders getOrders()
    {
        return orders;
    }

    public void setOrders(Orders orders)
    {
        this.orders = orders;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getOriginName()
    {
        return originName;
    }

    public void setOriginName(String originName)
    {
        this.originName = originName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}