package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.BuyPay;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BuyPayMapper
{
    int deleteByPrimaryKey(@Param("buyDate") String date, @Param("buyAppId") String appId);

    int insert(BuyPay record);

    int insertSelective(BuyPay record);

    BuyPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BuyPay record);

    int updateByPrimaryKey(BuyPay record);

    BuyPay selectByAppIdAndDate(@Param("buyDate") String date, @Param("buyAppId") String appId);

    BigDecimal selectCountBuyCost(String date);

    List<BuyPay> selectAll();

    void insertBatch(List<BuyPay> lists);

    List<BuyPay> selectSearch(String SQL);
}