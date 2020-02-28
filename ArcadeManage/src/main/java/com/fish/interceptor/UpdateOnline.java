package com.fish.interceptor;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.OnlineMapper;
import com.fish.dao.primary.model.Online;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UpdateOnline implements Runnable
{
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    OnlineMapper onlineMapper;

    @Override
    public void run()
    {
        updateOnlineData();
    }

    public void updateOnlineData()
    {
        //获取在线人数请求URL
        String url = baseConfig.getFlushOnline();
        String res = HttpUtil.get(url);
        if (res != null)
        {
            JSONObject resObject = JSONObject.parseObject(res);
            if (!"success".equals(resObject.getString("result")))
                return;
            System.out.println(res);
            JSONArray server = resObject.getJSONArray("server");
            Online online = new Online();
            online.setTimes(new Date());
            online.setInserttime(new Date());
            AtomicInteger buzyRoom = new AtomicInteger(), idleRoom = new AtomicInteger(), onlineCount = new AtomicInteger();
            JSONObject gameInfo = new JSONObject();
            for (int i = 0; i < server.size(); i++)
            {
                JSONObject jsonObject = server.getJSONObject(i);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int j = 0; j < data.size(); j++)
                {
                    JSONObject roomInfo = data.getJSONObject(j);
                    System.out.println(roomInfo);
                    //空闲房间数
                    int idleCount = roomInfo.getInteger("idleCount");
                    //繁忙房间数
                    int buzyCount = roomInfo.getInteger("buzyCount");
                    buzyRoom.addAndGet(buzyCount);
                    idleRoom.addAndGet(idleCount);
                    JSONArray buzyRooms = roomInfo.getJSONArray("buzyRooms");
                    for (int k = 0; k < buzyRooms.size(); k++)
                    {
                        JSONObject room = buzyRooms.getJSONObject(k);
                        int userCount = room.getInteger("userCount");
                        int gameCode = room.getInteger("gameCode");
                        String key = String.valueOf(gameCode);
                        JSONObject count = gameInfo.getJSONObject(key);
                        if (count == null)
                            count = new JSONObject();
                        if (count.containsKey("room"))
                            count.put("room", count.getInteger("room") + 1);
                        else
                            count.put("room", 1);

                        if (count.containsKey("total"))
                            count.put("total", count.getInteger("total") + userCount);
                        else
                            count.put("total", userCount);
                        gameInfo.put(key, count);
                        onlineCount.addAndGet(userCount);
                    }
                }
            }
            online.setBuzyroom(buzyRoom.get());
            online.setIdleroom(idleRoom.get());
            online.setOnline(onlineCount.get());
            online.setGameinfo(gameInfo.toJSONString());
            onlineMapper.insert(online);
        }
    }
}
