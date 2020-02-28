package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import com.fish.utils.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class WxAddShowService implements BaseService<WxConfig> {

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    @Override
    //查询所有WxConfig内容
    public List<WxConfig> selectAll(GetParameter parameter) {
        String resPath = baseConfig.getResHost();
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        for (WxConfig config : wxConfigs)
        {
            String ddAppSkipRes = config.getDdappskipres();
            try
            {
                if (!JSONObject.isValidObject(ddAppSkipRes))
                    continue;
                JSONObject data = JSONObject.parseObject(ddAppSkipRes);
                if (data.containsKey("list"))
                {
                    JSONArray list = data.getJSONArray("list");
                    if (list != null)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            JSONObject object = list.getJSONObject(i);
                            if (object.containsKey("state") && !object.getBoolean("state"))
                                continue;
                            if (object.containsKey("local") && object.getBoolean("local"))
                            {
                                config.setLocal("本地");
                                continue;
                            }
                            String icon = WxConfigService.concatUrl(resPath, object.getString("icon"), config.getDdappid(), "skip");
                            if (icon != null)
                            {
                                Field field = WxConfig.class.getDeclaredField("list" + (i + 1));
                                if (field != null)
                                {
                                    field.setAccessible(true);
                                    field.set(config, icon);
                                }

                            }

                        }
                    }
                }
                if (data.containsKey("banner"))
                {
                    JSONArray list = data.getJSONArray("banner");
                    if (list != null)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            JSONObject object = list.getJSONObject(i);
                            if (object.containsKey("state") && !object.getBoolean("state"))
                                continue;
                            if (object.containsKey("local") && object.getBoolean("local"))
                            {
                                config.setLocal("本地");
                                continue;
                            }
                            String url = WxConfigService.concatUrl(resPath, object.getString("url"), config.getDdappid(), "skip");
                            if (url != null)
                            {
                                Field field = WxConfig.class.getDeclaredField("banner" + (i + 1));
                                if (field != null)
                                {
                                    field.setAccessible(true);
                                    field.set(config, url);
                                }
                            }

                        }
                    }
                }

            } catch (Exception e)
            {
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
        }
        return wxConfigs;
    }

    //新增WxConfig内容
    public int insert(WxConfig record) {
        int insertAppConfig;
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(new Date().getTime()));
        try
        {
            insertAppConfig = appConfigMapper.insert(appConfig);
            System.out.println("appConfig插入数据" + insertAppConfig);
        } catch (Exception e)
        {
            e.printStackTrace();
            //新增判断AppId重复
            insertAppConfig = 3;
        }
        record.setCreateTime(new Timestamp(new Date().getTime()));
        String ddAppSkipRes = record.getDdappskipres();
        if (ddAppSkipRes != null)
        {
            String minify = ReadJsonUtil.minify(ddAppSkipRes);
            record.setDdappskipres(minify);
        }
        int insertWxconfig = 0;
        try
        {
            //新增产品信息
            insertWxconfig = wxConfigMapper.insert(record);
            System.out.println("wx插入数据 :" + insertWxconfig);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return insertWxconfig;
    }


    //更新产品信息
    public int updateByPrimaryKeySelective(WxConfig record) {
        int insert;
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(new Date().getTime()));
        try
        {
            insert = appConfigMapper.updateByPrimaryKeySelective(appConfig);
            System.out.println("appConfig插入数据" + insert);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String ddAppSkipRes = record.getDdappskipres();
        if (ddAppSkipRes != null)
        {
            String minify = ReadJsonUtil.minify(ddAppSkipRes);
            record.setDdappskipres(minify);
        }
        return wxConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddappid");
    }

    @Override
    public Class<WxConfig> getClassInfo() {
        return WxConfig.class;
    }

    @Override
    public boolean removeIf(WxConfig wxConfig, JSONObject searchData) {
        if (existValueFalse(searchData.getString("appId"), wxConfig.getDdappid()))
        {
            return true;
        }
        return existValueFalse(searchData.getString("productsName"), wxConfig.getDdappid());
    }

}
