package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeProductInfo;

import java.util.List;

public interface ArcadeProductInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArcadeProductInfo record);

    int insertSelective(ArcadeProductInfo record);

    ArcadeProductInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArcadeProductInfo record);

    int updateByPrimaryKey(ArcadeProductInfo record);

    List<ArcadeProductInfo> selectAll();
}