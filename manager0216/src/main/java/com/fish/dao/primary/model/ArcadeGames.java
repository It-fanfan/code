package com.fish.dao.primary.model;

import org.springframework.stereotype.Component;

@Component
public class ArcadeGames
{
    private Integer id;

    private Integer ddcode;

    private String ddname;


    private Integer ddsinglecoin;

    private Integer ddmulticoin;

    private Integer ddmaxplayer;

    private Integer ddispk;

    private Integer ddavailable;


    private String ddtitle;


    private Integer ddrolecount;

    private Integer ddrocker;

    private Integer ddengine;

    private Integer ddresolution;

    private String ddappid;

    private Integer ddautoselect;

    private String ddfriendurl;

    private String ddshareres;

    public String getDdshareres()
    {
        return ddshareres;
    }

    public void setDdshareres(String ddshareres)
    {
        this.ddshareres = ddshareres;
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname;
    }

    public String getDdtitle()
    {
        return ddtitle;
    }

    public void setDdtitle(String ddtitle)
    {
        this.ddtitle = ddtitle;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(Integer ddcode)
    {
        this.ddcode = ddcode;
    }


    public Integer getDdsinglecoin()
    {
        return ddsinglecoin;
    }

    public void setDdsinglecoin(Integer ddsinglecoin)
    {
        this.ddsinglecoin = ddsinglecoin;
    }

    public Integer getDdmulticoin()
    {
        return ddmulticoin;
    }

    public void setDdmulticoin(Integer ddmulticoin)
    {
        this.ddmulticoin = ddmulticoin;
    }

    public Integer getDdmaxplayer()
    {
        return ddmaxplayer;
    }

    public void setDdmaxplayer(Integer ddmaxplayer)
    {
        this.ddmaxplayer = ddmaxplayer;
    }

    public Integer getDdispk()
    {
        return ddispk;
    }

    public void setDdispk(Integer ddispk)
    {
        this.ddispk = ddispk;
    }

    public Integer getDdavailable()
    {
        return ddavailable;
    }

    public void setDdavailable(Integer ddavailable)
    {
        this.ddavailable = ddavailable;
    }


    public Integer getDdrolecount()
    {
        return ddrolecount;
    }

    public void setDdrolecount(Integer ddrolecount)
    {
        this.ddrolecount = ddrolecount;
    }

    public Integer getDdrocker()
    {
        return ddrocker;
    }

    public void setDdrocker(Integer ddrocker)
    {
        this.ddrocker = ddrocker;
    }

    public Integer getDdengine()
    {
        return ddengine;
    }

    public void setDdengine(Integer ddengine)
    {
        this.ddengine = ddengine;
    }

    public Integer getDdresolution()
    {
        return ddresolution;
    }

    public void setDdresolution(Integer ddresolution)
    {
        this.ddresolution = ddresolution;
    }

    public String getDdappid()
    {
        return ddappid;
    }

    public void setDdappid(String ddappid)
    {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public Integer getDdautoselect()
    {
        return ddautoselect;
    }

    public void setDdautoselect(Integer ddautoselect)
    {
        this.ddautoselect = ddautoselect;
    }

    public String getDdfriendurl()
    {
        return ddfriendurl;
    }

    public void setDdfriendurl(String ddfriendurl)
    {
        this.ddfriendurl = ddfriendurl == null ? null : ddfriendurl.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddsinglecoin=").append(ddsinglecoin);
        sb.append(", ddmulticoin=").append(ddmulticoin);
        sb.append(", ddmaxplayer=").append(ddmaxplayer);
        sb.append(", ddispk=").append(ddispk);
        sb.append(", ddavailable=").append(ddavailable);

        sb.append(", ddrolecount=").append(ddrolecount);
        sb.append(", ddrocker=").append(ddrocker);
        sb.append(", ddengine=").append(ddengine);
        sb.append(", ddresolution=").append(ddresolution);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddautoselect=").append(ddautoselect);
        sb.append(", ddfriendurl=").append(ddfriendurl);
        sb.append("]");
        return sb.toString();
    }
}