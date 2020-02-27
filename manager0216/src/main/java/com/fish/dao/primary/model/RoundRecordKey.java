package com.fish.dao.primary.model;

public class RoundRecordKey
{
    private Integer ddcode;

    private Boolean ddgroup;

    private Integer ddindex;

    public Integer getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(Integer ddcode)
    {
        this.ddcode = ddcode;
    }

    public Boolean getDdgroup()
    {
        return ddgroup;
    }

    public void setDdgroup(Boolean ddgroup)
    {
        this.ddgroup = ddgroup;
    }

    public Integer getDdindex()
    {
        return ddindex;
    }

    public void setDdindex(Integer ddindex)
    {
        this.ddindex = ddindex;
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
        sb.append(", ddindex=").append(ddindex);
        sb.append("]");
        return sb.toString();
    }
}