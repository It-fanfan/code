package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;

import java.util.List;

public interface ArcadeGameSetMapper {

    List<ArcadeGameSet> selectAll();
    int deleteByPrimaryKey(Integer id);

    int insert(ArcadeGameSet record);

    int insertSelective(ArcadeGameSet record);

    ArcadeGameSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ArcadeGameSet record);

    int updateByPrimaryKeySelective(ArcadeGameSet record);
}