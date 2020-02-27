package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadExcel;
import com.fish.utils.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class UploadController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    BaseConfig baseConfig;

    @GetMapping("/upload")
    public String upload()
    {
        return "upload";
    }

    @ResponseBody
    @PostMapping("/upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file)
    {
        JSONObject jsonObject = new JSONObject();
        if (file.isEmpty())
        {
            jsonObject.put("code", 404);
            jsonObject.put("msg", "未上传文件!");
            return jsonObject;
        }
        try
        {
            String readPath = baseConfig.getUpload();
            String originalFilename = file.getOriginalFilename();
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(readPath, originalFilename));
            jsonObject.put("code", 200);
            jsonObject.put("url", baseConfig.getDomain() + originalFilename);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return jsonObject;
    }

    @ResponseBody
    @PostMapping("/uploadExcel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            String readPath = baseConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, originalFilename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }

        jsonObject.put("code", 200);
        return jsonObject;
    }
}
