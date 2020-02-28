package com.fish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.GameDayInfo;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GameDayInfoService;
import com.fish.service.GroupFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manage")
public class GroupGameDayInfoController {

    @Autowired
    GameDayInfoService gameDayInfoService;
    @Autowired
    GroupFormatService groupFormatService;

    //查询展示所有群赛制配置
    @ResponseBody
    @GetMapping(value = "/groupformat")
    public GetResult getGroupFormat(GetParameter parameter) {
        return groupFormatService.findAll(parameter);
    }

    //新增群赛制配置信息
    @ResponseBody
    @PostMapping(value = "/groupformat/new")
    public PostResult insertGroupFormat(@RequestBody GameDayInfo productInfo) {
        PostResult result = new PostResult();
        String ddname = productInfo.getDdname();

        int count = groupFormatService.insert(productInfo);
      // count =1;
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","rounds");
//            String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
//            result.setCode(200);
//            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改群赛制配置
    @ResponseBody
    @PostMapping(value = "/groupformat/edit")
    public PostResult modifyGroupFormat(@RequestBody GameDayInfo productInfo) {
        PostResult result = new PostResult();
      //  int count =1;
       int count = groupFormatService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","rounds");
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
