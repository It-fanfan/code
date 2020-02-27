package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Field;


@Entity(name = "config_fish")
public class ConfigFish
{
    @PrimaryKey
    @Column(name = "ftId")
    private int ftId;
    @Column(name = "typeName")
    private String typeName;
    @Column(name = "basin")
    private int basin;
    @Column(name = "figure")
    private int figure;
    @Column(name = "lightExpend")
    private int lightExpend;
    @Column(name = "allowLimit")
    private int allowLimit;
    @Column(name = "description")
    private String description;
    @Column(name = "icon")
    private String icon;
    @Column(name = "skelName")
    private String skelName;
    @Column(name = "skin")
    private String skin;
    @Column(name = "animation")
    private String animation;

    public static Field[] getInitField() throws NoSuchFieldException
    {
        return new Field[]{
                ConfigFish.class.getDeclaredField("ftId"),
                ConfigFish.class.getDeclaredField("lightExpend"),
                ConfigFish.class.getDeclaredField("allowLimit")
        };
    }

    public int getFtId()
    {
        return ftId;
    }

    public void setFtId(int ftId)
    {
        this.ftId = ftId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getSkelName()
    {
        return skelName;
    }

    public void setSkelName(String skelName)
    {
        this.skelName = skelName;
    }

    public int getBasin()
    {
        return basin;
    }

    public void setBasin(int basin)
    {
        this.basin = basin;
    }

    public int getFigure()
    {
        return figure;
    }

    public void setFigure(int figure)
    {
        this.figure = figure;
    }

    public int getLightExpend()
    {
        return lightExpend;
    }

    public void setLightExpend(int lightExpend)
    {
        this.lightExpend = lightExpend;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getAllowLimit()
    {
        return allowLimit;
    }

    public void setAllowLimit(int allowLimit)
    {
        this.allowLimit = allowLimit;
    }

    public String getSkin()
    {
        return skin;
    }

    public void setSkin(String skin)
    {
        this.skin = skin;
    }

    public String getAnimation()
    {
        return animation;
    }

    public void setAnimation(String animation)
    {
        this.animation = animation;
    }
}
