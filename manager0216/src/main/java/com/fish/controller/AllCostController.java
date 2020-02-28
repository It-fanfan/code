package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.AllCostService;
import com.fish.service.MatchCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/allCost")
public class AllCostController
{
    @Autowired
    AllCostService allCostService;
    @Autowired
    MatchCostService matchCostService;

    @ResponseBody
    @GetMapping
    public GetResult getAllCost(GetParameter parameter)
    {
        return allCostService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/findMatch")
    public GetResult getMatchCost(GetParameter parameter)
    {
        return matchCostService.findAll(parameter);
    }
}
