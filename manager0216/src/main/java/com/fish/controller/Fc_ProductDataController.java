package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ProductDataService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadExcel;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping(value = "/manage")
public class Fc_ProductDataController {
    @Autowired
    ProductDataService fcProductDataService;
    @Autowired
    BaseConfig baseConfig;

    //查询产品信息详情
    @ResponseBody
    @GetMapping(value = "/productdata")
    public GetResult getProductData(GetParameter parameter) {
        return fcProductDataService.findAll(parameter);
    }

    //搜索产品信息详情
    @ResponseBody
    @PostMapping(value = "/productdata/search")
    public GetResult searchData(HttpServletRequest request, GetParameter parameter) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("ddAppId");
        return fcProductDataService.searchProductData(beginDate, endDate, productName, parameter);
    }

    @ResponseBody
    @PostMapping("/productdata/uploadExcel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        try {
            String readPath = baseConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, originalFilename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
            int insert = fcProductDataService.insertExcel(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("code", 200);
        return jsonObject;
    }

    //新增游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/productdata/new")
    public PostResult insertProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = fcProductDataService.insert(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    //修改游戏appid、secret信息
    @ResponseBody
    @PostMapping(value = "/productdata/edit")
    public PostResult modifyProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = fcProductDataService.updateByPrimaryKeySelective(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/productdata/delete")
    public PostResult deleteBuyPay(@RequestBody ProductData productInfo) {
        PostResult result = new PostResult();
        int count = fcProductDataService.deleteSelective(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
