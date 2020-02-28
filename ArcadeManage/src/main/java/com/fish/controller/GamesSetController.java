package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesSetService;
import com.fish.service.WxConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class GamesSetController
{
    @Autowired
    GamesSetService gamesSetService;

    @Autowired
    WxConfigService wxConfigService;

    @Autowired
    BaseConfig baseConfig;

    //查询展示所有合集配置信息
    @ResponseBody
    @GetMapping(value = "/gameset")
    public GetResult getGameSet(GetParameter parameter)
    {
        return gamesSetService.findAll(parameter);
    }

    //查询展示所有合集信息
    @ResponseBody
    @GetMapping(value = "/gamesetInfo")
    public List<ArcadeGameSet> getGameInfo(GetParameter parameter)
    {
        return gamesSetService.selectAll(parameter);
    }

    //查询展示所有appId和游戏名称信息
    @ResponseBody
    @GetMapping(value = "/gameset/jumpdirect")
    public List<WxConfig> getJumpDirect(GetParameter parameter)
    {
        List<WxConfig> wxConfigs = wxConfigService.selectAll(parameter);
        for (WxConfig wxConfig : wxConfigs)
        {
            String ddappid = wxConfig.getDdappid();
            if (StringUtils.isNotBlank((wxConfig.getProductName())))
            {
                String productName = wxConfig.getProductName();
                wxConfig.setJumpDirect(ddappid + "-" + productName);
            } else
            {
                wxConfig.setJumpDirect(ddappid);
            }
        }
        return wxConfigs;
    }

    //新增合集配置
    @ResponseBody
    @PostMapping(value = "/gameset/new")
    public PostResult insertGameSet(@RequestBody ArcadeGameSet productInfo)
    {
        PostResult result = new PostResult();

        int count = gamesSetService.insert(productInfo);
        if (count == 1)
        {
            String res = ReadJsonUtil.flushTable("gameset", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            return result;
        } else if (count == 5)
        {
            result.setCode(410);
            result.setMsg("游戏代号重复，操作失败");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改合集信息
    @ResponseBody
    @PostMapping(value = "/gameset/edit")
    public PostResult modifyGameSet(@RequestBody ArcadeGameSet productInfo)
    {
        PostResult result = new PostResult();
        int count = gamesSetService.updateByPrimaryKeySelective(productInfo);
        if (count == 1)
        {
            String res = ReadJsonUtil.flushTable("gameset", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            return result;
        } else if (count == 5)
        {
            result.setCode(410);
            result.setMsg("游戏代号重复，操作失败");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
