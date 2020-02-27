package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.User;

import java.util.List;

public interface UserMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(User user);

    User selectByUserName(String userName);

    List<User> selectAll();
}