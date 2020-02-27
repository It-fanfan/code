package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGameListMapper;
import com.fish.dao.primary.model.ArcadeGameList;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GameLisyService implements BaseService<ArcadeGameList>{


    @Autowired
    ArcadeGameListMapper arcadeGameListMapper;

    @Override
    //查询展示所有产品信息
    public List<ArcadeGameList> selectAll(GetParameter parameter) {

        return arcadeGameListMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(ArcadeGameList record) {
        record.setCreateTime(new Timestamp(new Date().getTime()));
        return arcadeGameListMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadeGameList record) {
        record.setUpdateTime(new Timestamp(new Date().getTime()));
        return arcadeGameListMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadeGameList> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeGameList arcadeGameConfig, Map<String, String> searchData) {
        return false;
    }
}
