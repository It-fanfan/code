package com.code.servlet.base;

import com.code.dao.db.FishInfoDb;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.Vector;

@WebServlet(urlPatterns = {"/flushcache"})
public class FlushCache extends HttpServlet
{
    private static final Logger LOG = LoggerFactory.getLogger(FlushCache.class);
    /**
     *
     */
    private static final long serialVersionUID = -7898090976388338515L;

    // 刷新类信息
    private static final Vector<Class<?>> flushClass = new Vector<>();

    public void init()
    {
        flushClass.add(FishInfoDb.class);
        LOG.debug("刷新注入:" + flushClass.toString());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 进行刷新缓存
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        ServletOutputStream out = response.getOutputStream();
        StringBuilder builder = new StringBuilder();
        String updateType = request.getParameter("update-type");
        for (Class<?> outclass : flushClass)
        {
            if (outclass.getSimpleName().equalsIgnoreCase(updateType.trim()))
            {
                builder.append("刷新接口:").append(outclass.getSimpleName()).append("<br>");
                String method = request.getParameter("update-method");
                String methodname = request.getParameter("update-method-name");
                String parameter = request.getParameter("update-parameter");
                if (!methodname.startsWith("flush"))
                {
                    builder.append("刷新方法未注入:").append(methodname).append("<br>");
                    break;
                }
                String key = outclass.getSimpleName() + method + methodname + parameter;
                long timeout = point(key);
                if (timeout > 0)
                {
                    builder.append("刷新方法:").append(methodname).append(",参数：").append(parameter).append("<br>");
                    builder.insert(0, "刷新失败，刷新过于频繁，距离下次刷新间隔" + (timeout / 1000) + "秒！<br>");
                    break;
                }
                try
                {
                    if (method != null && method.equals("static"))
                    {
                        Method methods = outclass.getMethod(methodname);
                        methods.invoke(null);
                        builder.insert(0, "刷新成功！<br>静态方法:" + methods.getName() + "<br>");
                        break;
                    }

                    Method methods;
                    if (parameter == null)
                        methods = outclass.getMethod(methodname);
                    else
                        methods = outclass.getMethod(methodname, String.class);
                    Object t = outclass.getDeclaredMethod("instance").invoke(null);
                    if (parameter != null)
                        methods.invoke(t, parameter);
                    else
                        methods.invoke(t);
                    builder.append("刷新类型<br>方法:").append(methods.getName()).append("，参数:").append(parameter).append("<br>");
                    builder.insert(0, "刷新结果:成功!<br>");

                } catch (Exception e)
                {
                    builder.append("刷新异常:").append(Log4j.getExceptionInfo(e)).append("<br>");
                }
                break;
            }
        }
        if (builder.length() > 0)
        {
            out.write(builder.toString().getBytes("utf-8"));
            out.close();
        } else
            response.sendRedirect("https://www.baidu.com");
    }

    /**
     * 拦截点数据
     *
     * @return allow req
     */
    protected long point(String key)
    {
        // 用多参数set方法保证对redis操作原子性
        long out = RedisUtils.pttl(key);
        if (out > 0)
        {
            return out;
        }
        long timeout = 1000 * 60;
        Long isSuccess = RedisUtils.setnxAndExpire(key, UUID.randomUUID().toString(), timeout);
        return isSuccess >= 0 ? 0 : timeout;
    }

}
