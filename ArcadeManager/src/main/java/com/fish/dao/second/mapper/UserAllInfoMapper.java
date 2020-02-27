package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserAllInfo;
import org.apache.ibatis.annotations.Param;

public interface UserAllInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAllInfo record);

    int insertSelective(UserAllInfo record);

    UserAllInfo selectByPrimaryKey(Integer id);

    UserAllInfo selectByUid(@Param("dduid")String ddUid,@Param("tableName")String tableName);

    int updateByPrimaryKeySelective(UserAllInfo record);

    int updateByPrimaryKey(UserAllInfo record);
}