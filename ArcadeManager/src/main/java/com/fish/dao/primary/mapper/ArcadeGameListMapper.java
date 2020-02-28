package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGameConfig;
import com.fish.dao.primary.model.ArcadeGameList;

import java.util.List;

public interface ArcadeGameListMapper {

    List<ArcadeGameList> selectAll();
    int deleteByPrimaryKey(Long id);

    int insert(ArcadeGameList record);

    int insertSelective(ArcadeGameList record);

    ArcadeGameList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArcadeGameList record);

    int updateByPrimaryKey(ArcadeGameList record);
}