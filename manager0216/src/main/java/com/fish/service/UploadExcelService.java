package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UploadExcelService {

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    WxConfig wxConfig;

    //插入Excel
    public int insert(JSONObject record) {
        String context = record.getString("context");
        System.out.println("context :" + context);
        context = context.substring(1, context.length() - 1);
        try
        {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (int i = 0; i < param.size(); i++)
            {
                String singleData = param.get(i).toString();
                String singleString = singleData.substring(1, singleData.length() - 2);
                System.out.println("我是singleString :" + singleString);
                String[] split = singleString.split("], ");
                for (int j = 0; j < split.length; j++)
                {
                    if (j != 0 && j < split.length)
                    {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        for (int x = 0; x < singleSplit.length; x++)
                        {
                            System.out.println("我是单条数据 " + x + " :" + singleSplit[x]);
                            if (x == 0)
                            {
                                mapSingle.put("product_name", singleSplit[x]);
                            }
                            if (x == 1)
                            {
                                mapSingle.put("add_id", singleSplit[x]);
                            }

                            if (x == 2)
                            {
                                mapSingle.put("ddMchId", singleSplit[x]);
                            }
                            if (x == 3)
                            {
                                mapSingle.put("ddKey", singleSplit[x]);
                            }
                            if (x == 4)
                            {
                                mapSingle.put("program_type", singleSplit[x]);
                            }

                            if (x == 5)
                            {
                                mapSingle.put("origin_name", singleSplit[x]);
                            }
                            if (x == 6)
                            {
                                mapSingle.put("origin_id", singleSplit[x]);
                            }
                            if (x == 7)
                            {
                                mapSingle.put("account", singleSplit[x]);
                            }
                            if (x == 8)
                            {
                                mapSingle.put("password", singleSplit[x]);
                            }
                            if (x == 9)
                            {
                                mapSingle.put("code_manager", singleSplit[x]);
                            }
                            if (x == 10)
                            {
                                mapSingle.put("manager_wechat", singleSplit[x]);
                            }
                            if (x == 11)
                            {
                                mapSingle.put("belong_company", singleSplit[x]);
                            }
                            if (x == 12)
                            {
                                mapSingle.put("clear_company", singleSplit[x]);
                            }
                            if (x == 13)
                            {
                                mapSingle.put("ddAppId", singleSplit[x]);
                            }
                            if (x == 14)
                            {
                                mapSingle.put("ddAppSecret", singleSplit[x]);
                            }
                        }
                        list.add(mapSingle);
                    }
                }

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return 1;
    }
}
