package com.fish.controller;

import com.fish.dao.primary.model.SupplementOrder;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.SupplementOrderService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class SupplementOrderController
{

    @Autowired
    SupplementOrderService supplementOrderService;
    @Autowired
    BaseConfig baseConfig;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/supplementorder")
    public GetResult getSupplementOrder(GetParameter parameter)
    {
        return supplementOrderService.findAll(parameter);
    }

    //新增产品赛制信息
    @ResponseBody
    @PostMapping(value = "/supplementorder/new")
    public PostResult insertSupplementOrder(@RequestBody SupplementOrder productInfo)
    {
        PostResult result = new PostResult();

        int count = supplementOrderService.insert(productInfo);
        if (count == 1)
        {
            String res = ReadJsonUtil.flushTable("user_value", baseConfig.getFlushCache());
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
    
}
