package com.fish.manger.v5;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.CustomerMaterialMapper;
import com.fish.dao.primary.model.CustomerMaterial;
import com.fish.utils.HttpPostUploadUtil;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        HttpPostUploadUtil util=new HttpPostUploadUtil();
        String filepath="E:\\IdeaProjects\\CustomerService\\CustomerService\\src\\main\\resources\\微信扫码.jpg";
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("", "微信扫码");
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("userfile", filepath);
        String mediaidrs = util.formUpload(textMap, fileMap);
        JSONObject mediaInfo = JSONObject.parseObject(mediaidrs);
        System.out.println("我是media ： "+mediaInfo.toJSONString());
//        String mediaid= mediaInfo.getString("media_id");
//        String type= mediaInfo.getString("type");
//        String created_at= mediaInfo.getString("created_at");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        CustomerMaterial customerMaterial = new CustomerMaterial();
//        customerMaterial.setMediaId(mediaid);
//        customerMaterial.setMaterialType(type);
//        customerMaterial.setMaterialCreated(created_at);
//        System.out.println("我是media ： "+mediaInfo.toJSONString());
        //int insert = customerMaterialMapper.insert(customerMaterial);
       // System.out.println("我是插入返回的数据"+insert);
    }
}
