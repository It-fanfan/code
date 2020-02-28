package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.CheckBoxData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/manage")
public class GamesController {

    @Autowired
    GamesService gamesService;


    //查询展示所有产品信息
    @GetMapping(value = "/games")
    public GetResult getGames(GetParameter parameter) {
        return gamesService.findAll(parameter);
    }
    //查询展示所有checkBoc信息
    @GetMapping(value = "/games/selectBox")
    public List<CheckBoxData> getGamesBox(GetParameter parameter) {

        List<ArcadeGames> arcadeGames = gamesService.selectAll(parameter);
        List<CheckBoxData> checkBoxDatas =new ArrayList<>();
        for (ArcadeGames arcadeGame : arcadeGames) {
            CheckBoxData checkBoxData = new CheckBoxData();
            String name = arcadeGame.getDdname128u();
            Integer code = arcadeGame.getDdcode();
            checkBoxData.setTitle(name+"("+code+")");
            checkBoxData.setValue(code);
            checkBoxDatas.add(checkBoxData);
        }
        return checkBoxDatas;
    }


    //新增产品信息
    @PostMapping(value = "/games/new")
    public PostResult insertGames(@RequestBody ArcadeGames productInfo) {
        System.out.println("---------------------");
        PostResult result = new PostResult();
        int count = gamesService.insert(productInfo);
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

    @PostMapping(value = "/games/delete")
    public PostResult deleteGames(@RequestBody ArcadeGames productInfo) {
        PostResult result = new PostResult();
        int count = 1;
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
    @PostMapping(value = "/games/edit")
    public PostResult modifyGames(@RequestBody ArcadeGames productInfo) {
        PostResult result = new PostResult();

        int count = gamesService.updateByPrimaryKeySelective(productInfo);
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
