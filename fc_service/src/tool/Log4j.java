package tool;

import java.lang.reflect.InvocationTargetException;

/**
 * LOG配置
 *
 * @author user
 */
public class Log4j
{

    /**
     * 获取异常具体信息
     */
    public static String getExceptionInfo(Exception e)
    {
        StringBuilder exception = new StringBuilder();
        exception.append(e.getMessage()).append("\n");
        StackTraceElement[] messages = e.getStackTrace();
        for (StackTraceElement stackTraceElement : messages)
        {
            exception.append(stackTraceElement).append("\n");
        }
        return exception.toString();
    }

    /**
     * 捕获反射方法向上抛出异常
     */
    public static String handleException(Exception e)
    {
        StringBuilder exception = new StringBuilder();
        if (e instanceof InvocationTargetException)
        {
            Throwable targetEx = ((InvocationTargetException) e).getTargetException();
            if (targetEx != null)
            {
                exception.append(targetEx.getMessage()).append("  ");
                StackTraceElement[] messages = targetEx.getStackTrace();
                for (StackTraceElement stackTraceElement : messages)
                {
                    exception.append(stackTraceElement).append("\n");
                }
            }
        } else
        {
            exception.append(e.getMessage());
        }

        return exception.toString();
    }
}
