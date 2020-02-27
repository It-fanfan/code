package com.fish.dao.second.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserAllInfo
{

    private String productName;
    //剩余金额
    private Integer remainMoney;
    private Integer cashOut;

    private Integer ddawardmoney;

    private Integer ddawardcoin;

    private Integer ddcoincount;

    private Integer ddmoney;

    private Integer ddtotalpaymoney;


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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ddregistertime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ddlogintime;


    public Integer getCashOut()
    {
        return cashOut;
    }

    public void setCashOut(Integer cashOut)
    {
        this.cashOut = cashOut;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getRemainMoney()
    {
        return remainMoney;
    }

    public void setRemainMoney(Integer remainMoney)
    {
        this.remainMoney = remainMoney;
    }

    public Integer getDdawardmoney()
    {
        return ddawardmoney;
    }

    public void setDdawardmoney(Integer ddawardmoney)
    {
        this.ddawardmoney = ddawardmoney;
    }

    public Integer getDdawardcoin()
    {
        return ddawardcoin;
    }

    public void setDdawardcoin(Integer ddawardcoin)
    {
        this.ddawardcoin = ddawardcoin;
    }

    public Integer getDdcoincount()
    {
        return ddcoincount;
    }

    public void setDdcoincount(Integer ddcoincount)
    {
        this.ddcoincount = ddcoincount;
    }

    public Integer getDdmoney()
    {
        return ddmoney;
    }

    public void setDdmoney(Integer ddmoney)
    {
        this.ddmoney = ddmoney;
    }

    public Integer getDdtotalpaymoney()
    {
        return ddtotalpaymoney;
    }

    public void setDdtotalpaymoney(Integer ddtotalpaymoney)
    {
        this.ddtotalpaymoney = ddtotalpaymoney;
    }

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
        this.dduid = dduid;
    }

    public String getDdoid()
    {
        return ddoid;
    }

    public void setDdoid(String ddoid)
    {
        this.ddoid = ddoid;
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid;
    }

    public String getDdclientversion()
    {
        return ddclientversion;
    }

    public void setDdclientversion(String ddclientversion)
    {
        this.ddclientversion = ddclientversion;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname;
    }

    public String getDdavatarurl()
    {
        return ddavatarurl;
    }

    public void setDdavatarurl(String ddavatarurl)
    {
        this.ddavatarurl = ddavatarurl;
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
        this.ddavatarframegain = ddavatarframegain;
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
        this.ddprovince = ddprovince;
    }

    public String getDdcity()
    {
        return ddcity;
    }

    public void setDdcity(String ddcity)
    {
        this.ddcity = ddcity;
    }

    public String getDdcountry()
    {
        return ddcountry;
    }

    public void setDdcountry(String ddcountry)
    {
        this.ddcountry = ddcountry;
    }

    public String getDdlanguage()
    {
        return ddlanguage;
    }

    public void setDdlanguage(String ddlanguage)
    {
        this.ddlanguage = ddlanguage;
    }

    public String getDdawardlist()
    {
        return ddawardlist;
    }

    public void setDdawardlist(String ddawardlist)
    {
        this.ddawardlist = ddawardlist;
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
        this.ddservicedtime = ddservicedtime;
    }

    public String getDdsharedtime()
    {
        return ddsharedtime;
    }

    public void setDdsharedtime(String ddsharedtime)
    {
        this.ddsharedtime = ddsharedtime;
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
}