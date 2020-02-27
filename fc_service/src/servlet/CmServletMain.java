package servlet;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.Log4j;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
    public static final Logger SERVLET_LOG = LoggerFactory.getLogger(CmServletMain.class);
    private static final long serialVersionUID = -712623596818241269L;

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
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     * @throws Exception
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) throws Exception
    {
        return null;
    }

    /**
     * 字符串解析
     *
     * @param sqlResource   数据库源
     * @param requestObject 上报节点
     * @param context       解析内容
     * @return 下发内容
     * @throws Exception 异常
     */
    protected String handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, String context) throws Exception
    {
        return null;
    }

    /**
     * 获取格式内容
     */
    protected String getContextType()
    {
        return "json";
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //
        // 数据库资源句柄
        //
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            //
            // 获取并解密相关数据
            //
            byte[] requestBytes = CmTool.parseServletInputStream(request);
            if (null == requestBytes)
            {
                return;
            }
            //
            // 根据字节流内容得到解析包
            //
            String requestContent = new String(requestBytes);
            String result = null;
            switch (getContextType())
            {
                case "json":
                {
                    JSONObject requestObject = JSONObject.parseObject(requestContent);
                    if (null == requestObject)
                    {
                        break;
                    }
                    if (requestObject.containsKey("ddUid"))
                    {
                        requestObject.put("uid", requestObject.getString("ddUid"));
                        requestObject.remove("ddUid");
                    }
                    for (Map.Entry<String, Object> entry : requestObject.entrySet())
                    {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.startsWith("dd"))
                        {
                            String _key = key.substring(2, 3).toLowerCase() + key.substring(3, key.length());
                            requestObject.put(_key, value);
                            requestObject.remove(key);
                        }
                    }

                    // 进行具体的响应操作
                    //
                    JSONObject responsePackage = handle(sqlResource, request, requestObject);
                    if (null != responsePackage)
                    {
                        result = responsePackage.toJSONString();
                        CmTool.encodeServletOutputStream(response, responsePackage);
                    }
                }
                break;
                case "xml":
                case "text":
                {
                    result = handle(sqlResource, request, requestContent);
                    ServletOutputStream out = response.getOutputStream();
                    out.write(result.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                }
                break;
            }
            SERVLET_LOG.info(this.getClass().getName() + ",get:" + requestContent + ",post=" + result);
        } catch (Exception e)
        {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
        }
    }

}
