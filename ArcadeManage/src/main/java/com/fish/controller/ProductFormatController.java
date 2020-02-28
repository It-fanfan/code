package com.fish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ProductFormat;
import com.fish.dao.primary.model.Rounds;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ProductFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class ProductFormatController {

    @Autowired
    ProductFormatService productFormatService;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/productformat")
    public GetResult getProductFormat(GetParameter parameter) {
        return productFormatService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/productformat/rounds")
    public List<Rounds> getRounds(GetParameter parameter) {
        return productFormatService.selectAllCode(parameter);
    }

    //新增产品赛制信息
    @ResponseBody
    @PostMapping(value = "/productformat/new")
    public PostResult insertProductFormat(@RequestBody Rounds productInfo) {
        PostResult result = new PostResult();

        int count = productFormatService.insert(productInfo);
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","game_round");
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

    //修改产品信息
    @ResponseBody
    @PostMapping(value = "/productformat/edit")
    public PostResult modifyProductFormat(@RequestBody Rounds productInfo) {
        PostResult result = new PostResult();
        int count = productFormatService.updateByPrimaryKeySelective(productInfo);
        //int count =1;
        if (count != 0) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","game_round");
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
