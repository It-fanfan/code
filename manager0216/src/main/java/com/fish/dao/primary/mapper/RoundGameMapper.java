package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundGame;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundGameMapper
{
    int deleteByPrimaryKey(Integer ddcode);

    int insert(RoundGame record);

    int insertSelective(RoundGame record);

    RoundGame selectByPrimaryKey(Integer ddcode);

    int updateByPrimaryKeySelective(RoundGame record);

    int updateByPrimaryKey(RoundGame record);

    List<RoundGame> selectAll();

    RoundGame selectByRoundGame(@Param("ddgame") Integer ddgame, @Param("ddround") String ddround);
}