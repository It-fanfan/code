package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RankingRecord;

import java.util.List;

public interface RankingRecordMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(RankingRecord record);

    int insertSelective(RankingRecord record);

    RankingRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RankingRecord record);

    int updateByPrimaryKey(RankingRecord record);

    List<RankingRecord> selectAll();

}