package com.fish.service;

import com.fish.dao.second.mapper.InitConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.InitConfig;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WxConfigService implements BaseService<WxConfig> {

    @Autowired
    WxConfigMapper initConfigMapper;
    @Override
    //查询展示所有wxconfig信息
    public List<WxConfig> selectAll(GetParameter parameter) {
        return initConfigMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(WxConfig record) {

        return initConfigMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(WxConfig record) {

        return initConfigMapper.updateByPrimaryKeySelective(record);
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
