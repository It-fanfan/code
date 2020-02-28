package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGames;

import java.util.List;

public interface ArcadeGamesMapper {

    List<ArcadeGames> selectAll();
    int deleteByPrimaryKey(Integer id);

    int insert(ArcadeGames record);

    int insertSelective(ArcadeGames record);

    ArcadeGames selectByPrimaryKey(Integer ddCode);

    int updateByPrimaryKeySelective(ArcadeGames record);

    //新增修改游戏信息
    int updateGameBySelective(ArcadeGames record);

    int  insertGameInfo(ArcadeGames record);

    int updateByPrimaryKey(ArcadeGames record);

    int updateSQL(String SQL);

    ArcadeGames selectByGameName(String ddName);
}