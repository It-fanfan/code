package com.fish.service;

import com.fish.dao.primary.mapper.ArcadePlayMapper;
import com.fish.dao.primary.model.ArcadePlay;
import com.fish.dao.second.mapper.InitConfigMapper;
import com.fish.dao.second.model.InitConfig;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArcadePlayService implements BaseService<ArcadePlay> {

    @Autowired
    ArcadePlayMapper arcadePlayMapper;
    @Override
    //查询展示所有产品信息
    public List<ArcadePlay> selectAll(GetParameter parameter) {
        return arcadePlayMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(ArcadePlay record) {
        record.setCreateTime(new Timestamp(new Date().getTime()));
        record.setUpdateTime(new Timestamp(new Date().getTime()));
        return arcadePlayMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadePlay record) {
        record.setUpdateTime(new Timestamp(new Date().getTime()));
        return arcadePlayMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadePlay> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadePlay arcadePlay, Map<String, String> searchData) {
        return false;
    }
}
