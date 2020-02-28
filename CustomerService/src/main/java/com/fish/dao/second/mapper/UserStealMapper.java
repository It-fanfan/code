package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserSteal;
import com.fish.dao.second.model.UserStealKey;

public interface UserStealMapper {
    int deleteByPrimaryKey(UserStealKey key);

    int insert(UserSteal record);

    int insertSelective(UserSteal record);

    UserSteal selectByPrimaryKey(UserStealKey key);

    int updateByPrimaryKeySelective(UserSteal record);

    int updateByPrimaryKey(UserSteal record);
}