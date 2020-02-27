package com.fish.controller;

import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class GlobalConfigController {

    @Autowired
    GlobalConfigService globalConfigService;


    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/globalConfig")
    public GetResult getGlobalConfig(GetParameter parameter) {
        return globalConfigService.findAll(parameter);

    }
    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/globalConfig/new")
    public PostResult insertGlobalConfig(@RequestBody GoodsValue goodsValue) {
        PostResult result = new PostResult();
        int count = globalConfigService.insert(goodsValue);
        if (count == 1) {
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
    @PostMapping(value = "/globalConfig/edit")
    public PostResult modifyGlobalConfig(@RequestBody GoodsValue goodsValue) {
        PostResult result = new PostResult();
        int count = globalConfigService.updateByPrimaryKeySelective(goodsValue);
        if (count != 0) {
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
