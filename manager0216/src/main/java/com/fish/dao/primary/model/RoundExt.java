package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RoundExt
{


    private String roundLength;
    private String ddcode;

    private Boolean ddgroup;

    private String ddname;

    private Integer ddpriority;

    private Boolean ddstate;

    private Long ddtime;

    private String ddreward;

    private String tip;

    private String dddesc;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date inserttime;


    public String getRoundLength()
    {
        return roundLength;
    }

    public void setRoundLength(String roundLength)
    {
        this.roundLength = roundLength;
    }

    public String getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(String ddcode)
    {
        this.ddcode = ddcode == null ? null : ddcode.trim();
    }

    public Boolean getDdgroup()
    {
        return ddgroup;
    }

    public void setDdgroup(Boolean ddgroup)
    {
        this.ddgroup = ddgroup;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public Integer getDdpriority()
    {
        return ddpriority;
    }

    public void setDdpriority(Integer ddpriority)
    {
        this.ddpriority = ddpriority;
    }

    public Boolean getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate)
    {
        this.ddstate = ddstate;
    }

    public Long getDdtime()
    {
        return ddtime;
    }

    public void setDdtime(Long ddtime)
    {
        this.ddtime = ddtime;
    }

    public String getDdreward()
    {
        return ddreward;
    }

    public void setDdreward(String ddreward)
    {
        this.ddreward = ddreward == null ? null : ddreward.trim();
    }

    public String getTip()
    {
        return tip;
    }

    public void setTip(String tip)
    {
        this.tip = tip == null ? null : tip.trim();
    }

    public String getDddesc()
    {
        return dddesc;
    }

    public void setDddesc(String dddesc)
    {
        this.dddesc = dddesc == null ? null : dddesc.trim();
    }

    public Date getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Date inserttime)
    {
        this.inserttime = inserttime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddgroup=").append(ddgroup);
        sb.append(", ddname=").append(ddname);
        sb.append(", ddpriority=").append(ddpriority);
        sb.append(", ddstate=").append(ddstate);
        sb.append(", ddtime=").append(ddtime);
        sb.append(", ddreward=").append(ddreward);
        sb.append(", tip=").append(tip);
        sb.append(", dddesc=").append(dddesc);
        sb.append(", inserttime=").append(inserttime);
        sb.append("]");
        return sb.toString();
    }
}