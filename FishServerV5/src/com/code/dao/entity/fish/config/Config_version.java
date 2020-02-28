package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
public class Config_version
{
    @PrimaryKey
    @Column(name = "ver")
    public String ver;

    @Column(name = "status")
    public boolean status;

    @Column(name = "url")
    public String url;

    @Column(name = "banner")
    public String banner;

    @Column(name = "md5")
    public String md5;

    @Column(name = "times")
    public Timestamp times;

    public String getVer()
    {
        return ver;
    }

    public void setVer(String ver)
    {
        this.ver = ver;
    }

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public Timestamp getTimes()
    {
        return times;
    }

    public void setTimes(Timestamp times)
    {
        this.times = times;
    }

    public String getBanner()
    {
        return banner;
    }

    public void setBanner(String banner)
    {
        this.banner = banner;
    }
}
