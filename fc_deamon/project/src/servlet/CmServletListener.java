package servlet;

import config.CmProjectConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pipe.CmPipeDeamon;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author feng
 */
@WebListener
public class CmServletListener implements ServletContextListener
{
    /**
     * 线程池
     */
    public static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(20, new BasicThreadFactory.Builder().namingPattern("worker-thread-%d").daemon(true).priority(Thread.MAX_PRIORITY).build());

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg)
    {
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg)
    {
        // 初始化服务器环境配置信息
        //
        CmProjectConfig.initConfig(arg);

        // 管道初始化
        //
        CmPipeDeamon.init(scheduler);
    }
}
