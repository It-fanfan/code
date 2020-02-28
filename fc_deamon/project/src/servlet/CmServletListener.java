<<<<<<< HEAD
package servlet;

import config.CmProjectConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
=======
/**
 *
 */
package servlet;

import config.CmGlobalConfig;
import config.CmProjectConfig;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
import pipe.CmPipeDeamon;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
<<<<<<< HEAD
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author feng
=======
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author feng
 *
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
 */
@WebListener
public class CmServletListener implements ServletContextListener
{
<<<<<<< HEAD
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
=======
    // 线程池
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(30);
    /*
     * (non-Javadoc)
     *
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    public void contextDestroyed(ServletContextEvent arg)
    {
    }

<<<<<<< HEAD
    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg)
    {
=======
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
        //
        // 初始化全局配置信息
        //
        CmGlobalConfig.initConfig(arg);

>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        // 初始化服务器环境配置信息
        //
        CmProjectConfig.initConfig(arg);

        // 管道初始化
        //
        CmPipeDeamon.init(scheduler);
    }
}
