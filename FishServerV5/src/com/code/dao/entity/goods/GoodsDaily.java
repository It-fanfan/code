package com.code.dao.entity.goods;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "goods_daily")
public class GoodsDaily
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "state")
    private boolean state;
    @Column(name = "goodsName")
    private String goodsName;
    @Column(name = "icon")
    private String icon;
    @Column(name = "labelUrl")
    private String labelUrl;
    @Column(name = "costType")
    private String costType;
    @Column(name = "price")
    private int price;
    @Column(name = "startTime")
    private java.sql.Timestamp startTime;
    @Column(name = "endTime")
    private java.sql.Timestamp endTime;
    @Column(name = "goodsType")
    private String goodsType;
    @Column(name = "productId")
    private int productId;
    @Column(name = "productNum")
    private int productNum;
    @Column(name = "discount")
    private int discount;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }


    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }


    public String getLabelUrl()
    {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl)
    {
        this.labelUrl = labelUrl;
    }


    public String getCostType()
    {
        return costType;
    }

    public void setCostType(String costType)
    {
        this.costType = costType;
    }


    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }


    public java.sql.Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(java.sql.Timestamp startTime)
    {
        this.startTime = startTime;
    }


    public java.sql.Timestamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime(java.sql.Timestamp endTime)
    {
        this.endTime = endTime;
    }


    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }


    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }


    public int getProductNum()
    {
        return productNum;
    }

    public void setProductNum(int productNum)
    {
        this.productNum = productNum;
    }


    public int getDiscount()
    {
        return discount;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }

}
