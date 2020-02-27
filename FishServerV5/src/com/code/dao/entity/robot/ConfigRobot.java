package com.code.dao.entity.robot;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_robot")
public class ConfigRobot
{
    @PrimaryKey
    @Column(name = "robotId")
    private int robotId;
    @Column(name = "robotName")
    private String robotName;
    @Column(name = "icon")
    private String icon;


    public int getRobotId()
    {
        return robotId;
    }

    public void setRobotId(int robotId)
    {
        this.robotId = robotId;
    }


    public String getRobotName()
    {
        return robotName;
    }

    public void setRobotName(String robotName)
    {
        this.robotName = robotName;
    }


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }
}
