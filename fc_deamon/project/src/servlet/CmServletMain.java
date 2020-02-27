/**
 *
 */
package servlet;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author feng
 */
public class CmServletMain extends HttpServlet
{

    private static final long serialVersionUID = -712623596818241269L;

    protected static Logger LOG = LoggerFactory.getLogger(CmServletMain.class);

    /**
     * The doGet method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("finish!");
        out.flush();
        out.close();
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource   数据库资源句柄
     * @param requestObject 请求的对象
     * @return 响应的包体
     */
    protected boolean handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, HttpServletResponse response) throws Exception
    {
        return false;
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) throws Exception
    {
        //转发协议参数
        String url = ReadConfig.get("RequestURL") + requestObject.getServletPath();
        try
        {
            url = url.replace("persieDeamon", "");
            if (requestPackage.containsKey("ddUid"))
            {
                requestPackage.put("uid", requestPackage.getString("ddUid"));
            }
            for (Map.Entry<String, Object> entry : requestPackage.entrySet())
            {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.startsWith("dd"))
                {
                    String _key = key.substring(2, 3).toLowerCase() + key.substring(3, key.length());
                    requestPackage.put(_key, value);
                }
            }
            String send = CmTool.sendPostByGson(url, requestPackage, "utf-8");
            if (send != null)
                return JSONObject.parseObject(send);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return new JSONObject();
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        //
        // 数据库资源句柄
        //
        JSONObject responsePackage = null;
        JSONObject requestObject = null;
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            do
            {
                //
                // 请求类型过滤
                //
                if (handle(sqlResource, request, response))
                {
                    break;
                }

                //
                // 获取并解密相关数据
                //
                byte[] requestBytes = CmTool.parseServletInputStream(request);
                if (null == requestBytes)
                {
                    break;
                }

                //
                // 根据字节流内容得到解析包
                //
                String requestContent = new String(requestBytes, StandardCharsets.UTF_8);

                requestObject = JSONObject.parseObject(requestContent);
                if (null == requestObject)
                {
                    break;
                }

                // 进行具体的响应操作
                //
                responsePackage = handle(sqlResource, request, requestObject);
            } while (false);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        try
        {
            if (null == responsePackage)
            {
                responsePackage = JSONObject.parseObject("{\"result\":\"failed\"}");
            }
            StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
            builder.append(",get:");
            if (requestObject != null)
                builder.append(requestObject.toJSONString());
            builder.append(",post:").append(responsePackage.toJSONString());
            LOG.info(builder.toString());
            CmTool.encodeServletOutputStream(response, responsePackage);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

}
