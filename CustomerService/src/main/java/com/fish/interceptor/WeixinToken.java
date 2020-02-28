package com.fish.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.WxConfigMapper;
import com.fish.dao.primary.model.WxConfig;
import com.fish.utils.http.XwhHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * token 维护工程
 */
@Service
public class WeixinToken implements Runnable {

    @Autowired
    WxConfigMapper mapper;

    private static Map<String, WxConfig> cache = null;

    @Override
    public void run() {
        System.out.println("执行线程，加载token维护!!!!" + mapper);
        if (cache == null) cache = new ConcurrentHashMap<>();
        List<WxConfig> wxConfigs = mapper.selectAll();
        System.out.println("查找数据:" + wxConfigs);
        if (wxConfigs != null) {
            wxConfigs.forEach(this::setAccessToken);
        }
        System.out.println("加载维护token线程池!!!");
    }


    /**
     * 获取access_token
     */
    private void setAccessToken(WxConfig wxConfig) {
        //获取access_token
        String appId = wxConfig.getDdappid();
        String appSecret = wxConfig.getDdappsecret();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String resToken = null;
        try {
            resToken = XwhHttp.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("进行维护token" + url + ",result=" + resToken);
        JSONObject result = JSONObject.parseObject(resToken);
        if (result.containsKey("access_token")) {
            wxConfig.setDdaccesstoken(result.getString("access_token"));
            wxConfig.setDdexpiresin(result.getInteger("expires_in"));
            wxConfig.setDdtokentime(new Timestamp(System.currentTimeMillis()));
            mapper.updateByPrimaryKey(wxConfig);
            cache.put(wxConfig.getDdappid(), wxConfig);
        }
    }

    /**
     * 获取token数据
     *
     * @param appId 游戏编号
     * @return token信息
     */
    public static WxConfig getWxConfig(String appId) {
        if (cache == null) return null;
        return cache.get(appId);
    }
}
