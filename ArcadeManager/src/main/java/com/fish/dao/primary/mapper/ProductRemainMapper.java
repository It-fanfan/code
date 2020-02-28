package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ProductRemain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Mapper
@Repository
public interface ProductRemainMapper {

    //获取新增用户数计算留存
    HashSet<String> getNewUserInfo(@Param("newDay") Date searchTime,@Param("appId") String appId);

    //获取活跃用户数计算留存
    HashSet<String> getAcUserInfo(@Param("newDay") Date searchTime,@Param("appId") String appId);


    //查询所有新增用户并插入
    List<ProductRemain> selectAddUser();

    //插入所有新增用户并插入
    void insertData(ProductRemain addRemains);

    //查询所有活跃用户
    List<ProductRemain> selectActiveUser();

    List<ProductRemain> selectAllTimeAndAppId();

    List<ProductRemain> selectAll();
}
