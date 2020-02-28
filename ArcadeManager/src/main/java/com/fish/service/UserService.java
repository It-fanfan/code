package com.fish.service;

import com.fish.dao.primary.model.User;

public interface UserService
{
    User selectByUserName(String userName);
}
