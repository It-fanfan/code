package com.fish.dao.second.model;

import java.util.Date;

public class UserApp
{
    private String ddoid;

    private String dduid;

    private String ddappid;

    private String ddclientversion;

    private Date ddregistertime;

    public String getDdoid()
    {
        return ddoid;
    }

    public void setDdoid(String ddoid)
    {
        this.ddoid = ddoid == null ? null : ddoid.trim();
    }

    public String getDduid()
    {
        return dduid;
    }

    public void setDduid(String dduid)
    {
        this.dduid = dduid == null ? null : dduid.trim();
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

    public Date getDdregistertime()
    {
        return ddregistertime;
    }

    public void setDdregistertime(Date ddregistertime)
    {
        this.ddregistertime = ddregistertime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddoid=").append(ddoid);
        sb.append(", dduid=").append(dduid);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddclientversion=").append(ddclientversion);
        sb.append(", ddregistertime=").append(ddregistertime);
        sb.append("]");
        return sb.toString();
    }
}