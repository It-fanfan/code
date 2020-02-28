package com.fish.utils;


public class ExportResult
{

    @Comments(name = "赛制名称")
    private String roundName;

    @Comments(name = "时段")
    private String roundLength;

    @Comments(name = "排名")
    private Integer index;

    @Comments(name = "昵称")
    private String name;

    @Comments(name = "uid")
    private String uid;

    @Comments(name = "奖励数量")
    private Integer value;

    @Comments(name = "奖励")
    private String type;

    @Comments(name = "比赛结束时间")
    private String matchdate;
    @NonExcel
    @Comments(name = "得分")
    private Integer mark;


    public String getMatchdate()
    {
        return matchdate;
    }

    public void setMatchdate(String matchdate)
    {
        this.matchdate = matchdate;
    }

    public String getRoundName()
    {
        return roundName;
    }

    public void setRoundName(String roundName)
    {
        this.roundName = roundName;
    }

    public String getRoundLength()
    {
        return roundLength;
    }

    public void setRoundLength(String roundLength)
    {
        this.roundLength = roundLength;
    }

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getMark()
    {
        return mark;
    }

    public void setMark(Integer mark)
    {
        this.mark = mark;
    }
}
