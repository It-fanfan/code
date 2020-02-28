package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 系统管理人员-角色
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:20
 */
public class ManageRole {

    /**
     * 角色ID
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色有权限的页面存储Json字符串
     */
    private String pages;

    /**
     * 角色拥有权限的页面
     */
    private String pageNames;

    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPages() { return pages; }

    public void setPages(String pages) { this.pages = pages; }

    public String getPageNames() { return pageNames; }

    public void setPageNames(String pageNames) { this.pageNames = pageNames; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
