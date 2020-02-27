package com.code.servlet.pay.huawei;

import com.alibaba.fastjson.JSONObject;
import com.annotation.AvoidRepeatableCommit;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/pay/huawei/notify", name = "huawei pay notify")
public class HuaweiNotifyServlet extends ServletMain
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        byte[] buffer = parseServletInputStream(req);
        String data = new String(buffer, StandardCharsets.UTF_8);
        STAR.info("HuaweiNotifyServlet:" + data);
        JSONObject json = JSONObject.parseObject(data);
        JSONObject statusUpdateNotification = json.getJSONObject("statusUpdateNotification");
        JSONObject notifycationSignature = json.getJSONObject("notifycationSignature");
        super.doPost(req, resp);
    }
}
