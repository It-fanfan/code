package com.fish.dao.primary.model;

import java.util.Date;

public class RankingRecord
{
    private Long id;

    private String dduid;

    private Integer ddgcode;

    private Date ddmdate;

    private String ddmcode;

    private Integer ddmindex;

    private Integer ddmark;

    private Integer ddranking;

    private String ddtype;

    private Integer ddtotal;

    private Date ddtime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public Integer getDdgcode()
    {
        return ddgcode;
    }

    public void setDdgcode(Integer ddgcode)
    {
        this.ddgcode = ddgcode;
    }

    public Date getDdmdate()
    {
        return ddmdate;
    }

    public void setDdmdate(Date ddmdate)
    {
        this.ddmdate = ddmdate;
    }

    public String getDdmcode()
    {
        return ddmcode;
    }

    public void setDdmcode(String ddmcode)
    {
        this.ddmcode = ddmcode == null ? null : ddmcode.trim();
    }

    public Integer getDdmindex()
    {
        return ddmindex;
    }

    public void setDdmindex(Integer ddmindex)
    {
        this.ddmindex = ddmindex;
    }

    public Integer getDdmark()
    {
        return ddmark;
    }

    public void setDdmark(Integer ddmark)
    {
        this.ddmark = ddmark;
    }

    public Integer getDdranking()
    {
        return ddranking;
    }

    public void setDdranking(Integer ddranking)
    {
        this.ddranking = ddranking;
    }

    public String getDdtype()
    {
        return ddtype;
    }

    public void setDdtype(String ddtype)
    {
        this.ddtype = ddtype == null ? null : ddtype.trim();
    }

    public Integer getDdtotal()
    {
        return ddtotal;
    }

    public void setDdtotal(Integer ddtotal)
    {
        this.ddtotal = ddtotal;
    }

    public Date getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Date ddtime)
    {
        this.ddtime = ddtime;
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
        sb.append(", ddgcode=").append(ddgcode);
        sb.append(", ddmdate=").append(ddmdate);
        sb.append(", ddmcode=").append(ddmcode);
        sb.append(", ddmindex=").append(ddmindex);
        sb.append(", ddmark=").append(ddmark);
        sb.append(", ddranking=").append(ddranking);
        sb.append(", ddtype=").append(ddtype);
        sb.append(", ddtotal=").append(ddtotal);
        sb.append(", ddtime=").append(ddtime);
        sb.append("]");
        return sb.toString();
    }
}