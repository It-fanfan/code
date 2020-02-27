package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ProductInfo;
import com.fish.dao.primary.model.ProductJson;

import java.util.List;

public interface ProductInfoMapper {
    //查询所有产品信息列表
    List<ProductInfo> selectAll();
    //新增产品信息
    int insert(ProductInfo record);
    //修改产品信息
    int updateProductInfo(ProductInfo productInfo);

    List<String> selectPlatForm();

    int deleteProductInfo(ProductInfo productInfo);
}
