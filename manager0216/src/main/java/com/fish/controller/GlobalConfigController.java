package com.fish.controller;

import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GlobalConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class GlobalConfigController {

    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    BaseConfig baseConfig;

    //查询全局配置
    @ResponseBody
    @GetMapping(value = "/globalConfig")
    public GetResult getGlobalConfig(GetParameter parameter) {
        return globalConfigService.findAll(parameter);
    }

    //新增全局配置
    @ResponseBody
    @PostMapping(value = "/globalConfig/new")
    public PostResult insertGlobalConfig(@RequestBody GoodsValue goodsValue) {
        PostResult result = new PostResult();
        int count = globalConfigService.insert(goodsValue);
        if (count == 1) {
            String res = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushCache());
            String pubRes = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushPublicCache());
            result.setMsg("操作成功" + res + pubRes);
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    //修改全局配置
    @ResponseBody
    @PostMapping(value = "/globalConfig/edit")
    public PostResult modifyGlobalConfig(@RequestBody GoodsValue goodsValue) {
        PostResult result = new PostResult();
        int count = globalConfigService.updateByPrimaryKeySelective(goodsValue);
        if (count != 0) {
            String res = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushCache());
            String pubRes = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushPublicCache());
            result.setMsg("操作成功" + res + pubRes);
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/globalConfig/delete")
    public PostResult deleteGoodsValue(@RequestBody GoodsValue goodsValue) {
        PostResult result = new PostResult();
        int count = globalConfigService.deleteSelective(goodsValue);
        if (count != 0) {
            String res = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushCache());
            String pubRes = ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushPublicCache());
            result.setMsg("操作成功" + res + pubRes);
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
