package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGameSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArcadeGameSetMapper
{

    List<ArcadeGameSet> selectAll();

    List<ArcadeGameSet> selectAllByDesc();

    List<ArcadeGameSet> selectAllByAsc();

    int deleteByPrimaryKey(Integer id);

    int insert(ArcadeGameSet record);

    int insertSelective(ArcadeGameSet record);

    ArcadeGameSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ArcadeGameSet record);

    int updateByPrimaryKeySelective(ArcadeGameSet record);
}