package com.fish.dao.second.mapper;

import com.fish.dao.second.model.InitConfig;

import java.util.List;

public interface InitConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InitConfig record);

    int insertSelective(InitConfig record);

    InitConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InitConfig record);

    int updateByPrimaryKey(InitConfig record);

    List<InitConfig> selectAll();
}