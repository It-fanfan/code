package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ProductShow;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductShowMapper {
    //查询展示所有产品概况信息
    List<ProductShow> showProductProfile();

    //根据时间，产品名称，平台，查询展示信息
    List<ProductShow> showSearchProductProfile(@Param("newDay") Date searchTime,@Param("productName") String productName,@Param("platForm") String platform);


    List<ProductShow> selectShowProduct();

    void insertShowProduct(ProductShow productShows);
}
