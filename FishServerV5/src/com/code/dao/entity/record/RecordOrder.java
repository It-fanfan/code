package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_order")
public class RecordOrder
{
    @PrimaryKey
    @Column(name = "orderId")
    private long orderId;
    @Column(name = "userId")
    private long userId;
    @Column(name = "price")
    private long price;
    @Column(name = "status")
    private boolean status;
    @Column(name = "finishTime")
    private java.sql.Timestamp finishTime;


    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getPrice()
    {
        return price;
    }

    public void setPrice(long price)
    {
        this.price = price;
    }


    public boolean getStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }


    public java.sql.Timestamp getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(java.sql.Timestamp finishTime)
    {
        this.finishTime = finishTime;
    }

}
