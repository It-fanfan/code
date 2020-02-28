package com.fish.dao.third.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class MinitjWx
{

    private String wxAppid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wxDate;

    private Integer wxNew;

    private Integer wxActive;

    private Integer wxVisit;

    private BigDecimal wxAvgLogin;

    private BigDecimal wxAvgOnline;

    private BigDecimal wxRemain2;

    private Integer wxVideoShow;

    private BigDecimal wxVideoClickrate;

    private BigDecimal wxVideoIncome;

    private Integer wxBannerShow;

    private BigDecimal wxBannerClickrate;

    private BigDecimal wxBannerIncome;

    private Integer wxRegAd;

    private Integer wxRegJump;

    private Integer wxRegSearch;

    private Integer wxRegApp;

    private Integer wxRegCode;

    private Integer wxRegSession;

    private BigDecimal wxActiveWomen;

    private Integer wxShareUser;

    private Integer wxShareCount;

    private BigDecimal wxShareRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wxUpdatetime;

    private String wxRegJson;

    public String getWxAppid()
    {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid)
    {
        this.wxAppid = wxAppid == null ? null : wxAppid.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getWxDate()
    {
        return wxDate;
    }

    public void setWxDate(Date wxDate)
    {
        this.wxDate = wxDate;
    }

    public Integer getWxNew()
    {
        return wxNew;
    }

    public void setWxNew(Integer wxNew)
    {
        this.wxNew = wxNew;
    }

    public Integer getWxActive()
    {
        return wxActive;
    }

    public void setWxActive(Integer wxActive)
    {
        this.wxActive = wxActive;
    }

    public Integer getWxVisit()
    {
        return wxVisit;
    }

    public void setWxVisit(Integer wxVisit)
    {
        this.wxVisit = wxVisit;
    }

    public BigDecimal getWxAvgLogin()
    {
        return wxAvgLogin;
    }

    public void setWxAvgLogin(BigDecimal wxAvgLogin)
    {
        this.wxAvgLogin = wxAvgLogin;
    }

    public BigDecimal getWxAvgOnline()
    {
        return wxAvgOnline;
    }

    public void setWxAvgOnline(BigDecimal wxAvgOnline)
    {
        this.wxAvgOnline = wxAvgOnline;
    }

    public BigDecimal getWxRemain2()
    {
        return wxRemain2;
    }

    public void setWxRemain2(BigDecimal wxRemain2)
    {
        this.wxRemain2 = wxRemain2;
    }

    public Integer getWxVideoShow()
    {
        return wxVideoShow;
    }

    public void setWxVideoShow(Integer wxVideoShow)
    {
        this.wxVideoShow = wxVideoShow;
    }

    public BigDecimal getWxVideoClickrate()
    {
        return wxVideoClickrate;
    }

    public void setWxVideoClickrate(BigDecimal wxVideoClickrate)
    {
        this.wxVideoClickrate = wxVideoClickrate;
    }

    public BigDecimal getWxVideoIncome()
    {
        return wxVideoIncome;
    }

    public void setWxVideoIncome(BigDecimal wxVideoIncome)
    {
        this.wxVideoIncome = wxVideoIncome;
    }

    public Integer getWxBannerShow()
    {
        return wxBannerShow;
    }

    public void setWxBannerShow(Integer wxBannerShow)
    {
        this.wxBannerShow = wxBannerShow;
    }

    public BigDecimal getWxBannerClickrate()
    {
        return wxBannerClickrate;
    }

    public void setWxBannerClickrate(BigDecimal wxBannerClickrate)
    {
        this.wxBannerClickrate = wxBannerClickrate;
    }

    public BigDecimal getWxBannerIncome()
    {
        return wxBannerIncome;
    }

    public void setWxBannerIncome(BigDecimal wxBannerIncome)
    {
        this.wxBannerIncome = wxBannerIncome;
    }

    public Integer getWxRegAd()
    {
        return wxRegAd;
    }

    public void setWxRegAd(Integer wxRegAd)
    {
        this.wxRegAd = wxRegAd;
    }

    public Integer getWxRegJump()
    {
        return wxRegJump;
    }

    public void setWxRegJump(Integer wxRegJump)
    {
        this.wxRegJump = wxRegJump;
    }

    public Integer getWxRegSearch()
    {
        return wxRegSearch;
    }

    public void setWxRegSearch(Integer wxRegSearch)
    {
        this.wxRegSearch = wxRegSearch;
    }

    public Integer getWxRegApp()
    {
        return wxRegApp;
    }

    public void setWxRegApp(Integer wxRegApp)
    {
        this.wxRegApp = wxRegApp;
    }

    public Integer getWxRegCode()
    {
        return wxRegCode;
    }

    public void setWxRegCode(Integer wxRegCode)
    {
        this.wxRegCode = wxRegCode;
    }

    public Integer getWxRegSession()
    {
        return wxRegSession;
    }

    public void setWxRegSession(Integer wxRegSession)
    {
        this.wxRegSession = wxRegSession;
    }

    public BigDecimal getWxActiveWomen()
    {
        return wxActiveWomen;
    }

    public void setWxActiveWomen(BigDecimal wxActiveWomen)
    {
        this.wxActiveWomen = wxActiveWomen;
    }

    public Integer getWxShareUser()
    {
        return wxShareUser;
    }

    public void setWxShareUser(Integer wxShareUser)
    {
        this.wxShareUser = wxShareUser;
    }

    public Integer getWxShareCount()
    {
        return wxShareCount;
    }

    public void setWxShareCount(Integer wxShareCount)
    {
        this.wxShareCount = wxShareCount;
    }

    public BigDecimal getWxShareRate()
    {
        return wxShareRate;
    }

    public void setWxShareRate(BigDecimal wxShareRate)
    {
        this.wxShareRate = wxShareRate;
    }

    public Date getWxUpdatetime()
    {
        return wxUpdatetime;
    }

    public void setWxUpdatetime(Date wxUpdatetime)
    {
        this.wxUpdatetime = wxUpdatetime;
    }

    public String getWxRegJson()
    {
        return wxRegJson;
    }

    public void setWxRegJson(String wxRegJson)
    {
        this.wxRegJson = wxRegJson == null ? null : wxRegJson.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", wxAppid=").append(wxAppid);
        sb.append(", wxDate=").append(wxDate);
        sb.append(", wxNew=").append(wxNew);
        sb.append(", wxActive=").append(wxActive);
        sb.append(", wxVisit=").append(wxVisit);
        sb.append(", wxAvgLogin=").append(wxAvgLogin);
        sb.append(", wxAvgOnline=").append(wxAvgOnline);
        sb.append(", wxRemain2=").append(wxRemain2);
        sb.append(", wxVideoShow=").append(wxVideoShow);
        sb.append(", wxVideoClickrate=").append(wxVideoClickrate);
        sb.append(", wxVideoIncome=").append(wxVideoIncome);
        sb.append(", wxBannerShow=").append(wxBannerShow);
        sb.append(", wxBannerClickrate=").append(wxBannerClickrate);
        sb.append(", wxBannerIncome=").append(wxBannerIncome);
        sb.append(", wxRegAd=").append(wxRegAd);
        sb.append(", wxRegJump=").append(wxRegJump);
        sb.append(", wxRegSearch=").append(wxRegSearch);
        sb.append(", wxRegApp=").append(wxRegApp);
        sb.append(", wxRegCode=").append(wxRegCode);
        sb.append(", wxRegSession=").append(wxRegSession);
        sb.append(", wxActiveWomen=").append(wxActiveWomen);
        sb.append(", wxShareUser=").append(wxShareUser);
        sb.append(", wxShareCount=").append(wxShareCount);
        sb.append(", wxShareRate=").append(wxShareRate);
        sb.append(", wxUpdatetime=").append(wxUpdatetime);
        sb.append(", wxRegJson=").append(wxRegJson);
        sb.append("]");
        return sb.toString();
    }
}