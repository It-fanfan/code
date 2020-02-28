package com.fish.controller;

import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.OnlineStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class OnlineStatisticsController {

    @Autowired
    OnlineStatisticsService onlineStatisticsService;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/onlinestatistics")
    public GetResult getOnlineStatistics(GetParameter parameter) {
        return onlineStatisticsService.findAll(parameter);
    }

    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/onlinestatistics/new")
    public PostResult insertOnlineStatistics(@RequestBody OnlineStatistics productInfo) {
        PostResult result = new PostResult();
        int count = onlineStatisticsService.insert(productInfo);
        if (count == 1) {
//            JSONObject paramMap = JSONObject.fromObject(productInfo);
//            String res= HttpUtil.post("/flush/logic/persie_deamon", paramMap);
//            System.out.println("前台业务返回值 :"+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改产品信息
    @ResponseBody
    @PostMapping(value = "/onlinestatistics/edit")
    public PostResult modifyOnlineStatistics(@RequestBody OnlineStatistics productInfo) {
        PostResult result = new PostResult();
        int count = onlineStatisticsService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
//            JSONObject paramMap = JSONObject.fromObject(productInfo);
//            String res= HttpUtil.post("/flush/logic/persie_deamon", paramMap);
//            System.out.println("前台业务返回值 :"+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
