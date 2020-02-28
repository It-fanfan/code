package com.fish.controller;

import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxAddDataService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @pragram: FcWxAddDataController
 * @description: 微信广告数据明细
 * @create: 2020-02-25-15:38
 */
@Controller
@RequestMapping(value = "/manage")
public class FcWxAddDataController {
    @Autowired
    WxAddDataService wxAddDataService;
    @Autowired
    BaseConfig baseConfig;

    @ResponseBody
    @GetMapping(value = "/wxAddData")
    public GetResult getProductData(GetParameter parameter) {
        return wxAddDataService.findAll(parameter);
    }

    @ResponseBody
    @PostMapping(value = "/wxAddData/new")
    public PostResult insertProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = wxAddDataService.insert(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/wxAddData/edit")
    public PostResult modifyProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = wxAddDataService.updateByPrimaryKeySelective(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/wxAddData/delete")
    public PostResult deleteBuyPay(@RequestBody ProductData productInfo) {
        PostResult result = new PostResult();
        int count = wxAddDataService.deleteSelective(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
