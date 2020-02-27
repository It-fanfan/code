package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ProductJson;
import com.fish.dao.primary.model.ProductRemain;
import java.util.Date;

public interface ProductProfileMapper {

    int insertSelective(ProductRemain record);

    ProductRemain selectByPrimaryKey(Date times);
    //获取json信息插入product_frofile表
    int insertJson(ProductJson record);

    //通过日期和appId匹配活跃用户数
    Integer searchActiveUser(Date newDay, String appId);
}