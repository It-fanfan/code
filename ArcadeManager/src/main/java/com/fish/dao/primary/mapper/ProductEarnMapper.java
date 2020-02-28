package com.fish.dao.primary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fish.dao.primary.model.ProductEarn;

import java.util.List;

public interface ProductEarnMapper extends BaseMapper {

    List<ProductEarn> selectAll();

    //查询所有进入次数
    List<ProductEarn> selectInNumbers();

    //查询付费用户数
    List<ProductEarn> selectPayUser();

    //查询内购收益
    List<ProductEarn> selectInCost();

    //查询人均在线时长
    List<ProductEarn> selectOnlineAvg();

    //插入ProductEarn
    void insertEarnData(ProductEarn productEarn);
    //插入内购收益

    //插入人均在线时长
}
