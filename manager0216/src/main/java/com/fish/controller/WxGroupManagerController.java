package com.fish.controller;

import com.fish.dao.second.model.WxGroupManager;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxGroupManagerService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class WxGroupManagerController {

    @Autowired
    WxGroupManagerService wxGroupManagerService;

    @Autowired
    BaseConfig baseConfig;

    @ResponseBody
    @GetMapping(value = "/wxgroup")
    public GetResult getWxGroupManager(GetParameter parameter){
        GetResult result = wxGroupManagerService.findAll(parameter);
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/wxgroup/edit")
    public PostResult modifyWxConfig(@RequestBody WxGroupManager productInfo) {
        PostResult result = new PostResult();

       int count = wxGroupManagerService.updateWxGroupManager(productInfo);
       if (count == 0){
           result.setSuccessed(false);
           result.setMsg("操作失败");
       }else {
           wxGroupManagerService.updateConfigConfirm(productInfo);
       }


        return result;
    }
}
