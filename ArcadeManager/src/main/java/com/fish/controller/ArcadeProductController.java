package com.fish.controller;

import com.fish.dao.primary.model.ArcadeProductInfo;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ArcadeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class ArcadeProductController {
    @Autowired
    ArcadeProductService productService;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/productInfo")
    public GetResult getProductInfo(GetParameter parameter) {
        return productService.findAll(parameter);
    }


    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/productInfo/new")
    public PostResult insertProductInfo(@RequestBody ArcadeProductInfo productInfo) {
        PostResult result = new PostResult();
        int count = productService.insert(productInfo);
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
    @PostMapping(value = "/productInfo/edit")
    public PostResult modifyProductInfo(@RequestBody ArcadeProductInfo productInfo) {
        PostResult result = new PostResult();
        int count = productService.updateByPrimaryKeySelective(productInfo);
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
