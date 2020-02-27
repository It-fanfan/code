package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Date;

@Entity(name = "config_notice")
public class ConfigNotice
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @ReadOnly
    @Column(name = "state")
    private boolean state;
    @Column(name = "subIndex")
    private int subIndex;
    @Column(name = "jumpInfo")
    private String jumpInfo;
    @Column(name = "image")
    private String image;
    @Column(name = "title")
    private String title;

    @ReadOnly
    @Column(name = "startTime")
    private Date startTime;
    @ReadOnly
    @Column(name = "endTime")
    private Date endTime;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public boolean getState()
    {
        java.util.Date now = new java.util.Date();
        if (getStartTime().after(now) || getEndTime().before(now))
            return false;
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }


    public int getSubIndex()
    {
        return subIndex;
    }

    public void setSubIndex(int subIndex)
    {
        this.subIndex = subIndex;
    }


    public String getJumpInfo()
    {
        return jumpInfo;
    }

    public void setJumpInfo(String jumpInfo)
    {
        this.jumpInfo = jumpInfo;
    }


    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
}
