package com.fish.service;

import com.fish.dao.primary.model.ManageAccount;

public interface UserService
{
    ManageAccount selectByUserName(String userName);
}
