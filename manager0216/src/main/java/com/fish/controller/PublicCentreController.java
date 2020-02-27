package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.PublicCentre;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesService;
import com.fish.service.PublicCentreService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

@Controller
@RequestMapping(value = "/manage")
public class PublicCentreController
{

    @Autowired
    PublicCentreService publicCentreService;
    @Autowired
    BaseConfig baseConfig;

    //查询公众号信息
    @ResponseBody
    @GetMapping(value = "/public")
    public GetResult getPublicCentre(GetParameter parameter)
    {
        return publicCentreService.findAll(parameter);
    }

    //新增游戏信息
    @ResponseBody
    @PostMapping(value = "/public/new")
    public PostResult insertPublicCentre(@RequestBody PublicCentre productInfo)
    {
        PostResult result = new PostResult();
        int count = publicCentreService.insert(productInfo);
        if (count == 1)
        {


            result.setMsg("操作成功" );
            return result;
        } else
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改游戏表信息
    @ResponseBody
    @PostMapping(value = "/public/edit")
    public PostResult modifyPublicCentre(@RequestBody PublicCentre productInfo)
    {
        PostResult result = new PostResult();

        int count = publicCentreService.updateByPrimaryKeySelective(productInfo);
        if (count != 0)
        {


            result.setMsg("操作成功" );
            return result;
        } else
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }
    @ResponseBody
    @GetMapping(value = "/public/submitJson")
    public PostResult submitPublicCentre(GetParameter parameter)
    {
        PostResult result = new PostResult();
        publicCentreService.selectAllForJson(parameter);
        int count = 1;
        if (count != 0)
        {

            result.setMsg("操作成功" );
            return result;
        } else
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }
}
