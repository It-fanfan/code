package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_buy")
public class RecordBuy
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "goodsType")
    private String goodsType;
    @Column(name = "goodsId")
    private int goodsId;
    @Column(name = "total")
    private int total;
    @Column(name = "cost")
    private long cost;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;
    @Column(name = "costType")
    private String costType;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }


    public int getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(int goodsId)
    {
        this.goodsId = goodsId;
    }


    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }


    public long getCost()
    {
        return cost;
    }

    public void setCost(long cost)
    {
        this.cost = cost;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }


    public String getCostType()
    {
        return costType;
    }

    public void setCostType(String costType)
    {
        this.costType = costType;
    }

}
