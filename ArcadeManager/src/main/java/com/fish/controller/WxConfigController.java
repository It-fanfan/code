package com.fish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.InitConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class WxConfigController {

    @Autowired
    WxConfigService WxConfigService;

    //查询展示所有wxconfig信息
    @ResponseBody
    @GetMapping(value = "/wxconfig")
    public GetResult getGameConfig(GetParameter parameter) {
        return WxConfigService.findAll(parameter);
    }

    //新增游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/new")
    public PostResult insertGameConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = WxConfigService.insert(productInfo);
        if (count == 1) {
//           JSONObject paramMap = new JSONObject();
//            paramMap.put("name","init_config");
//            String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/edit")
    public PostResult modifyGameConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = WxConfigService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
//            com.alibaba.fastjson.JSONObject paramMap = new JSONObject();
//            paramMap.put("name","init_config");
//            String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
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
