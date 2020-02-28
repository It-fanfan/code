package com.fish.dao.second.mapper;

import com.fish.dao.second.model.GoodsValue;

import java.util.List;

public interface GoodsValueMapper
{
    int deleteByPrimaryKey(Integer ddid);

    int insert(GoodsValue record);

    int insertSelective(GoodsValue record);

    GoodsValue selectByPrimaryKey(Integer ddid);

    int updateByPrimaryKeySelective(GoodsValue record);

    int updateByPrimaryKey(GoodsValue record);

    List<GoodsValue> selectAll();
}