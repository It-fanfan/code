package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class GamesSetController {

    @Autowired
    GamesSetService gamesSetService;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/gameset")
    public GetResult getGameSet(GetParameter parameter) {
        return gamesSetService.findAll(parameter);
    }

    //查询展示所有合集信息
    @ResponseBody
    @GetMapping(value = "/gamesetInfo")
    public List<ArcadeGameSet> getGameInfo(GetParameter parameter) {
        return gamesSetService.selectAll(parameter);
    }

    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/gameset/new")
    public PostResult insertGameSet(@RequestBody ArcadeGameSet productInfo) {
        PostResult result = new PostResult();

           int count = gamesSetService.insert(productInfo);
        if (count == 1) {
//            com.alibaba.fastjson.JSONObject paramMap = new com.alibaba.fastjson.JSONObject();
//            paramMap.put("name","gameset");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
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
    @PostMapping(value = "/gameset/edit")
    public PostResult modifyGameSet(@RequestBody ArcadeGameSet productInfo) {
        PostResult result = new PostResult();
       // int count = 1;
       int count = gamesSetService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
//            com.alibaba.fastjson.JSONObject paramMap = new JSONObject();
//            paramMap.put("name","gameset");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
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
