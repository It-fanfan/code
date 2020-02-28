package com.fish.dao.second.mapper;

import com.fish.dao.second.model.AppConfig;

import java.util.List;

public interface AppConfigMapper
{
    int deleteByPrimaryKey(String ddappid);

    int insert(AppConfig record);

    int insertSelective(AppConfig record);

    AppConfig selectByPrimaryKey(String ddappid);

    int updateByPrimaryKeySelective(AppConfig record);

    int updateByPrimaryKey(AppConfig record);

    List<AppConfig> selectAll();
}