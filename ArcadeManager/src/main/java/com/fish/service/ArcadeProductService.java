package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeProductInfoMapper;
import com.fish.dao.primary.model.ArcadeProductInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArcadeProductService implements BaseService<ArcadeProductInfo>{
    @Autowired
    ArcadeProductInfoMapper arcadeProductInfoMapper;
    @Override
    //查询展示所有产品信息
    public List<ArcadeProductInfo> selectAll(GetParameter parameter) {
        return arcadeProductInfoMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(ArcadeProductInfo record) {
        record.setCreateTime(new Timestamp(new Date().getTime()));
        return arcadeProductInfoMapper.insert(record);
    }

    public int updateByPrimaryKeySelective(ArcadeProductInfo record) {
        record.setUpdateTime(new Timestamp(new Date().getTime()));
        return arcadeProductInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadeProductInfo> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeProductInfo arcadeProductInfo, Map<String, String> searchData) {
        return false;
    }



}
