package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Recharge;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
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


    int selectCashOut(String SQL);

    //计算单个用户总的提现金额
    BigDecimal selectRecharged(String SQL);
    List<Recharge> selectRechargeByUid(String uid);
    //查询提现成功订单
    List<Recharge> selectAllCharged();
    //查询提现记录
    List<Recharge> selectAllChargeSQL(String sql);
    List<Recharge> selectChargedByTime(@Param("start") String start, @Param("end") String end);
    List<Recharge> selectByTime(@Param("start") String start, @Param("end") String end);

}