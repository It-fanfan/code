package com.fish.dao.second.mapper;

import com.fish.dao.second.model.GroupMatch;

public interface GroupMatchMapper
{
    int deleteByPrimaryKey(String ddid);

    int insert(GroupMatch record);

    int insertSelective(GroupMatch record);

    GroupMatch selectByPrimaryKey(String ddid);

    int updateByPrimaryKeySelective(GroupMatch record);

    int updateByPrimaryKey(GroupMatch record);

}