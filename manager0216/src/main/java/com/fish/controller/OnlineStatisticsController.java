package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/manage")
public class OnlineStatisticsController
{

    @Autowired
    OnlineService onlineService;

    //查询在线情况
    @ResponseBody
    @GetMapping(value = "/online")
    public GetResult getOnline(GetParameter parameter)
    {
        return onlineService.findAll(parameter);
    }
}
