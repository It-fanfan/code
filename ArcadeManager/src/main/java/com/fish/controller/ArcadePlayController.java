package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGames;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class ArcadePlayController {

    @Autowired
    GamesService gamesService;


    //查询展示所有产品信息
    @ResponseBody
    @GetMapping(value = "/play")
    public GetResult getGames(GetParameter parameter) {
        return gamesService.findAll(parameter);
    }
    //新增产品信息
    @ResponseBody
    @PostMapping(value = "/play/new")
    public PostResult insertGames(@RequestBody ArcadeGames productInfo) {
        System.out.println("---------------------");
        PostResult result = new PostResult();
        int count = gamesService.insertGameInfo(productInfo);
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","games");
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
    @PostMapping(value = "/play/edit")
    public PostResult modifyGames(@RequestBody ArcadeGames productInfo) {
        PostResult result = new PostResult();

        int count = gamesService.updateGameBySelective(productInfo);
        if (count != 0) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","games");
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
