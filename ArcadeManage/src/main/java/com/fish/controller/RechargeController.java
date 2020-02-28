package com.fish.controller;

import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/manage")
public class RechargeController
{

    @Autowired
    RechargeService rechargeService;

    //查询提现情况
    @GetMapping(value = "/recharge")
    public GetResult getRecharge(GetParameter parameter)
    {
        return rechargeService.findAll(parameter);
    }

    //审核提现接口
    @PostMapping(value = "/recharge/audit")
    public PostResult auditRecharge(@RequestBody List<Recharge> productInfo)
    {
        PostResult result = new PostResult();
        int cash = rechargeService.getCash(productInfo);
        if (cash == 200)
        {
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("未完全提现成功，请联系管理员");
            return result;
        }
    }

    //提现信息
    @PostMapping(value = "/recharge/edit")
    public PostResult modifyRecharge(@RequestBody Recharge productInfo)
    {
        PostResult result = new PostResult();
        int count = rechargeService.updateByPrimaryKeySelective(productInfo);
        if (count != 0)
        {
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
