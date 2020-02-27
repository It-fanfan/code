package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.service.UploadExcelService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadExcel;
import com.fish.utils.ReadJsonUtil;
import com.fish.utils.log4j.Log4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipException;

@Controller
@RequestMapping("/upload")
public class UploadController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    UploadExcelService uploadExcelService;

    @Autowired
    ArcadeGamesMapper gamesMapper;

    @GetMapping
    public String upload()
    {
        return "upload";
    }

    @ResponseBody
    @PostMapping
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
    @PostMapping("/zip")
    public JSONObject uploadZip(@RequestParam("file") MultipartFile file, HttpServletRequest request)
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
            String type = request.getParameter("type");
            boolean isAuto = "on".equals(request.getParameter("isAuto"));
            boolean isDelete = "on".equals(request.getParameter("isDelete"));
            JSONObject uploadJSON = baseConfig.getUploadJson();
            String readPath = uploadJSON.getString(type);
            System.out.println(readPath + ",type=" + type + ",json=" + uploadJSON);
            String originalFilename = file.getOriginalFilename();
            File readFile = new File(readPath, Objects.requireNonNull(originalFilename));
            FileUtils.copyInputStreamToFile(file.getInputStream(), readFile);
            //进行解压
            if (isAuto)
            {
                ZipFile zipFile = new ZipFile(readFile);
                zipFile.setCharset(Charset.defaultCharset());
                if (!zipFile.isValidZipFile())
                {
                    throw new ZipException("压缩文件不合法，可能已经损坏！");
                }
                File zip = new File(readPath);
                if (zip.isDirectory() && !zip.exists())
                {
                    zip.mkdirs();
                }
                zipFile.extractAll(readPath);
                if (isDelete)
                {
                    readFile.delete();
                }
            }
            jsonObject.put("code", 200);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return jsonObject;
    }

    @ResponseBody
    @PostMapping("/image")
    public JSONObject uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request)
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
            String flushType = request.getParameter("flushType");
            String readPath = baseConfig.getUploadJson().getString("game");
            String hostUrl = null;
            if (flushType != null && !flushType.trim().isEmpty())
            {
                readPath = baseConfig.getUploadJson().getString(flushType);
                hostUrl = baseConfig.getUploadJson().getString("host-" + flushType);
            }
            Enumeration<String> paras = request.getParameterNames();
            while (paras.hasMoreElements())
            {
                String key = paras.nextElement();
                System.out.println(key + "=" + request.getParameter(key));
            }
            String originalFilename = file.getOriginalFilename();
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(readPath, originalFilename));
            jsonObject.put("code", 200);
            if (hostUrl != null)
            {
                hostUrl = hostUrl.concat(originalFilename);
                if ("qr".equals(flushType))
                {
                    String gameCode = request.getParameter("gameCode");
                    String SQL = "update games set ddFriendUrl='" + hostUrl + "'";
                    if (gameCode != null && !"all".equals(gameCode))
                    {
                        SQL = SQL.concat(" where ddCode in(").concat(gameCode).concat(")");
                    }
                    System.out.println(SQL);
                    gamesMapper.updateSQL(SQL);
                    jsonObject.put("msg", ReadJsonUtil.flushTable("games", baseConfig.getFlushCache()));
                }
            }
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return jsonObject;
    }

    @ResponseBody
    @PostMapping("/excel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file)
    {
        System.out.println("进来了吗");
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
            int insert = uploadExcelService.insert(jsonObject);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }

        jsonObject.put("code", 200);
        return jsonObject;
    }
}
