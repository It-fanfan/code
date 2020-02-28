package com.fish.utils;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.CustomerMaterialMapper;
import com.fish.dao.primary.model.CustomerMaterial;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;

public class MaterialJob extends QuartzJobBean {
    //@Autowired
    CustomerMaterialMapper customerMaterialMapper;

   // @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
      /*  System.out.println("我是进入任务");
        HttpPostUploadUtil util = new HttpPostUploadUtil();
        String filepath = "E:\\IdeaProjects\\CustomerService\\CustomerService\\src\\main\\resources\\微信扫码.jpg";
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("", "微信扫码");
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("userfile", filepath);
        String mediaidrs = util.formUpload(textMap, fileMap);
        JSONObject mediaInfo = JSONObject.parseObject(mediaidrs);
        System.out.println("我是media ： " + mediaInfo.toJSONString());
        String mediaid = mediaInfo.getString("media_id");
        String type = mediaInfo.getString("type");
        String created_at = mediaInfo.getString("created_at");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CustomerMaterial customerMaterial = new CustomerMaterial();
        customerMaterial.setMediaId(mediaid);
        customerMaterial.setMaterialType(type);
        customerMaterial.setMaterialCreated(created_at);
        System.out.println("我是media ： " + mediaInfo.toJSONString());*/

       // int insert = customerMaterialMapper.insert(customerMaterial);
        //System.out.println("我是插入返回的数据" + insert);

    }
}
