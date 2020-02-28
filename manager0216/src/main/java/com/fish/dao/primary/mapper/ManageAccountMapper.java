package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ManageAccount;

import java.util.List;

public interface ManageAccountMapper
{
    int deleteByPrimaryKey(String username);

    int insert(ManageAccount record);

    int insertSelective(ManageAccount record);

    ManageAccount selectByPrimaryKey(String username);

    int updateByPrimaryKeySelective(ManageAccount record);

    int updateByPrimaryKey(ManageAccount record);

    ManageAccount login(ManageAccount user);

    ManageAccount selectByUserName(String userName);

    List<ManageAccount> selectAll();
}