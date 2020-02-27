package com.code.servlet.login;

import com.alibaba.fastjson.JSONObject;
import com.annotation.AvoidRepeatableCommit;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/login/enter", name = "com.code.protocols.login.Enter")
public class UserEnterServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // POST请求进行拦截跳转
        ServletOutputStream out = response.getOutputStream();
        JSONObject result = new JSONObject();
        result.put("code", HttpServletResponse.SC_OK);
        result.put("status", true);
        result.put("msg", "success");
        String json = result.toJSONString();
        String sendData = new BASE64Encoder().encode(json.getBytes(StandardCharsets.UTF_8));
        out.write(sendData.getBytes());
        out.close();
    }
}
