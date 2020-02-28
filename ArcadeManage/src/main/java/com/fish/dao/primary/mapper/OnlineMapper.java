package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Online;

import java.util.List;

public interface OnlineMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(Online record);

    int insertSelective(Online record);

    Online selectByPrimaryKey(Long id);

    List<Online> selectCurrent(String SQL);

    int updateByPrimaryKeySelective(Online record);

    int updateByPrimaryKey(Online record);
}