package com.fish.service;

import com.fish.interceptor.UploadMedia;
import com.fish.interceptor.WeixinToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ServletListener implements ServletContextListener {

    @Autowired
    WeixinToken weixinToken;
    @Autowired
    UploadMedia media;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("===========================MyServletContextListener销毁");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("===========================MyServletContextListener初始化");
        System.out.println(sce.getServletContext().getServerInfo());
        //初始启动一次，每隔1个小时维护一次
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(15);
        executorService.scheduleAtFixedRate(weixinToken, 0, 1, TimeUnit.HOURS);
        executorService.scheduleAtFixedRate(media, 1, 2, TimeUnit.DAYS);

    }

}
