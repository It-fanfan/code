package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserApp;
import org.apache.ibatis.annotations.Param;

public interface UserAppMapper
{
    int deleteByPrimaryKey(String ddoid);

    int insert(UserApp record);

    int insertSelective(UserApp record);

    UserApp selectByPrimaryKey(String dduid);

    UserApp selectByOpenId(String ddopenid);

    int updateByPrimaryKeySelective(UserApp record);

    int updateByPrimaryKey(UserApp record);

    UserApp searchOppenId(@Param("dduid") String dduid, @Param("ddappid") String ddappid);
}