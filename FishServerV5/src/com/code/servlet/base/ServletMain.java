package com.code.servlet.base;

import com.alibaba.fastjson.JSONObject;
import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.LoginCode;
import com.code.protocols.login.ERROR;
import com.code.service.ui.FlushService;
import com.utils.XWHMathTool;
import com.utils.XwhTool;
import com.utils.db.RedisUtils;
import com.utils.font.Localization;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 进行接口处理
 *
 * @author Host-0
 */
public abstract class ServletMain<T, R> extends HttpServlet
{
    protected static final Logger STAR = LoggerFactory.getLogger(ServletMain.class);
    //访问日志
    protected static final Logger LOG = LoggerFactory.getLogger("servletLog");
    protected static final int STATUS_SUCCESS = 200;
    /**
     *
     */
    private static final long serialVersionUID = 8411928682891421074L;

    /**
     * 解析客户端提交过来数据
     *
     * @param request req
     * @return data
     * @throws IOException error
     */
    public static byte[] parseServletInputStream(HttpServletRequest request) throws IOException
    {
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int read;
        while ((read = is.read(buff)) > 0)
        {
            baos.write(buff, 0, read);
        }
        byte[] buffer = baos.toByteArray();
        baos.close();
        return buffer;
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request 请求数据
     * @return ip
     */
    private static String getIpAddress(final HttpServletRequest request)
    {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15)
        {
            String[] ips = ip.split(",");
            for (String strIp : ips)
            {
                if (!("unknown".equalsIgnoreCase(strIp)))
                {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // GET请求进行拦截跳转
        request.getRequestDispatcher("/error.html").forward(request, response);
    }

    /**
     * 拦截点数据
     *
     * @return allow req
     */
    private ERROR point(RequestParameter parameter, HttpServletRequest request)
    {
        // 获取注解
        AvoidRepeatableCommit avoidRepeatableCommit = this.getClass().getAnnotation(AvoidRepeatableCommit.class);
        if (avoidRepeatableCommit == null)
        {
            return ERROR.success;
        }
        String randomKey = XwhTool.getMD5Encode(parameter.getRequestJson());
        String codeKey = null;
        //指定攔截是否開啓
        if (avoidRepeatableCommit.filter())
        {
            //拦截开启，进行检测token
            if (!(parameter.getRequestObject() instanceof AbstractRequest) || ((codeKey = ((AbstractRequest) parameter.getRequestObject()).logincode) == null))
            {
                return ERROR.un_token;
            }
            String userId = ((AbstractRequest) parameter.getRequestObject()).userid;
            setUserCache(request, userId);
        }
        long timeout = avoidRepeatableCommit.timeout();
        String ipKey = String.format("%s-%s", this.getClass().getName(), randomKey);
        int hashCode = Math.abs(ipKey.hashCode());
        String key = String.format("%s_%d", parameter.getIp(), hashCode);
        // 用多参数set方法保证对redis操作原子性
        long out = RedisUtils.pttl(key);
        if (out > 0)
        {
            return ERROR.frequently;
        }
        Long isSuccess = RedisUtils.setnxAndExpire(key, UUID.randomUUID().toString(), timeout);
        if (isSuccess < 0)
        {
            return ERROR.system_error;
        }
        //进行校验logincode
        if (codeKey != null)
        {
            UserCache userCache = getUserCache(request);
            long ttl = RedisUtils.pttl(codeKey);
            if (ttl <= 0)
            {
                return ERROR.timeout;
            } else
            {
                RedisUtils.pexpire(codeKey, 60 * 1000 * 20);
            }
            if (!codeKey.equals(userCache.getLoginCode()))
            {
                RedisUtils.del(codeKey);
                return ERROR.conflict;
            }
        }
        return ERROR.success;
    }

    /**
     * 获取登陆状态数据
     *
     * @return data
     */
    protected LoginCode getLoginCode(HttpServletRequest request)
    {
        UserCache userCache = getUserCache(request);
        if (userCache == null)
        {
            return null;
        }
        return LoginCode.getLoginCode(userCache);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        System.out.println(request.getRequestURI());
        long current = System.currentTimeMillis();
        byte[] result = null;
        StringBuilder putLog = new StringBuilder();
        RequestParameter parameter = parseUploadData(request);
        if (parameter == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
            return;
        }
        try
        {

            parameter.setSessionId(getSessionId(request));
            parameter.setPlatform("ios");
            // 访问user-agent
            String user_agent = request.getHeader("user-agent");
            if (user_agent != null)
            {
                if (user_agent.toLowerCase().contains("android"))
                {
                    parameter.setPlatform("android");
                }
            }
            parameter.setIp(getIpAddress(request));
            //数据解析
            // 进行拦截检测
            ERROR point = point(parameter, request);
            if (point != ERROR.success)
            {

                putLog.append(putExceptionData(point));
                result = encryptionBase64(putLog.toString()).getBytes(StandardCharsets.UTF_8);
                return;
            }
            R r = doLogic(parameter, request, response);
            //进行检测附加信息
            setAdditionData((AbstractResponse) r, request);
            String resultJson = setFontChange(parameter.localization, r);
            String flushInfo = encryptionBase64(resultJson);
            putLog.append(resultJson);
            result = flushInfo.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e));
            putLog.append(putExceptionData(ERROR.server_error));
            result = encryptionBase64(putLog.toString()).getBytes(StandardCharsets.UTF_8);
        } finally
        {
            StringBuffer log = new StringBuffer();
            log.append(this.getClass().getName()).append(":");
            log.append("get json=");
            log.append(parameter.getRequestJson());
            log.append(";put json=").append(putLog);
            printLog(log);
            ServletOutputStream out = response.getOutputStream();
            out.write(result != null ? result : "402".getBytes());
            out.close();
            STAR.warn(this.getClass().getName() + ":耗时:" + (System.currentTimeMillis() - current) + "ms!");
        }
    }

    private String putExceptionData(ERROR error)
    {
        JSONObject json = new JSONObject();
        json.put("status", true);
        json.put("code", error.getCode());
        json.put("msg", error.getMsg());
        return json.toJSONString();
    }

    /**
     * 设置字体变化（进行文字转义）
     *
     * @return 转义文字
     */
    private String setFontChange(String localization, R r)
    {
        if (localization == null)
            return XwhTool.getJSONByFastJSON(r);
        return XwhTool.getJSONByFastJSON(Localization.getInstance(localization).convert(r));
    }

    /**
     * 设置附加数据
     *
     * @param res 下发数据
     */
    private void setAdditionData(AbstractResponse res, HttpServletRequest request)
    {
        if (!existFlush())
            return;
        UserCache userCache = getUserCache(request);
        if (userCache != null)
        {
            res.flush = new FlushService(userCache).getNode();
        }
    }

    /**
     * 獲取當前玩家信息
     *
     * @return 數據參數
     */
    protected UserCache getUserCache(HttpServletRequest request)
    {
        String key = "USER-ID";
        HttpSession session = request.getSession();
        return (UserCache) session.getAttribute(key);
    }

    private String getSessionId(HttpServletRequest request)
    {
        return request.getSession().getId();
    }

    /**
     * 設置玩家緩存數據
     *
     * @param request 下发请求信息
     * @param userId  玩家信息
     */
    protected void setUserCache(HttpServletRequest request, String userId)
    {
        if (userId == null || !XWHMathTool.isNumeric(userId))
            return;
        String key = "USER-ID";
        HttpSession session = request.getSession();
        UserCache userCache = UserCache.getUserCache(userId);
        if (userCache != null)
        {
            session.setAttribute(key, userCache);
            ServletContext application = session.getServletContext();
            List onlineUserList = (List) application.getAttribute("onlineUserList");
            if (onlineUserList == null)
            {
                onlineUserList = new ArrayList<>();
            }
            if (!onlineUserList.contains(userId))
            {
                onlineUserList.add(userCache.getUserId());
                application.setAttribute("onlineUserList", onlineUserList);
            }
        }
    }

    /**
     * 解析数据信息
     */
    private RequestParameter parseUploadData(HttpServletRequest request) throws IOException
    {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        // 进行解析数据
        byte[] buffer = parseServletInputStream(request);
        String base64 = new String(buffer, StandardCharsets.UTF_8);
        String requestJson = null;
        try
        {
            requestJson = decryptionBase64(base64);
            T requestObject = XwhTool.parseJSONString(requestJson, params[0]);
            RequestParameter parameter = new RequestParameter(requestJson, requestObject);
            String localization = null;
            Field field = existField(requestObject, "localization");
            if (field != null)
            {
                localization = (String) field.get(requestObject);
            }
            if (localization == null)
            {
                localization = (String) request.getSession().getAttribute("localization");
            } else
            {
                request.getSession().setAttribute("localization", localization);
            }
            if (localization != null)
            {
                parameter.setLocalization(localization);
            }
            return parameter;
        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e) + ",json=" + requestJson);
        }
        return null;
    }

    private static Field existField(Object object, String name)
    {
        Field[] fields = object.getClass().getFields();
        for (Field field : fields)
        {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }

    /**
     * 打印日志
     *
     * @param log data
     */
    private void printLog(StringBuffer log)
    {
        LOG.info(log.toString());
    }

    /**
     * 进行逻辑解析
     *
     * @param req      {json}
     * @param request  http req
     * @param response http res
     * @return {json}
     * @throws Exception error
     */
    protected R doLogic(RequestParameter req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return doLogic(req.getRequestObject(), request, response);
    }

    /**
     * 进行逻辑解析
     *
     * @param req      {json}
     * @param request  http req
     * @param response http res
     * @return {json}
     * @throws Exception error
     */
    protected R doLogic(T req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    /**
     * 检测是否刷新
     *
     * @return 刷新节点
     */
    protected boolean existFlush()
    {
        return true;
    }

    /**
     * base64加密
     *
     * @param json data
     * @return encode
     */
    private String encryptionBase64(String json)
    {
        // return json;
        return new BASE64Encoder().encode(json.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解密
     *
     * @param base64 decode
     * @return data
     * @throws IOException error
     */
    private String decryptionBase64(String base64) throws IOException
    {
        // return base64;
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64);
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public class RequestParameter
    {
        T requestObject;
        String requestJson;
        String ip;
        String platform;
        String sessionId;
        String localization;

        RequestParameter(String requestJson, T requestObject)
        {
            setRequestJson(requestJson);
            setRequestObject(requestObject);
        }

        public T getRequestObject()
        {
            return requestObject;
        }

        void setRequestObject(T requestObject)
        {
            this.requestObject = requestObject;
        }

        String getRequestJson()
        {
            return requestJson;
        }

        void setRequestJson(String requestJson)
        {
            this.requestJson = requestJson;
        }

        public String getIp()
        {
            return ip;
        }

        void setIp(String ip)
        {
            this.ip = ip;
        }

        public String getPlatform()
        {
            return platform;
        }

        public void setPlatform(String platform)
        {
            this.platform = platform;
        }

        public String getSessionId()
        {
            return sessionId;
        }

        void setSessionId(String sessionId)
        {
            this.sessionId = sessionId;
        }

        public String getLocalization()
        {
            return localization;
        }

        void setLocalization(String localization)
        {
            this.localization = localization;
        }
    }

}
