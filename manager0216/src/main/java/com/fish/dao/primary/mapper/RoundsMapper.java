package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Rounds;

import java.util.List;

public interface RoundsMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(Rounds record);

    int insertSelective(Rounds record);

    Rounds selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rounds record);

    int updateByPrimaryKey(Rounds record);

    List<Rounds> selectAll();

    List<Rounds> selectAllS();

    Rounds selectByDdCode(String ddCode);

    Rounds selectByDdCodeS(String ddCode);

    Rounds selectByDdCodeQ(String ddCode);

}