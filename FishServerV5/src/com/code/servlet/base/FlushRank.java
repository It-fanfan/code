package com.code.servlet.base;

import com.alibaba.fastjson.JSONObject;
import com.annotation.AvoidRepeatableCommit;
import com.code.service.range.BookRankingService;
import com.utils.log4j.Log4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = {"/flushrank"})
public class FlushRank extends HttpServlet
{
    /**
     *
     */
    private static final long serialVersionUID = -7898090976388338515L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 进行刷新缓存
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        ServletOutputStream out = response.getOutputStream();

        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "刷新成功");
            //进行遍历查找用户
            int size = BookRankingService.resetRanking();
            jsonObject.put("size", size);
            out.write(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e)
        {
            out.write(Log4j.getExceptionInfo(e).getBytes());
        }

    }
}
