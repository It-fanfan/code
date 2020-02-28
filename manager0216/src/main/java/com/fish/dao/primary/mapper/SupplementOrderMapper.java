package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.SupplementOrder;

import java.util.List;

public interface SupplementOrderMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(SupplementOrder record);

    int insertSelective(SupplementOrder record);

    SupplementOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SupplementOrder record);

    int updateByPrimaryKey(SupplementOrder record);

    List<SupplementOrder> selectAll();

}