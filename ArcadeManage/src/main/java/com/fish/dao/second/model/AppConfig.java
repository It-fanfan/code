package com.fish.dao.second.model;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AppConfig
{

    private String productName;

    private String originName;

    private String ddappid;

    private String ddcheckversion;

    private Integer ddprogram;

    private String ddname;

    private String ddgameurl;

    private Integer ddcode;
    private String codename;

    private Integer ddcheckcode;
    private String checkcodename;

    private Date ddtime;

    private String remark;

    public String getCheckcodename()
    {
        return checkcodename;
    }

    public void setCheckcodename(String checkcodename)
    {
        this.checkcodename = checkcodename;
    }

    public String getCodename()
    {
        return codename;
    }

    public void setCodename(String codename)
    {
        this.codename = codename;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getOriginName()
    {
        return originName;
    }

    public void setOriginName(String originName)
    {
        this.originName = originName;
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdcheckversion()
    {
        return ddcheckversion;
    }

    public void setDdcheckversion(String ddcheckversion)
    {
        this.ddcheckversion = ddcheckversion == null ? null : ddcheckversion.trim();
    }

    public Integer getDdprogram()
    {
        return ddprogram;
    }

    public void setDdprogram(Integer ddprogram)
    {
        this.ddprogram = ddprogram;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public String getDdgameurl()
    {
        return ddgameurl;
    }

    public void setDdgameurl(String ddgameurl)
    {
        this.ddgameurl = ddgameurl == null ? null : ddgameurl.trim();
    }

    public Integer getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(Integer ddcode)
    {
        this.ddcode = ddcode;
    }

    public Integer getDdcheckcode()
    {
        return ddcheckcode;
    }

    public void setDdcheckcode(Integer ddcheckcode)
    {
        this.ddcheckcode = ddcheckcode;
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddcheckversion=").append(ddcheckversion);
        sb.append(", ddprogram=").append(ddprogram);
        sb.append(", ddname=").append(ddname);
        sb.append(", ddgameurl=").append(ddgameurl);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddcheckcode=").append(ddcheckcode);
        sb.append(", ddtime=").append(ddtime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}