package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    @ResponseBody
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @ResponseBody
    @GetMapping("/select")
    public String select()
    {
        return "select";
    }

    @ResponseBody
    @GetMapping("/menu")
    public JSONArray menu()
    {
        JSONArray jsonArray = new JSONArray();
        int index = 0;
        {
            JSONObject parent = new JSONObject();
            parent.put("id",++index);
            parent.put("parentId", 0);
            parent.put("name", "微信官方数据");
            parent.put("url", null);
            jsonArray.add(parent);
        }
        {
            JSONObject wxInput = new JSONObject();
            wxInput.put("id",++index);
            wxInput.put("parentId", 1);
            wxInput.put("name", "微信配置");
            wxInput.put("url", "wxConfig.html");
            jsonArray.add(wxInput);
        }
        {
            JSONObject wxInput = new JSONObject();
            wxInput.put("id",++index);
            wxInput.put("parentId", 1);
            wxInput.put("name", "测试配置");
            wxInput.put("url", "test.html");
            jsonArray.add(wxInput);
        }
        {
            JSONObject wxInput = new JSONObject();
            wxInput.put("id",++index);
            wxInput.put("parentId", 1);
            wxInput.put("name", "上传文件");
            wxInput.put("url", "uploadCdn.html");
            jsonArray.add(wxInput);
        }

        return jsonArray;
    }
}
