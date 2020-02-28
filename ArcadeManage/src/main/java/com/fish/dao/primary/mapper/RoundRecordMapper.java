package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundRecord;
import com.fish.dao.primary.model.RoundRecordKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundRecordMapper
{
    int deleteByPrimaryKey(RoundRecordKey key);

    int insert(RoundRecord record);

    int insertSelective(RoundRecord record);

    RoundRecord selectByPrimaryKey(RoundRecordKey key);

    int updateByPrimaryKeySelective(RoundRecord record);

    int updateByPrimaryKey(RoundRecord record);


    List<RoundRecord> selectAllRank();

    List<RoundRecord> selectGRank();
    List<RoundRecord> selectGRankTime(@Param("start") String start, @Param("end") String end);

    List<RoundRecord> selectSRank();

    List<RoundRecord> selectSRankTime(@Param("start") String start, @Param("end") String end);
}