package com.fish.dao.primary.model;

public class PageInfo
{
    Integer pageId;
    Integer pageParentId;
    String pageUrl;
    String pageName;

    public Integer getPageId()
    {
        return pageId;
    }

    public void setPageId(Integer pageId)
    {
        this.pageId = pageId;
    }

    public Integer getPageParentId()
    {
        return pageParentId;
    }

    public void setPageParentId(Integer pageParentId)
    {
        this.pageParentId = pageParentId;
    }

    public String getPageUrl()
    {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl)
    {
        this.pageUrl = pageUrl;
    }

    public String getPageName()
    {
        return pageName;
    }

    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }
}
