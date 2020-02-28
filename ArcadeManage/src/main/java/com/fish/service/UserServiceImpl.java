package com.fish.service;

import com.fish.dao.primary.mapper.ManageAccountMapper;
import com.fish.dao.primary.model.ManageAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService
{
    @Autowired
    private ManageAccountMapper userMapper;

    @Override
    public ManageAccount selectByUserName(String userName)
    {
        return userMapper.selectByUserName(userName);
    }
}
