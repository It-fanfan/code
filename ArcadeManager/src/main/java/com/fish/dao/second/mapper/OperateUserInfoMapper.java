package com.fish.dao.second.mapper;

import com.fish.dao.second.model.InitConfig;
import com.fish.dao.second.model.OperateUserInfo;

import java.util.List;

public interface OperateUserInfoMapper {


    List<OperateUserInfo> selectAll();
    int deleteByPrimaryKey(Long id);

    int insert(OperateUserInfo record);

    int insertSelective(OperateUserInfo record);

    OperateUserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperateUserInfo record);

    int updateByPrimaryKey(OperateUserInfo record);
}