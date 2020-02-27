package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundGroup;

public interface RoundGroupMapper
{
    int deleteByPrimaryKey(Integer ddcode);

    int insert(RoundGroup record);

    int insertSelective(RoundGroup record);

    RoundGroup selectByPrimaryKey(Integer ddcode);

    int updateByPrimaryKeySelective(RoundGroup record);

    int updateByPrimaryKey(RoundGroup record);
}