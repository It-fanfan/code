package com.code.dao.entity.achievement;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_prop")
public class ConfigProp
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    private boolean status;
    @Column(name = "icon")
    private String icon;
    @Column(name = "describe")
    private String describe;
    @Column(name = "scene")
    private String scene;
    @ReadOnly
    @Column(name = "achievementType")
    private String achievementType;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }


    public String getAchievementType()
    {
        return achievementType;
    }

    public void setAchievementType(String achievementType)
    {
        this.achievementType = achievementType;
    }

    public String getScene()
    {
        return scene;
    }

    public void setScene(String scene)
    {
        this.scene = scene;
    }

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }
}
