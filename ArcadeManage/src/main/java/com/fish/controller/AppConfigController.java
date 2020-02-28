package com.fish.controller;

import com.fish.dao.second.model.AppConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.AppConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class AppConfigController
{

    @Autowired
    AppConfigService appConfigService;
    @Autowired
    BaseConfig baseConfig;

    //查询展示所有Appconfig信息
    @ResponseBody
    @GetMapping(value = "/appconfig")
    public GetResult getAppConfig(GetParameter parameter)
    {
        return appConfigService.findAll(parameter);
    }

    //新增游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/appconfig/new")
    public PostResult insertAppConfig(@RequestBody AppConfig productInfo)
    {
        PostResult result = new PostResult();
        int count = appConfigService.insert(productInfo);
        if (count == 1)
        {
            String res = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
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

    //修改游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/appconfig/edit")
    public PostResult modifyAppConfig(@RequestBody AppConfig productInfo)
    {
        PostResult result = new PostResult();
        int count = appConfigService.updateByPrimaryKeySelective(productInfo);
        if (count != 0)
        {
            String res = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
