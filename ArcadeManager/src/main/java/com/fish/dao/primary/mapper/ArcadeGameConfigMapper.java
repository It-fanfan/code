package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGameConfig;

import java.util.List;

public interface ArcadeGameConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArcadeGameConfig record);

    int insertSelective(ArcadeGameConfig record);

    ArcadeGameConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArcadeGameConfig record);

    int updateByPrimaryKey(ArcadeGameConfig record);

    List<ArcadeGameConfig> selectAll();
}