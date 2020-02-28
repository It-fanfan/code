package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.utils.ReadExcel;
import com.fish.utils.http.XwhHttp;
import com.fish.utils.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/client")
public class ServiceClientController {
     String appId = "wxa3767fede98509c6";
     String secret = "8919594e4054a7a195643755daaafb41";
    /**
     * 客户端上传二维码至服务器
     */
    @ResponseBody
    @PostMapping("/uploadQRCode")
    public JSONObject sendMsToAccount(JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        JSONObject formData = new JSONObject();
        formData.put("filename","");
        formData.put("filelength","");
        formData.put("content-type","image/png");
        jsonObject.put(" access_token",getAccessToken(appId, secret));
        jsonObject.put("type","image");
        jsonObject.put("media",formData);

        String response ="";
        try {
            response = XwhHttp.doPostForm("https://api.weixin.qq.com/cgi-bin/media/upload", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject resDataObj = JSONObject.parseObject(response);
        String mediaId = (String) resDataObj.get("media_id");

        JSONObject resJson = new JSONObject();
        resJson.put("media_id", mediaId);
        resJson.put("code", 200);
        return jsonObject;
    }

    @ResponseBody
    @PostMapping("/uploadCode")
    public JSONObject uploadExcel(@RequestParam("QRCode") MultipartFile QRCode ,@RequestParam("openId") String openId)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            ReadExcel readExcel = new ReadExcel();

            jsonObject.put("context", readExcel.read(0));
        } catch (Exception e)
        {
            System.out.println(Log4j.getExceptionInfo(e));
        }
        jsonObject.put("code", 200);
        return jsonObject;
    }

    /**
     * 获取access_token
     * appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
     * */
    public String getAccessToken(String appid, String appsecret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appsecret;
        JSONObject resDataObj = JSONObject.parseObject(url);
        String accessToken = (String) resDataObj.get("access_token");
        return accessToken;
    }
}
