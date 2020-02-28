package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.OnlineMapper;
import com.fish.dao.primary.model.Online;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OnlineService implements BaseService<Online>
{

    @Autowired
    OnlineMapper onlineMapper;

    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;

    //是否进行对比
    private boolean isCompare = false;
    //选择线类型
    private String lineType = "online";

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<Online> getClassInfo()
    {
        return Online.class;
    }

    /**
     * 获取折线图数据
     *
     * @param data 查找数据
     */
    public JSONObject getEcharts(List<Online> data)
    {
        JSONObject lineData = new JSONObject();
        //xAxis 点数据
        JSONArray xAxis = new JSONArray();
        //点数：144个
        for (int i = 0; i < 144; i++)
        {
            int val = i * 10;
            int x = val % 60, y = val / 60;
            xAxis.add(String.format("%02d:%02d", y, x));
        }
        lineData.put("xAxis", xAxis);
        //series 线集合 {name:string,data:array,smooth:false}
        JSONArray series = new JSONArray();
        //数据点进行换算
        DateFormat format = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Map<String, int[]> cache = new HashMap<>();
        data.forEach(point ->
        {
            String time = format.format(point.getTimes());
            String[] val = time.split(":");
            //获取当前key值
            int key = (Integer.valueOf(val[0]) * 60 + Integer.valueOf(val[1])) / 10;
            String day = dateFormat.format(point.getTimes());
            Integer online = point.getOnline();
            Integer buzy = point.getBuzyroom();
            Integer idle = point.getIdleroom();
            putValue(cache, key, day + "-online", online);
            putValue(cache, key, day + "-buzy", buzy);
            putValue(cache, key, day + "-idle", idle);
        });
        cache.forEach((k, v) ->
        {
            JSONObject info = new JSONObject();
            if (isCompare && !k.endsWith(lineType))
            {
                return;
            }
            String name = "剩余房间数";
            if (k.endsWith("online"))
                name = "在线人数";
            else if (k.endsWith("buzy"))
                name = "房间占用数";
            if (isCompare)
                name = k.replace(lineType, name);
            info.put("name", name);
            info.put("data", v);
            info.put("smooth", k.endsWith("idle"));
            series.add(info);
        });
        if (series.isEmpty())
        {
            lineData.put("xAxis", new JSONArray());
        }
        lineData.put("series", series);
        return lineData;
    }

    /**
     * 设置点值
     */
    private void putValue(Map<String, int[]> cache, int key, String day, Integer val)
    {
        int[] value = cache.get(day);
        if (value == null)
            value = new int[144];
        value[key] = val != null ? val : 0;
        cache.put(day, value);
    }

    @Override
    public boolean removeIf(Online online, JSONObject searchData)
    {
        return false;
    }

    @Override
    public List<Online> selectAll(GetParameter parameter)
    {
        isCompare = false;
        JSONObject search = getSearchData(parameter.getSearchData());
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String start, end;
        if (search == null || search.getString("times").isEmpty())
        {
            end = start = "'" + format.format(now) + "'";
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            start = "'" + format.format(parse[0]) + "'";
            end = "'" + format.format(parse[1]) + "'";
            isCompare = !start.equals(end);
        }
        //线逻辑
        lineType = "online";
        if (search != null && search.containsKey("lineType") && !search.getString("lineType").trim().isEmpty())
        {
            lineType = search.getString("lineType");
        }
        String SQL = "SELECT * FROM  online WHERE Date(insertTime) between " + start + " and " + end;
        System.out.println(SQL);
        List<Online> data = onlineMapper.selectCurrent(SQL);
        Vector<Online> result = new Vector<>();
        //游戏名称
        if (search != null && search.containsKey("gameName") && !search.getString("gameName").trim().isEmpty())
        {
            String gameCode = search.getString("gameName");
            //游戏进行解析
            for (Online online : data)
            {
                String json = online.getGameinfo();
                if (json == null)
                    continue;
                JSONObject game = JSONObject.parseObject(json);
                if (game == null)
                    continue;
                JSONObject room = game.getJSONObject(gameCode);
                if (room == null)
                    continue;
                Online line = new Online();
                line.setInserttime(online.getInserttime());
                line.setTimes(online.getTimes());
                line.setOnline(room.getInteger("total"));
                line.setBuzyroom(room.getInteger("room"));
                result.add(line);
            }
        } else
        {
            result.addAll(data);
        }
        return result;
    }
}
