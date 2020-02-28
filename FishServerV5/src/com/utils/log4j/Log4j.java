package com.utils.log4j;

import com.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LOG配置
 *
 * @author user
 */
public class Log4j
{
    /**
     * 分析日志入录
     */
    public static void analysisLog(String platform, String type, String userId, String ip, String sessionId)
    {
        Map<String, Object> hash = new LinkedHashMap<>();
        hash.put("platform", platform);
        hash.put("type", type);
        hash.put("userId", userId);
        hash.put("ip", ip);
        hash.put("sessionId", sessionId);
        NAME.ANALYSIS_LOG.info(XwhTool.getJSONByFastJSON(hash));
    }

    /**
     * 获取异常具体信息
     *
     * @param e
     * @return
     */
    public static String getExceptionInfo(Exception e)
    {
        StringBuffer exception = new StringBuffer();
        exception.append(e.getMessage() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        for (StackTraceElement stackTraceElement : messages)
        {
            exception.append(stackTraceElement + "\n");
        }
        return exception.toString();
    }

    /**
     * 捕获反射方法向上抛出异常
     *
     * @param e
     * @return
     */
    public static String handleException(Exception e)
    {
        StringBuffer exception = new StringBuffer();
        if (e instanceof InvocationTargetException)
        {
            Throwable targetEx = ((InvocationTargetException) e).getTargetException();
            if (targetEx != null)
            {
                exception.append(targetEx.getMessage() + "  ");
                StackTraceElement[] messages = targetEx.getStackTrace();
                for (StackTraceElement stackTraceElement : messages)
                {
                    exception.append(stackTraceElement + "\n");
                }

            }
        } else
        {
            exception.append(e.getMessage());
        }

        return exception.toString();
    }

    public interface NAME
    {
        Logger ACCESS_LOG = LoggerFactory.getLogger("accessLog");
        Logger ANALYSIS_LOG = LoggerFactory.getLogger("analysisLog");
    }
}
