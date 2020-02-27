package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadePlay;
import com.fish.dao.primary.model.GameDayInfo;

import java.util.List;

public interface ArcadePlayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArcadePlay record);

    int insertSelective(ArcadePlay record);

    ArcadePlay selectByPrimaryKey(Integer id);

    List<ArcadePlay> selectAll();

    int updateByPrimaryKeySelective(ArcadePlay record);

    int updateByPrimaryKey(ArcadePlay record);
}