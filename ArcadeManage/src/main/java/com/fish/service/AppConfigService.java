package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AppConfigService implements BaseService<AppConfig>
{

    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;

    @Override
    //查询展示appconfig信息
    public List<AppConfig> selectAll(GetParameter parameter)
    {
        List<AppConfig> appConfigs = appConfigMapper.selectAll();
        for (AppConfig appConfig : appConfigs)
        {
            String ddappid = appConfig.getDdappid();
            Integer ddcode = appConfig.getDdcode();
            Integer ddcheckcode = appConfig.getDdcheckcode();
            if (ddcode != null)
            {
                ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(ddcode);
                appConfig.setCodename(arcadeGameSet.getDdname());
            }
            if (ddcheckcode != null && ddcheckcode != 0)
            {
                ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(ddcheckcode);
                appConfig.setCheckcodename(arcadeGameSet.getDdname());
            }
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddappid);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                appConfig.setProductName(productName);
                if (StringUtils.isNotBlank(wxConfig.getOriginName()))
                {
                    appConfig.setOriginName(wxConfig.getOriginName());
                }
            }
        }
        return appConfigs;
    }

    //新增appconfig信息
    public int insert(AppConfig record)
    {

        return appConfigMapper.insert(record);
    }

    //更新appconfig信息
    public int updateByPrimaryKeySelective(AppConfig record)
    {
        record.setDdtime(new Timestamp(new Date().getTime()));
        return appConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<AppConfig> getClassInfo()
    {
        return AppConfig.class;
    }

    @Override
    public boolean removeIf(AppConfig appConfig, JSONObject searchData)
    {

        if (existValueFalse(searchData.getString("appId"), appConfig.getDdappid()))
        {
            return true;
        }

        if (existValueFalse(searchData.getString("gameName"), appConfig.getDdname()))
        {
            return true;
        }
        return existValueFalse(searchData.getString("gameCode"), appConfig.getDdcode());
    }
}
