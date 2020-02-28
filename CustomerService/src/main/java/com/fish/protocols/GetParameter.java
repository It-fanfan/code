package com.fish.protocols;

public class GetParameter
{
    //查询类型
    private String datagrid;
    //查询条件
    private String searchData;
    //排序信息
    private String sort;
    private String order;

    private int page;
    private int limit;
    //是否为excel导出模式
    private boolean excel;

    public String getDatagrid()
    {
        return datagrid;
    }

    public void setDatagrid(String datagrid)
    {
        this.datagrid = datagrid;
    }

    public String getSearchData()
    {
        return searchData;
    }

    public void setSearchData(String searchData)
    {
        this.searchData = searchData;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public boolean isExcel()
    {
        return excel;
    }

    public void setExcel(boolean excel)
    {
        this.excel = excel;
    }
}
