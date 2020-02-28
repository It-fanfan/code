package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.UserInfoService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/manage")
public class UserInfoController
{

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    BaseConfig baseConfig;

    //展示用户信息
    @ResponseBody
    @GetMapping(value = "/userinfo")
    public GetResult getUserInfo(GetParameter parameter)
    {
        return userInfoService.findAll(parameter);
    }

}
