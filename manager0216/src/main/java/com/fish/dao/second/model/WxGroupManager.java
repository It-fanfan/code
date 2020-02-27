package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 微信群管理
 */
@Component
public class WxGroupManager {
    /**
     * wx_group_manager 部分内容
     */
    private String id;
    /**
     * 关联config_confirm的ddId
     */
    private String cdId;
    /**
     * 微信群名
     */
    private String wxGroupName;
    /**
     * 微信群管理
     */
    private String wxGroupManager;
    /**
     *  微信号
     */
    private String wxNumber;


    private String createTime;

    /**
     * config_confirm部分字段
     */
    private String ddId;
    /**
     * *微信群备注
     */
    private String describe;
    /**
     *  二维码状态 0：关闭；1开启
     */
    private Integer ddStatus;
    /**
     *  微信群二维码地址
     */
    private String ddYes;
    /**
     * 客服二维码
     */
    private String ddNo;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public String getWxGroupName() {
        return wxGroupName;
    }

    public void setWxGroupName(String wxGroupName) {
        this.wxGroupName = wxGroupName;
    }

    public String getWxGroupManager() {
        return wxGroupManager;
    }

    public void setWxGroupManager(String wxGroupManager) {
        this.wxGroupManager = wxGroupManager;
    }

    public String getWxNumber() {
        return wxNumber;
    }

    public void setWxNumber(String wxNumber) {
        this.wxNumber = wxNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDdId() {
        return ddId;
    }

    public void setDdId(String ddId) {
        this.ddId = ddId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getDdStatus() {
        return ddStatus;
    }

    public void setDdStatus(Integer ddStatus) {
        this.ddStatus = ddStatus;
    }

    public String getDdYes() {
        return ddYes;
    }

    public void setDdYes(String ddYes) {
        this.ddYes = ddYes;
    }

    public String getDdNo() {
        return ddNo;
    }

    public void setDdNo(String ddNo) {
        this.ddNo = ddNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }



}
