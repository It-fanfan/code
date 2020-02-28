package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundExt;

import java.util.List;

public interface RoundExtMapper
{
    int deleteByPrimaryKey(String ddcode);

    int insert(RoundExt record);

    int insertSelective(RoundExt record);

    RoundExt selectByddCode(String ddcode);

    RoundExt selectByddCodeS(String ddcode);

    RoundExt selectByddCodeG(String ddcode);

    int updateByPrimaryKeySelective(RoundExt record);

    int updateByPrimaryKey(RoundExt record);

    List<RoundExt> selectAll();

    List<RoundExt> selectAllS();

    List<RoundExt> selectAllG();

    int selectSMaxId();

    int selectGMaxId();
}