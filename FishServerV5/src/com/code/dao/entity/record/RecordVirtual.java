package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_virtual")
public class RecordVirtual
{
    @PrimaryKey
    @Column(name = "id")
    private String id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "goodsId")
    private int goodsId;
    @Column(name = "firstSave")
    private boolean firstSave;
    @Column(name = "balance")
    private int balance;
    @Column(name = "historyBalance")
    private int historyBalance;
    @Column(name = "saveSum")
    private int saveSum;
    @Column(name = "virtualValue")
    private int virtualValue;
    @Column(name = "success")
    private boolean success;
    @Column(name = "wealthType")
    private String wealthType;
    @Column(name = "price")
    private int price;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
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


    public boolean getFirstSave()
    {
        return firstSave;
    }

    public void setFirstSave(boolean firstSave)
    {
        this.firstSave = firstSave;
    }


    public int getBalance()
    {
        return balance;
    }

    public void setBalance(int balance)
    {
        this.balance = balance;
    }


    public int getHistoryBalance()
    {
        return historyBalance;
    }

    public void setHistoryBalance(int historyBalance)
    {
        this.historyBalance = historyBalance;
    }


    public int getSaveSum()
    {
        return saveSum;
    }

    public void setSaveSum(int saveSum)
    {
        this.saveSum = saveSum;
    }


    public int getVirtualValue()
    {
        return virtualValue;
    }

    public void setVirtualValue(int virtualValue)
    {
        this.virtualValue = virtualValue;
    }


    public boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getWealthType()
    {
        return wealthType;
    }

    public void setWealthType(String wealthType)
    {
        this.wealthType = wealthType;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

    public int getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(int goodsId)
    {
        this.goodsId = goodsId;
    }
}
