package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundExt;
import org.apache.ibatis.annotations.Param;

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

    List<RoundExt> selectByTimes(@Param("start") String start, @Param("end") String end);

    List<RoundExt> selectAllS();

    List<RoundExt> selectAllG();

    int selectSMaxId();

    int selectGMaxId();
}