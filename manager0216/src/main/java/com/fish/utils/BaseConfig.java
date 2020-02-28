package com.fish.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "config")
public class BaseConfig
{
    private String upload;

    private String domain;

    private String excelSave;

    //资源路径
    private String readRes;
    //赛场资源
    private String matchRes;
    //资源域名
    private String resHost;
    //刷新接口
    private String flushCache;
    //刷新公众号接口
    private String flushPublicCache;
    //刷新在线人数
    private String flushOnline;

    private JSONObject uploadJson;
    //微信查询订单地址
    private String wxQueryUrl;
    //服务器补单地址
    private String supplementUrl;

    public String getFlushPublicCache() {
        return flushPublicCache;
    }

    public void setFlushPublicCache(String flushPublicCache) {
        this.flushPublicCache = flushPublicCache;
    }

    public String getSupplementUrl()
    {
        return supplementUrl;
    }

    public void setSupplementUrl(String supplementUrl)
    {
        this.supplementUrl = supplementUrl;
    }

    public String getWxQueryUrl()
    {
        return wxQueryUrl;
    }

    public void setWxQueryUrl(String wxQueryUrl)
    {
        this.wxQueryUrl = wxQueryUrl;
    }

    public String getUpload()
    {
        return upload;
    }

    public void setUpload(String upload)
    {
        this.upload = upload;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getExcelSave()
    {
        return excelSave;
    }

    public void setExcelSave(String excelSave)
    {
        this.excelSave = excelSave;
    }

    public String getReadRes()
    {
        return readRes;
    }

    public void setReadRes(String readRes)
    {
        this.readRes = readRes;
    }

    public String getMatchRes()
    {
        return matchRes;
    }

    public void setMatchRes(String matchRes)
    {
        this.matchRes = matchRes;
    }

    public String getResHost()
    {
        return resHost;
    }

    public void setResHost(String resHost)
    {
        this.resHost = resHost;
    }

    public String getFlushCache()
    {
        return flushCache;
    }

    public void setFlushCache(String flushCache)
    {
        this.flushCache = flushCache;
    }

    public String getFlushOnline()
    {
        return flushOnline;
    }

    public void setFlushOnline(String flushOnline)
    {
        this.flushOnline = flushOnline;
    }

    public JSONObject getUploadJson()
    {
        return uploadJson;
    }

    public void setUploadJson(JSONObject uploadJson)
    {
        this.uploadJson = uploadJson;
    }
}
