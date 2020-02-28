package com.fish.dao.second.model;

import java.sql.Timestamp;

public class GoodValueInfo
{
    private Integer id;
    private Integer state;
    private String goodsName;
    private String icon;
    private String labelUrl;
    private Integer rmb;
    private Integer firstSave;
    private Integer virtualValue;
    private String wealthType;
    private Integer total;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer discount;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
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

    public Integer getRmb()
    {
        return rmb;
    }

    public void setRmb(Integer rmb)
    {
        this.rmb = rmb;
    }

    public Integer getFirstSave()
    {
        return firstSave;
    }

    public void setFirstSave(Integer firstSave)
    {
        this.firstSave = firstSave;
    }

    public Integer getVirtualValue()
    {
        return virtualValue;
    }

    public void setVirtualValue(Integer virtualValue)
    {
        this.virtualValue = virtualValue;
    }

    public String getWealthType()
    {
        return wealthType;
    }

    public void setWealthType(String wealthType)
    {
        this.wealthType = wealthType;
    }

    public Integer getTotal()
    {
        return total;
    }

    public void setTotal(Integer total)
    {
        this.total = total;
    }

    public Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public Timestamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Timestamp endTime)
    {
        this.endTime = endTime;
    }

    public Integer getDiscount()
    {
        return discount;
    }

    public void setDiscount(Integer discount)
    {
        this.discount = discount;
    }
}
