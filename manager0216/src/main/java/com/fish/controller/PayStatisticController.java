package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.PayStatisticService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manage")
public class PayStatisticController
{

    @Autowired
    PayStatisticService payStatisticService;
    @Autowired
    BaseConfig baseConfig;
    //查询展示所有游戏信息
    @GetMapping(value = "/statistic")
    public GetResult getGames(GetParameter parameter)
    {
        return payStatisticService.findAll(parameter);
    }

}
