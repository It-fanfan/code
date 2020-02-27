package com.fish.dao.second.mapper;

import com.fish.dao.second.model.GoodValueInfo;

import java.util.List;

public interface GoodValueInfoMapper
{

    GoodValueInfo selectByGoodsId(Integer goodsId);

    List<GoodValueInfo> selectAll();
}
