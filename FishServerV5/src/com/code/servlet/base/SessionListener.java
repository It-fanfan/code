package com.code.servlet.base;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.service.mq.RabbitMQ;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@WebListener
public class SessionListener implements HttpSessionListener
{
    private static Logger LOG = LoggerFactory.getLogger(SessionListener.class);

    public void sessionCreated(HttpSessionEvent event)
    {
        HttpSessionListener.super.sessionCreated(event);
        HttpSession session = event.getSession();
        session.setMaxInactiveInterval(60);
        LOG.warn("Session创建:" + session.getId() + ",interval=" + session.getMaxInactiveInterval() + "," + session.getCreationTime());

    }

    public void sessionDestroyed(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();
        long create = session.getCreationTime();
        long last = session.getLastAccessedTime();
        JSONObject json = new JSONObject();
        json.put("sessionId", session.getId());
        json.put("times", last - create);
        try
        {
            ServletContext application = session.getServletContext();
            String key = "USER-ID";
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            UserCache cache = (UserCache) session.getAttribute(key);
            json.put("interval", session.getMaxInactiveInterval());
            json.put("create", format.format(new Timestamp(create)));
            json.put("last", format.format(new Timestamp(last)));
            json.put("userCache", cache != null);
            if (cache != null)
            {
                String userId = cache.getUserId();
                json.put("user", userId + "(" + cache.getNickName() + ")");
                // 从在线列表中删除用户名
                List onlineUserList = (List) application.getAttribute("onlineUserList");
                onlineUserList.remove(userId);
                RabbitMQ.addOnlineQueue(cache.userId(), cache.getBasin(), create, last);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        LOG.warn("Session销毁:" + json.toJSONString());
    }
}
