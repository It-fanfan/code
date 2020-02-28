package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.RechargedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manage")
public class RechargedController
{
    //提现情况
    @Autowired
    RechargedService rechargedService;


    @GetMapping(value = "/recharged")
    public GetResult getCharged(GetParameter parameter)
    {
        return rechargedService.findAll(parameter);
    }


}
