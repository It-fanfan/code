package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundReceive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundReceiveMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(RoundReceive record);

    int insertSelective(RoundReceive record);

    RoundReceive selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoundReceive record);

    int updateByPrimaryKey(RoundReceive record);

    List<RoundReceive> selectAll();
    List<RoundReceive> selectSearchTime(@Param("start") String start, @Param("end") String end);

    List<RoundReceive> selectBySearchData(String SQL);
}