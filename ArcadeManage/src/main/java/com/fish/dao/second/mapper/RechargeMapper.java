package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Recharge;

import java.util.List;

public interface RechargeMapper
{
    int deleteByPrimaryKey(String ddid);

    int insert(Recharge record);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(String ddid);

    int updateByPrimaryKeySelective(Recharge record);

    int updateByPrimaryKey(Recharge record);

    List<Recharge> selectAll();

    //查询提现成功订单
    List<Recharge> selectAllCharged();
}