package com.code.service.achievement;

import com.annotation.Timing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * 成就标签，分定时添加
 */
@Timing
public class AchievementRunnable implements Runnable
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AchievementService.class);

    //缓存队列数据
    private ServletContext application;

    public AchievementRunnable(ServletContext servletContext)
    {
        this.application = servletContext;
    }

    @Override
    public void run()
    {
        if (this.application != null)
        {
            List onlineUserList = (List) application.getAttribute("onlineUserList");
            if (onlineUserList != null)
            {
                LOGGER.debug("当前在线人数:" + onlineUserList.size());
            }
        }
    }
}
