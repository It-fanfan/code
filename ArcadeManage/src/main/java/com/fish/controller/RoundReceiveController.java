package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.RoundReceive;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RoundReceiveService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/manage")
public class RoundReceiveController
{

    @Autowired
    RoundReceiveService roundReceiveService;
    @Autowired
    BaseConfig baseConfig;

    //查询展示比赛结果
    @ResponseBody
    @GetMapping(value = "/roundreceive")
    public GetResult getRoundreceive(GetParameter parameter)
    {
        return roundReceiveService.findAll(parameter);
    }

    //新增比赛结果
    @ResponseBody
    @PostMapping(value = "/roundreceive/new")
    public PostResult insertRoundreceive(@RequestBody RoundReceive productInfo)
    {
        PostResult result = new PostResult();

        int count = roundReceiveService.insert(productInfo);
        if (count == 1)
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

    @ResponseBody
    @GetMapping(value = "/roundreceive/result")
    public GetResult searchRoundReceive(HttpServletRequest request, GetParameter parameter)
    {
        JSONObject search = new JSONObject();
        String times = request.getParameter("times");
        String userName = request.getParameter("userName");
        String gameCode = request.getParameter("gameCode");
        String roundCode = request.getParameter("roundCode");
        String ddGroup = request.getParameter("ddGroup");

        if (times != null && times.length() > 0)
        {
            search.put("times", times);
        }
        if (userName != null && userName.length() > 0)
        {
            search.put("userName", userName);
        }
        if (gameCode != null && gameCode.length() > 0)
        {
            search.put("gameCode", gameCode);
        }
        if (roundCode != null && roundCode.length() > 0)
        {
            search.put("roundCode", roundCode);
        }
        if (ddGroup != null && ddGroup.length() > 0)
        {
            search.put("ddGroup", ddGroup);
        }
//        Enumeration<String> keys = request.getParameterNames();
//        while (keys.hasMoreElements())
//        {
//            String key = keys.nextElement();
//            search.put(key, request.getParameter(key));
//        }
        if (search.size() > 0)
            parameter.setSearchData(search.toJSONString());
        return roundReceiveService.findAll(parameter);
    }

    //修改比赛结果
    @ResponseBody
    @PostMapping(value = "/roundreceive/edit")
    public PostResult modifyRoundreceive(@RequestBody RoundReceive productInfo)
    {
        PostResult result = new PostResult();
        int count = roundReceiveService.updateByPrimaryKeySelective(productInfo);
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
