package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.GameRound;

import java.util.List;

public interface GameRoundMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(GameRound record);

    int insertSelective(GameRound record);

    GameRound selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameRound record);

    int updateByPrimaryKey(GameRound record);

    List<GameRound> selectAll();

    List<GameRound> selectByDdCode(Integer code);

    List<GameRound> selectByDdRound(String ddRound);
}