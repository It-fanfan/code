package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Ranking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(Ranking record);

    int insertSelective(Ranking record);

    Ranking selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Ranking record);

    int updateByPrimaryKey(Ranking record);

    int insertBatch(List<Ranking> rankings);

    Ranking selectByGameCode(int gameCode);


    List<Ranking> selectByAllDate();

    List<Ranking> selectByAllDateProgram();

    List<Ranking> selectByDate(String timestamp);

    List<Ranking> selectByDateGroup(String timestamp);

    List<Ranking> selectResult(@Param("matchid") String matchId, @Param("matchdate") String matchDate, @Param("gamecode") int gameCode, @Param("matchindex") int matchIndex);

    List<Ranking> selectGroupResult(@Param("matchid") String matchId, @Param("matchdate") String matchDate, @Param("gamecode") int gameCode, @Param("matchindex") int matchIndex);

    Ranking selectIsSameData(@Param("matchid") String matchId, @Param("uid") String uid, @Param("ranking") Long ranking, @Param("mark") long mark, @Param("matchindex") int matchindex, @Param("gamecode") int gamecode, @Param("matchdate") String endTime);


}