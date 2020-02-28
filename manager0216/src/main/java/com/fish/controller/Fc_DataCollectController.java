package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.DataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/manage")
public class Fc_DataCollectController {

    @Autowired
    DataCollectService fcDataCollectService;

    //查询数据汇总信息
    @ResponseBody
    @GetMapping(value = "/datacollect")
    public GetResult getDataCollect(GetParameter parameter) {
        return fcDataCollectService.findAll(parameter);
    }


    //刷新数据汇总信息
    @ResponseBody
    @GetMapping(value = "/datacollect/flush")
    public PostResult flushDataCollect(GetParameter parameter) {
        PostResult result = new PostResult();
        int count = fcDataCollectService.flushAll();
        if (count != 1) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    //搜索数据汇总信息
    @ResponseBody
    @PostMapping(value = "/datacollect/search")
    public GetResult searchData(HttpServletRequest request, GetParameter parameter) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String type = request.getParameter("type");
        return fcDataCollectService.searchData(beginDate, endDate, type, parameter);
    }

}
