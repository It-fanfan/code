package com.fish.controller;

import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundGame;
import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RoundGameService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/manage")
public class RoundGameController
{

    @Autowired
    RoundGameService roundGameService;
    @Autowired
    BaseConfig baseConfig;

    //查询小游戏赛制情况
    @GetMapping(value = "/roundgame")
    public GetResult getRecharge(GetParameter parameter)
    {
        return roundGameService.findAll(parameter);
    }

    @ResponseBody
    @GetMapping(value = "/roundgame/rounds")
    public List<RoundExt> getRounds(GetParameter parameter)
    {
        return roundGameService.selectAllS();
    }

    @PostMapping(value = "/roundgame/delete")
    public PostResult deleteRecharge(@RequestBody Recharge productInfo)
    {
        PostResult result = new PostResult();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    //新增赛制配置信息
    @ResponseBody
    @PostMapping(value = "/roundgame/new")
    public PostResult insertRoundExt(@RequestBody RoundGame productInfo)
    {
        PostResult result = new PostResult();
        int count = roundGameService.insert(productInfo);
        if (count == 1)
        {
            String res = ReadJsonUtil.flushTable("round_game", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏信息
    @PostMapping(value = "/roundgame/edit")
    public PostResult modifyRoundExt(@RequestBody RoundGame productInfo)
    {
        PostResult result = new PostResult();
        int count = roundGameService.updateByPrimaryKeySelective(productInfo);
        if (count != 0)
        {
            String res = ReadJsonUtil.flushTable("round_game", baseConfig.getFlushCache());
            result.setCode(200);
            result.setMsg("操作成功" + res);
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
