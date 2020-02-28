package com.code.dao.entity.work;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 */
@Entity(name = "work_turntable")
public class WorkTurntable
{
    /**
     * id
     */
    @PrimaryKey
    @Column(name = "id")
    private int id;

    /**
     * 表位置
     */
    @Column(name = "local")
    private String local;

    /**
     * 产品id
     */
    @Column(name = "productId")
    private int productId;

    /**
     * 奖励类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 数量
     */
    @Column(name = "num")
    private int num;

    /**
     * icon
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 概率
     */
    @Column(name = "rate")
    private int rate;

    /**
     * 状态
     */
    @Column(name = "state")
    private boolean state;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getRate()
    {
        return rate;
    }

    public void setRate(int rate)
    {
        this.rate = rate;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }
}
