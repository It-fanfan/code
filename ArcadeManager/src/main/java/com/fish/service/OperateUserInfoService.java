package com.fish.service;

import com.fish.dao.second.mapper.InitConfigMapper;
import com.fish.dao.second.mapper.OperateUserInfoMapper;
import com.fish.dao.second.model.InitConfig;
import com.fish.dao.second.model.OperateUserInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperateUserInfoService implements BaseService<OperateUserInfo> {

    @Autowired
    InitConfigMapper initConfigMapper;
    @Autowired
    OperateUserInfoMapper operateUserInfoMapper;
    @Override
    //查询展示所有产品信息
    public List<OperateUserInfo> selectAll(GetParameter parameter) {
        return operateUserInfoMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(InitConfig record) {

        return initConfigMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(InitConfig record) {

        return initConfigMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<OperateUserInfo> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(OperateUserInfo operateUserInfo, Map<String, String> searchData) {
        return false;
    }
}
