package com.fish.service;

import com.fish.dao.primary.mapper.UserMapper;
import com.fish.dao.primary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByUserName(String userName)
    {
        return userMapper.selectByUserName(userName);
    }
}
