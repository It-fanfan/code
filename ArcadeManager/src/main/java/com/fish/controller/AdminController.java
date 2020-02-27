package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @ResponseBody
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/select")
    public String select() {
        return "select";
    }

    @ResponseBody
    @GetMapping("/menu")
    public JSONArray menu() {
        int index = 0;
        JSONArray jsonArray = new JSONArray();
        {
            JSONObject wxInput = new JSONObject();
            wxInput.put("id", ++index);
            wxInput.put("parentId", 0);
            wxInput.put("name", "产品信息");
            jsonArray.add(wxInput);
        }
        final int baseIndex = index;
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", baseIndex);
            parent.put("name", "配置");
            jsonArray.add(parent);
            final int parentId = index;
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "产品信息");
                child.put("url", "ArcadeProductinfo.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "审核配置");
                child.put("url", "ArcadeProductconfig.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "产品赛制");
                child.put("url", "ArcadeGameconfig.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "赛制配置");
                child.put("url", "gameDay.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "全局配置");
                child.put("url", "ArcadeGlobalconfig.html");
                jsonArray.add(child);
            }

        }
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", baseIndex);
            parent.put("name", "合集配置");
            jsonArray.add(parent);
            final int grandId = index;
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandId);
                child.put("name", "游戏列表");
                child.put("url", "Testinfo.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandId);
                child.put("name", "合集配置");
                child.put("url", "gameset.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandId);
                child.put("name", "游戏表");
                child.put("url", "arcadePlay.html");
                jsonArray.add(child);
            }
        }
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", baseIndex);
            parent.put("name", "数据统计");
            jsonArray.add(parent);
            final int grandparentId = index;
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "在线情况统计");
                child.put("url", "onlineStatistics.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "用户数据统计");
                child.put("url", "userData.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "更新游戏素材");
                child.put("url", " wxConfig.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "上传素材");
                child.put("url", "uploadCdn.html");
                jsonArray.add(child);
            }
        }
        return jsonArray;
    }
}
