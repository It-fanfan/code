package com.fish.dao.second.mapper;

import com.fish.dao.second.model.AllCost;
import com.fish.protocols.MatchCost;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AllCostMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(AllCost record);

    int insertSelective(AllCost record);

    AllCost selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AllCost record);

    int updateByPrimaryKey(AllCost record);

    List<AllCost> selectAllCost(@Param("start") String start, @Param("end") String end);

    List<MatchCost> selectBySQL(String SQL);
}