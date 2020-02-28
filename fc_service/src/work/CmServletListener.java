package work;

import config.CmProjectConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ClassUtils;
import tool.Log4j;
import tool.ipsearch.IPSeeker;

import javax.persistence.Entity;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author feng
 */
@WebListener
public class CmServletListener implements ServletContextListener
{
    // 线程池
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(30);
    private static Logger LOG = LoggerFactory.getLogger(CmServletListener.class);

    /*
     * (non-Javadoc)
     *
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg)
    {
        scheduler.shutdown();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg)
    {
        // 数据库资源句柄
        // 初始化服务器环境配置信息
        //
        CmProjectConfig.initConfig(arg);
        // 初始化用户信息
        //
        initDatabase();
    }

    /**
     * 进行加载业务逻辑表
     */
    private void initDatabase()
    {
        String packageName = "db";
        System.out.println("进行扫描包" + packageName);
        Set<Class<?>> classes = ClassUtils.getClasses(packageName);
        classes.forEach(action ->
        {
            if (action.isAnnotationPresent(Entity.class))
            {
                try
                {
                    System.out.println(action.getName());
                    Method method = action.getDeclaredMethod("init");
                    if (method != null)
                    {
                        method.invoke(null);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    LOG.error(Log4j.getExceptionInfo(e));
                }
            }
        });
        System.out.println("進行初始实体类~");
    }
}
