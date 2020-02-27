package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGameConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GameConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class GameConfigController {

    @Autowired
    GameConfigService gameConfigService;

    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/gameConfig")
    public GetResult getGameConfig(GetParameter parameter) {
        return gameConfigService.findAll(parameter);
    }

    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/gameConfig/new")
    public PostResult insertGameConfig(@RequestBody ArcadeGameConfig productInfo) {
        PostResult result = new PostResult();
        int count = gameConfigService.insert(productInfo);
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
    @PostMapping(value = "/gameConfig/edit")
    public PostResult modifyGameConfig(@RequestBody ArcadeGameConfig productInfo) {
        PostResult result = new PostResult();
        int count = gameConfigService.updateByPrimaryKeySelective(productInfo);
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
