package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.GameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/game")
public class GameInfoController {


    @Autowired
    GameInfoService gameInfoService;

    @ResponseBody
    @GetMapping(value = "/Info")
    public GetResult getGameSet(GetParameter parameter) {
        return gameInfoService.findAll(parameter);
    }


    @ResponseBody
    @PostMapping("/uploadImage")
    public JSONObject uploadExcel(@RequestParam("QRCode") MultipartFile QRCode, @RequestParam("openId") String openId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        return jsonObject;
    }


}
