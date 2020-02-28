package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Orders;

import java.util.List;

public interface OrdersMapper
{
    int deleteByPrimaryKey(String ddid);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(String ddid);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> selectAll();
}