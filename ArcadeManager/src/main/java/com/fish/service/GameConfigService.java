package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGameConfigMapper;
import com.fish.dao.primary.model.ArcadeGameConfig;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GameConfigService  implements BaseService<ArcadeGameConfig>{


    @Autowired
    ArcadeGameConfigMapper arcadeGameConfigMapper;

    @Override
    //查询展示所有产品信息
    public List<ArcadeGameConfig> selectAll(GetParameter parameter) {

        return arcadeGameConfigMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(ArcadeGameConfig record) {
        record.setCreateTime(new Timestamp(new Date().getTime()));
        return arcadeGameConfigMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadeGameConfig record) {
        record.setUpdateTime(new Timestamp(new Date().getTime()));
        return arcadeGameConfigMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadeGameConfig> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeGameConfig arcadeGameConfig, Map<String, String> searchData) {
        return false;
    }
}
