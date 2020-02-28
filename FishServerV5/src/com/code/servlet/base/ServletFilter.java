package com.code.servlet.base;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Vector;

@WebFilter(urlPatterns = {"/*"})
public class ServletFilter implements Filter
{
    private Vector<String> extraUri = new Vector<>();

    @Override
    public void init(FilterConfig filterConfig)
    {
        extraUri.add("/operator/ActionType");
        extraUri.add("/operate/refresh");
        extraUri.add("/flushcache");
        extraUri.add("/flushrank");
        extraUri.add("/login/enter");
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException
    {

        // 拦截器调用
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        // websocket长链，直接过滤
        String upgrade = request.getHeader("upgrade");
        if (upgrade != null && upgrade.contains("websocket"))
        {
            arg2.doFilter(arg0, arg1);
            return;
        }
        String contentType = request.getContentType();
        // 上传拦截
        if (null != contentType && contentType.contains("multipart/form-data"))
        {
            arg2.doFilter(arg0, arg1);
            return;
        }
        String servletPath = request.getServletPath();
        // 无效登录用户进行拦截
        if (servletPath.contains("/druid"))
        {
            arg2.doFilter(arg0, arg1);
            return;
        }
        String uri = request.getRequestURI();
        response.setHeader("Access-Control-Allow-Origin", "*");
        /* 星号表示所有的异域请求都可以接受， */
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("access-control-Allow-headers", "content-type,platform,token");
        if (request.getMethod().equals("OPTIONS"))
        {
            response.sendError(HttpServletResponse.SC_OK);
            return;
        }
        String servlet = request.getServletPath();
        if (extraUri.contains(servlet))
        {
            arg2.doFilter(arg0, arg1);
            return;
        }
        if (!request.getMethod().equals("POST"))
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "非法“" + request.getMethod() + "”请求！谢谢！");
            return;
        }
        String token = request.getHeader("token");
        if (token != null && uri.endsWith(token))
        {
            arg2.doFilter(arg0, arg1);
            return;
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "请求响应错误，请验证后 再进行提交！谢谢！");
    }

    @Override
    public void destroy()
    {
        System.out.println(this.getClass().getName() + "::destroy!");
    }

}
