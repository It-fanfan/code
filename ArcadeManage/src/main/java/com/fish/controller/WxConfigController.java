package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class WxConfigController
{

    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    BaseConfig baseConfig;

    //查询展示所有wxconfig信息
    @ResponseBody
    @GetMapping(value = "/wxconfig")
    public GetResult getWxConfig(GetParameter parameter)
    {
        return wxConfigService.findAll(parameter);
    }


    //获取资源图
    @ResponseBody
    @PostMapping(value = "/wxconfig/flushpicture")
    public PostResult flushResource(@RequestBody JSONObject parameter)
    {
        PostResult result = new PostResult();
        int i = wxConfigService.flushResource(parameter);
        if (i != 0)
        {
            String res = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //新增游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/new")
    public PostResult insertWxConfig(@RequestBody WxConfig productInfo)
    {
        PostResult result = new PostResult();
        int count = wxConfigService.insert(productInfo);
        if (count == 1)
        {
            String resWx = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            String resApp = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else if (count == 3)
        {
            result.setCode(408);
            result.setMsg("AppId重复，操作失败");
            return result;
        } else if (count == 4)
        {
            result.setCode(409);
            result.setMsg("产品名称重复，操作失败");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/wxconfig/edit")
    public PostResult modifyWxConfig(@RequestBody WxConfig productInfo)
    {
        PostResult result = new PostResult();
        int count = wxConfigService.updateByPrimaryKeySelective(productInfo);
        if (count == 1)
        {
            String resWx = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            String resApp = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else if (count == 3)
        {
            result.setCode(408);
            result.setMsg("AppId重复，操作失败");
            return result;
        } else if (count == 4)
        {
            result.setCode(409);
            result.setMsg("产品名称重复，操作失败");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }
}
