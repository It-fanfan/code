package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.utils.http.XwhHttp;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AppletCommonService {
    public static final String[] keyWord = {"你好", "您好", "请问", "在"};
    public static final List<String> keyList = Arrays.asList(keyWord);
    //	小程序的access_token
    public static final String ACCESS_TOKEN = "";

    /**
     * 小程序客服自动回复功能
     *	@author 13
     * @param msg 微信传递的参数集合
     * @return "SUCCEE"告知微信处理成功
     * @throws Exception
     */
    public String replyMessage(Map<String, Object> msg) throws Exception {
        System.out.println("客服消息自动回复 ====> start");
        String opendId = msg.get("FromUserName").toString();
        Map<String, String> contenMap = new HashMap<String, String>();
        // 发送结果
        String result = "";
        String conten = msg.get("Content") == null ? "" : msg.get("Content").toString();
        //  判断文字中是否包含关键词，若包含则提前自动回复
        boolean flag = false;
        for (String string : keyList) {
            if (conten.startsWith(string)) {
                flag = true;
                break;
            }
        }
        // 若用户消息包含关键字则自动回复
        if (flag) {
            contenMap.put("content", "你好！正在赶来的路上，可以先留下你的问题");
            result = sendMessage(opendId, "text", contenMap);
            System.out.println("客服自动回复结果：" + result);
            // 此处将消息转发至人工客服，不然人工客服窗会没有消息
            Map<String, Object> tranMap = new HashMap<String, Object>();
            tranMap.put("ToUserName", msg.get("FromUserName"));
            tranMap.put("FromUserName", msg.get("ToUserName"));
            tranMap.put("CreateTime", msg.get("CreateTime"));
            tranMap.put("MsgType", "transfer_customer_service");
            String messageJson = JSONObject.toJSONString(tranMap);
            System.out.println("消息转发至人工客服");
            return messageJson;
        }
        //	TODO 此处根据自己的业务逻辑获取微信临时素材id
        String mediaId = "xxxxxxxx";
        //	若没有活动临时素材则不进项操作
        if (StringUtils.isBlank(mediaId)) {
            // 此处将消息转发至人工客服，不然人工客服窗会没有消息
            Map<String, Object> tranMap = new HashMap<String, Object>();
            tranMap.put("ToUserName", msg.get("FromUserName"));
            tranMap.put("FromUserName", msg.get("ToUserName"));
            tranMap.put("CreateTime", msg.get("CreateTime"));
            tranMap.put("MsgType", "transfer_customer_service");
            String messageJson = JSONObject.toJSONString(tranMap);
            System.out.println("消息转发至人工客服");
            return messageJson;
        }
        //  有临时素材照片则自动回复临时素材图片media_id【 活动二维码或推广二维码等】
        contenMap.put("media_id", mediaId);
        result = sendMessage(opendId, "image", contenMap);
        System.out.println("客服自动回复结果：" + result);
        return "SUCCESS";
    }

    /**
     * 客服消息自动回复
     * @author 13
     * @param opendId    接收者openId
     * @param msgtype    消息格式：text 文本；image 图片; link 图文链接
     * @param contentMap 正文map
     * @return
     * @throws Exception
     */
    private String sendMessage(String opendId, String msgtype, Map<String, String> contentMap) throws Exception {
        Map<String, Object> replyMessageMap = new HashMap<String, Object>();
        replyMessageMap.put("access_token", ACCESS_TOKEN);
        replyMessageMap.put("touser", opendId);
        replyMessageMap.put("msgtype", msgtype);
        replyMessageMap.put(msgtype, contentMap);
        String messageJson = JSONObject.toJSONString(replyMessageMap);
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
        // 发送结果
        return XwhHttp.doPostForm(url, replyMessageMap);
    }

}
