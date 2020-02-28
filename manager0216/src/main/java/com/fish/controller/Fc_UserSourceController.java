package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/manage")
public class Fc_UserSourceController
{

    @Autowired
    ProductDataService fcProductDataService;

    //查询用户来源信息
    @ResponseBody
    @GetMapping(value = "/usersource")
    public GetResult getUserSource(GetParameter parameter)
    {
        return fcProductDataService.findAll(parameter);
    }

    //搜索用户来源信息
    @ResponseBody
    @PostMapping(value = "/usersource/search")
    public GetResult searchUserSource(HttpServletRequest request, GetParameter parameter)
    {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("productName");
        return fcProductDataService.searchProductData(beginDate, endDate, productName, parameter);

    }

}
