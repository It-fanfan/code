package com.code.service.mq;

import com.alibaba.fastjson.JSONObject;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.utils.ReadConfig;
import com.utils.db.XWHResultSetMapper;
import com.utils.http.XwhHttp;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 微信消息订阅
 */
public class WxNoticeSend
{
    private static Logger LOG = LoggerFactory.getLogger(WxNoticeSend.class);

    private static WxNoticeSend instance;

    private static WxNoticeSend getInstance()
    {
        if (instance == null)
            instance = new WxNoticeSend();
        return instance;
    }

    /**
     * 发送消息订阅内容
     */
    public void subscribeSend(String openid, String template_id, String token)
    {
        String context = getSubscribeReq(openid, template_id, getFishRodTemplate());
        String url = ReadConfig.get("wx_notice_url").replace("ACCESS_TOKEN", token);
        try
        {
            System.out.println(context);
            String result = XwhHttp.sendPostByGson(url, context, "utf-8");
            System.out.println(result);
            LOG.info(result);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取订阅请求内容
     *
     * @return context
     */
    public String getSubscribeReq(String openId, String template_id, JSONObject object)
    {
        JSONObject req = new JSONObject();
        req.put("touser", openId);
        req.put("template_id", template_id);
        req.put("data", object);
        return req.toJSONString();
    }

    /**
     * 获取鱼竿恢复模板
     *
     * @return context
     */
    public JSONObject getFishRodTemplate()
    {
        JSONObject template = new JSONObject();
        Map<String, String> string1 = new HashMap<>();
        string1.put("value", "14/15");
        template.put("character_string1", string1);
        Map<String, String> thing2 = new HashMap<>();
        thing2.put("value", "这个位置好像只有20个字符,占满看看~");
        template.put("thing2", thing2);
        System.out.println(template.toJSONString());
        return template;
    }

    /**
     * 获取TOKEN
     */
    private static String getToken()
    {
        String host = Systemstatusinfo.getText("virtual_host");
        String url = "/midas/token";
        JSONObject req = new JSONObject();
        req.put("appId", ReadConfig.get("appid"));
        req.put("appSecret", ReadConfig.get("secret"));
        String json = XwhHttp.sendPost(host, url, req.toJSONString());
        if (json != null)
        {
            System.out.println("查询内容:" + json);
            LOG.debug("查询Token结果:" + json);
            JSONObject result = JSONObject.parseObject(json);
            if (result.getInteger("code") == 200)
                return result.getString("wxToken");
        }
        return null;
    }


    public static void main(String[] arg)
    {
        XWHResultSetMapper.init(Executors.newScheduledThreadPool(15));
        String openid = "ocymP4pulgfE6q5PJ30pAIiYsll4";
        String token = getToken();
        getInstance().subscribeSend(openid, ReadConfig.get("wx_notice_model"), token);
        System.exit(1);
    }

}
