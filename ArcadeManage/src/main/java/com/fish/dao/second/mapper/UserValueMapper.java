package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserValue;

import java.util.List;

public interface UserValueMapper
{
    List<UserValue> selectAll();

    int deleteByPrimaryKey(String dduid);

    int insert(UserValue record);

    int insertSelective(UserValue record);

    UserValue selectByPrimaryKey(String dduid);

    int updateByPrimaryKeySelective(UserValue record);

    int updateByPrimaryKey(UserValue record);
}