package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_props")
public class UserProps
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @PrimaryKey
    @Column(name = "local")
    private String local;
    @PrimaryKey
    @Column(name = "productId")
    private int productId;
    @Column(name = "total")
    private int total;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public String getLocal()
    {
        return local;
    }

    public void setLocal(String local)
    {
        this.local = local;
    }


    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }


    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

    @Override
    public String toString()
    {
        return "UserProps{" + "userId=" + userId + ", local='" + local + '\'' + ", productId=" + productId + ", total=" + total + ", insertTime=" + insertTime + '}';
    }
}
