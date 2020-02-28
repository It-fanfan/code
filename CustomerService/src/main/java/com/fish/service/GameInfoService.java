package com.fish.service;

import com.fish.dao.primary.mapper.ManageAccountMapper;
import com.fish.dao.primary.mapper.WxConfigMapper;
import com.fish.dao.primary.model.ManageAccount;
import com.fish.dao.primary.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameInfoService implements BaseService<WxConfig>
{
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Override
    public List<WxConfig> selectAll(GetParameter parameter) {
        return wxConfigMapper.selectAll();
    }


    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<WxConfig> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(WxConfig wxConfig, Map<String, String> searchData) {
        return false;
    }


}
