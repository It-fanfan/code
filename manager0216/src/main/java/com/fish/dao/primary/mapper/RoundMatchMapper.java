package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundMatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundMatchMapper
{
    int deleteByPrimaryKey(Integer ddcode);

    int insert(RoundMatch record);

    int insertSelective(RoundMatch record);

    RoundMatch selectByPrimaryKey(Integer ddcode);

    RoundMatch selectByRoundGame(@Param("ddround") String ddRound, @Param("ddgame") Integer ddgame);

    int updateByPrimaryKeySelective(RoundMatch record);

    int updateByPrimaryKey(RoundMatch record);

    List<RoundMatch> selectAll();
}