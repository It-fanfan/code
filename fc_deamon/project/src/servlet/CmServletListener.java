/**
 *
 */
package servlet;

import config.CmGlobalConfig;
import config.CmProjectConfig;
import pipe.CmPipeDeamon;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author feng
 *
 */
@WebListener
public class CmServletListener implements ServletContextListener
{
    // 线程池
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(30);
    /*
     * (non-Javadoc)
     *
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg)
    {
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
        //
        // 初始化全局配置信息
        //
        CmGlobalConfig.initConfig(arg);

        // 初始化服务器环境配置信息
        //
        CmProjectConfig.initConfig(arg);

        // 管道初始化
        //
        CmPipeDeamon.init(scheduler);
    }
}
