package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.BuyPay;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.BuyPayService;
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
public class Fc_BuyPayController {

    @Autowired
    BaseConfig baseConfig;
    @Autowired
    BuyPayService buyPayService;

    //查询买量信息
    @ResponseBody
    @GetMapping(value = "/buypay")
    public GetResult getBuyPay(GetParameter parameter) {
        return buyPayService.findAll(parameter);
    }

    @ResponseBody
    @PostMapping("/buypay/uploadExcel")
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
            int insert = buyPayService.insertExcel(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.put("code", 200);
        return jsonObject;
    }

    //买量搜索
    @ResponseBody
    @PostMapping(value = "/buypay/search")
    public GetResult searchBuyPay(HttpServletRequest request, GetParameter parameter) {

        GetResult result = new GetResult();
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("productName");
        // productDataService.searchData(beginDate,endDate,productName,parameter);
        return buyPayService.searchData(beginDate, endDate, productName, parameter);
    }

    //新增订单信息
    @ResponseBody
    @PostMapping(value = "/buypay/new")
    public PostResult insertBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.insert(productInfo);
        // count =1;
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    //修改游戏信息
    @ResponseBody
    @PostMapping(value = "/buypay/edit")
    public PostResult modifyBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.updateByPrimaryKeySelective(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/buypay/delete")
    public PostResult deleteBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.deleteSelective(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }
}
