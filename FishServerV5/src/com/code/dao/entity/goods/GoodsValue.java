package com.code.dao.entity.goods;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 *
 */
@Entity(name = "goods_value")
public class GoodsValue
{
    /**
     * 商品编号
     */
    @PrimaryKey
    @Column(name = "id")
    private int id;

    /**
     * 状态
     */
    @Column(name = "state")
    private boolean state;

    /**
     * 商品名称
     */
    @Column(name = "goodsName")
    private String goodsName;

    /**
     * 图标地址
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 标签地址
     */
    @Column(name = "labelUrl")
    private String labelUrl;

    /**
     * 人名币，单位分
     */
    @Column(name = "rmb")
    private int rmb;

    /**
     * 虚拟币
     */
    @Column(name = "virtualValue")
    private int virtualValue;

    @Column(name = "wealthType")
    private String wealthType;

    /**
     * 价格数值
     */
    @Column(name = "total")
    private int total;

    /**
     * 开始时间
     */
    @Column(name = "startTime")
    private Timestamp startTime;

    /**
     * 截至时间
     */
    @Column(name = "endTime")
    private Timestamp endTime;


    @Column(name = "discount")
    private int discount;

    public int getDiscount()
    {
        return discount;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }

    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getGoodsName()
    {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getIcon()
    {
        return this.icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLabelUrl()
    {
        return this.labelUrl;
    }

    public void setLabelUrl(String labelUrl)
    {
        this.labelUrl = labelUrl;
    }

    public int getRmb()
    {
        return this.rmb;
    }

    public void setRmb(int rmb)
    {
        this.rmb = rmb;
    }


    public int getVirtualValue()
    {
        return this.virtualValue;
    }

    public void setVirtualValue(int virtualValue)
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

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public Timestamp getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public Timestamp getEndTime()
    {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime)
    {
        this.endTime = endTime;
    }

}
