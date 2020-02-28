package com.fish.dao.second.model;

import java.util.Date;

public class UserInfo
{
    private Integer id;

    private String dduid;

    private String ddoid;

    private String ddappid;

    private String ddclientversion;

    private String ddname;

    private String ddavatarurl;

    private Integer ddavatarframe;

    private String ddavatarframegain;

    private Integer ddsex;

    private String ddprovince;

    private String ddcity;

    private String ddcountry;

    private String ddlanguage;

    private String ddawardlist;

    private Integer ddcollected;

    private Integer ddinterested;

    private String ddservicedtime;

    private String ddsharedtime;

    private Integer dddaywatchvideo;

    private Integer dddaylogingift;

    private Date ddregistertime;

    private Date ddlogintime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDduid()
    {
        return dduid;
    }

    public void setDduid(String dduid)
    {
        this.dduid = dduid == null ? null : dduid.trim();
    }

    public String getDdoid()
    {
        return ddoid;
    }

    public void setDdoid(String ddoid)
    {
        this.ddoid = ddoid == null ? null : ddoid.trim();
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdclientversion()
    {
        return ddclientversion;
    }

    public void setDdclientversion(String ddclientversion)
    {
        this.ddclientversion = ddclientversion == null ? null : ddclientversion.trim();
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public String getDdavatarurl()
    {
        return ddavatarurl;
    }

    public void setDdavatarurl(String ddavatarurl)
    {
        this.ddavatarurl = ddavatarurl == null ? null : ddavatarurl.trim();
    }

    public Integer getDdavatarframe()
    {
        return ddavatarframe;
    }

    public void setDdavatarframe(Integer ddavatarframe)
    {
        this.ddavatarframe = ddavatarframe;
    }

    public String getDdavatarframegain()
    {
        return ddavatarframegain;
    }

    public void setDdavatarframegain(String ddavatarframegain)
    {
        this.ddavatarframegain = ddavatarframegain == null ? null : ddavatarframegain.trim();
    }

    public Integer getDdsex()
    {
        return ddsex;
    }

    public void setDdsex(Integer ddsex)
    {
        this.ddsex = ddsex;
    }

    public String getDdprovince()
    {
        return ddprovince;
    }

    public void setDdprovince(String ddprovince)
    {
        this.ddprovince = ddprovince == null ? null : ddprovince.trim();
    }

    public String getDdcity()
    {
        return ddcity;
    }

    public void setDdcity(String ddcity)
    {
        this.ddcity = ddcity == null ? null : ddcity.trim();
    }

    public String getDdcountry()
    {
        return ddcountry;
    }

    public void setDdcountry(String ddcountry)
    {
        this.ddcountry = ddcountry == null ? null : ddcountry.trim();
    }

    public String getDdlanguage()
    {
        return ddlanguage;
    }

    public void setDdlanguage(String ddlanguage)
    {
        this.ddlanguage = ddlanguage == null ? null : ddlanguage.trim();
    }

    public String getDdawardlist()
    {
        return ddawardlist;
    }

    public void setDdawardlist(String ddawardlist)
    {
        this.ddawardlist = ddawardlist == null ? null : ddawardlist.trim();
    }

    public Integer getDdcollected()
    {
        return ddcollected;
    }

    public void setDdcollected(Integer ddcollected)
    {
        this.ddcollected = ddcollected;
    }

    public Integer getDdinterested()
    {
        return ddinterested;
    }

    public void setDdinterested(Integer ddinterested)
    {
        this.ddinterested = ddinterested;
    }

    public String getDdservicedtime()
    {
        return ddservicedtime;
    }

    public void setDdservicedtime(String ddservicedtime)
    {
        this.ddservicedtime = ddservicedtime == null ? null : ddservicedtime.trim();
    }

    public String getDdsharedtime()
    {
        return ddsharedtime;
    }

    public void setDdsharedtime(String ddsharedtime)
    {
        this.ddsharedtime = ddsharedtime == null ? null : ddsharedtime.trim();
    }

    public Integer getDddaywatchvideo()
    {
        return dddaywatchvideo;
    }

    public void setDddaywatchvideo(Integer dddaywatchvideo)
    {
        this.dddaywatchvideo = dddaywatchvideo;
    }

    public Integer getDddaylogingift()
    {
        return dddaylogingift;
    }

    public void setDddaylogingift(Integer dddaylogingift)
    {
        this.dddaylogingift = dddaylogingift;
    }

    public Date getDdregistertime()
    {
        return ddregistertime;
    }

    public void setDdregistertime(Date ddregistertime)
    {
        this.ddregistertime = ddregistertime;
    }

    public Date getDdlogintime()
    {
        return ddlogintime;
    }

    public void setDdlogintime(Date ddlogintime)
    {
        this.ddlogintime = ddlogintime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dduid=").append(dduid);
        sb.append(", ddoid=").append(ddoid);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddclientversion=").append(ddclientversion);
        sb.append(", ddname=").append(ddname);
        sb.append(", ddavatarurl=").append(ddavatarurl);
        sb.append(", ddavatarframe=").append(ddavatarframe);
        sb.append(", ddavatarframegain=").append(ddavatarframegain);
        sb.append(", ddsex=").append(ddsex);
        sb.append(", ddprovince=").append(ddprovince);
        sb.append(", ddcity=").append(ddcity);
        sb.append(", ddcountry=").append(ddcountry);
        sb.append(", ddlanguage=").append(ddlanguage);
        sb.append(", ddawardlist=").append(ddawardlist);
        sb.append(", ddcollected=").append(ddcollected);
        sb.append(", ddinterested=").append(ddinterested);
        sb.append(", ddservicedtime=").append(ddservicedtime);
        sb.append(", ddsharedtime=").append(ddsharedtime);
        sb.append(", dddaywatchvideo=").append(dddaywatchvideo);
        sb.append(", dddaylogingift=").append(dddaylogingift);
        sb.append(", ddregistertime=").append(ddregistertime);
        sb.append(", ddlogintime=").append(ddlogintime);
        sb.append("]");
        return sb.toString();
    }
}