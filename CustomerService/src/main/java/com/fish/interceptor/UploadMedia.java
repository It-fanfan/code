package com.fish.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.WxConfigMapper;
import com.fish.dao.primary.model.WxConfig;
import com.fish.utils.BaseConfig;
import com.fish.utils.HttpPostUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UploadMedia implements Runnable {
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    WxConfigMapper mapper;

    @Override
    public void run() {
        File dir = new File(baseConfig.getUpload());
        for (File path : Objects.requireNonNull(dir.listFiles())) {
            if (path.isDirectory()) {
                String appId = path.getName();
                uploadMedia(appId, path);
            }
        }
    }

    /**
     * 上报素材文件夹
     *
     * @param appId    应用编号
     * @param mediaDir 素材文件夹
     */
    private void uploadMedia(String appId, File mediaDir) {
        WxConfig wxConfig = WeixinToken.getWxConfig(appId);
        if (wxConfig == null) return;
        long update = 0;
        File currentFile = null;
        for (File newFile : Objects.requireNonNull(mediaDir.listFiles())) {
            if (mediaDir.isFile() && (mediaDir.getName().endsWith("png") || mediaDir.getName().endsWith("jpg"))) {
                long time = newFile.lastModified();
                if (update < time) {
                    update = time;
                    currentFile = newFile;
                }
            }
        }
        if (update > 0) {
            HttpPostUploadUtil uploadUtil = new HttpPostUploadUtil(wxConfig);
            Map<String, String> textMap = new HashMap<>();
            textMap.put("", "微信扫码");
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("userfile", currentFile.getPath());
            String mediaidrs = uploadUtil.formUpload(textMap, fileMap);
            JSONObject result = JSONObject.parseObject(mediaidrs);
            if (result.containsKey("media_id")) {
                String media_id = result.getString("media_id");
                wxConfig.setDdmediaid(media_id);
                mapper.updateByPrimaryKey(wxConfig);
            }
        }
    }
}
