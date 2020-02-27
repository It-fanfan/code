package com.test;

import com.code.servlet.base.ServletMain;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = "/test/threadSafe", name = "test thread safe")
public class TestThreadSafeServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("doGet");
        // GET请求进行拦截跳转
        request.getRequestDispatcher("/error.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        byte[] data = ServletMain.parseServletInputStream(req);
        String userId = new String(data, "utf-8");
        setSessionCache(userId, req);
        flushData(userId, req);
    }

    private void setSessionCache(String userId, HttpServletRequest req)
    {
        HttpSession session = req.getSession();
        System.out.println("get session UserId :" + session.getAttribute("userId"));
        session.setAttribute("userId", userId);
        System.out.println("set session UserId :" + session.getAttribute("userId"));
    }

    private String getUserId(HttpServletRequest req)
    {
        HttpSession session = req.getSession();
        return (String) session.getAttribute("userId");
    }

    private static long id = 0;

    public void flushData(String userId, HttpServletRequest req)
    {
        int len = 0;
        for (int i = 0; i < 100000; i++)
        {
            len++;
        }
        String temp = getUserId(req);
        if (!temp.equals(userId))
        {
            System.out.println("加法运算" + (++id));
        }
    }

    @Override
    protected long getLastModified(HttpServletRequest req)
    {
        System.out.println("getLastModified");
        return super.getLastModified(req);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("doDelete");
        super.doDelete(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("doHead");
        super.doHead(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("doOptions");
        super.doOptions(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("doPut");
        super.doPut(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("doTrace");
        super.doTrace(req, resp);
    }


    @Override
    public int hashCode()
    {
        System.out.println("hashCode");
        return super.hashCode();
    }


    @Override
    public void destroy()
    {
        System.out.println("destroy");
        super.destroy();
    }

    @Override
    public void log(String message, Throwable t)
    {
        System.out.println("log message t");
        super.log(message, t);
    }

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        System.out.println("init config");
        super.init(config);
    }

    @Override
    public String getServletName()
    {
        System.out.println("getServletName");
        return super.getServletName();
    }

    @Override
    public String getServletInfo()
    {
        System.out.println("getServletInfo");
        return super.getServletInfo();
    }

    @Override
    public void log(String msg)
    {
        System.out.println("log public");
        super.log(msg);
    }

    @Override
    public void init() throws ServletException
    {
        System.out.println("init public");
        super.init();
    }

    @Override
    public String getInitParameter(String name)
    {
        System.out.println("getInitParameter");
        return super.getInitParameter(name);
    }

    @Override
    public ServletContext getServletContext()
    {
        System.out.println("getServletContext");
        return super.getServletContext();
    }

    @Override
    public ServletConfig getServletConfig()
    {
        System.out.println("getServletConfig");
        return super.getServletConfig();
    }

    @Override
    public Enumeration<String> getInitParameterNames()
    {
        System.out.println("getInitParameterNames");
        return super.getInitParameterNames();
    }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("finalize");
        super.finalize();
    }
}
