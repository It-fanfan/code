package com.code.dao.entity.achievement;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_achievement_type")
public class ConfigAchievementType
{

    @PrimaryKey
    @Column(name = "typeName")
    private String typeName;
    @Column(name = "describe")
    private String describe;
    @Column(name = "icon")
    private String icon;
    @Column(name = "info")
    private String info;

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }


    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

}
