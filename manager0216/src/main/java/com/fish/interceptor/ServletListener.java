package com.fish.interceptor;

import com.fish.service.DataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ServletListener implements ServletContextListener
{

    @Autowired
    UpdateOnline updateOnline;

    @Autowired
    DataCollectService updateDataCollect;

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        System.out.println("===========================MyServletContextListener销毁");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        System.out.println("===========================MyServletContextListener初始化");
        System.out.println(sce.getServletContext().getServerInfo());
        //初始启动一次，每隔1个小时维护一次
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(15);
//         executorService.scheduleAtFixedRate(updateOnline, 0, 10, TimeUnit.MINUTES);
        long oneDay = 24 * 60 * 60 * 1000;
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        long initDelay = getTimeMillis("15:15:00") - System.currentTimeMillis();
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
        executorService.scheduleAtFixedRate(updateDataCollect, initDelay, oneDay, TimeUnit.MILLISECONDS);
        System.out.println("执行汇总数据刷新 时间："+dateFormat.format(System.currentTimeMillis()));
    }

    private static long getTimeMillis(String time)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");

            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);

            return curDate.getTime();

        } catch (Exception e)
        {

            e.printStackTrace();

        }

        return 0;

    }
}
