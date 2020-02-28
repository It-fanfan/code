package com.code.servlet.base;

import com.annotation.Timing;
import com.utils.ClassUtils;
import com.utils.db.XWHResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@WebListener
public class Listener implements ServletContextListener
{
    private static final String servicePackage = "com.code.service";
    private static Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    // 线程池
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(15);

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        XWHResultSetMapper.init(scheduler);
        System.out.println("工程启动中!!!!");
        LOGGER.debug("工程正在启动...,进行扫描service层包体...");
        Set<Class<?>> classes = ClassUtils.getClasses(servicePackage);
        classes.forEach(action ->
        {
            if (action.isAnnotationPresent(Timing.class))
            {
                try
                {
                    Runnable runnable = (Runnable) action.getDeclaredConstructor(ServletContext.class).newInstance(sce.getServletContext());
                    scheduler.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.MINUTES);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        LOGGER.debug("扫描结束:" + classes);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            scheduler.shutdown();
            boolean result = scheduler.awaitTermination(10, SECONDS);
            System.out.println("进行销毁线程:" + result);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
