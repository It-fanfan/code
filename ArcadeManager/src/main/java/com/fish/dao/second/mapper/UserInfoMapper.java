package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}