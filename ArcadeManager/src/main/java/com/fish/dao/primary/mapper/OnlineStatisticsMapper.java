package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.protocols.GetParameter;

import java.util.List;

public interface OnlineStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OnlineStatistics record);

    int insertSelective(OnlineStatistics record);

    OnlineStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OnlineStatistics record);

    int updateByPrimaryKey(OnlineStatistics record);

    List<OnlineStatistics> selectAll(GetParameter parameter);
}