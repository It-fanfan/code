package com.fish.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.CustomerMaterial;
import com.fish.dao.primary.model.WxConfig;
import com.fish.interceptor.WeixinToken;
import com.fish.protocols.GetParameter;
import com.fish.utils.BaseConfig;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WechatAccountService implements BaseService<CustomerMaterial> {

    @Autowired
    BaseConfig baseConfig;

    private static Logger logger = LoggerFactory.getLogger(WechatAccountService.class);

//    @Autowired
//    CustomerMaterialMapper customerMaterialMapper;

    public String acceptCustomerMessage(HttpServletRequest request, HttpServletResponse response, String appId) throws IOException {
        String respMessage = "";
        String mediaId = "";
        JSONObject respData = getWechatInfo(request); //接受微信request需要此方式接收
        //JSONObject respData = (JSONObject) getParameterMap(request);//此方式为本地测试postman接收map参数
        String AppId = respData.getString("AppId");

        logger.info("AppId : " + AppId + "传入AppId" + appId);

        WxConfig wxConfig = WeixinToken.getWxConfig(appId);
        if (wxConfig == null) {
            return "无效应用，请检查配置参数在处理~~~";
        }
        logger.info("接收用户发送的信息-----respData-----" + respData);
        try {
            // 发送方帐号（OpenID）
            String fromUserName = respData.getString("FromUserName");
            // 开发者微信号
            String toUserName = respData.getString("ToUserName");
            String createTime = respData.getString("CreateTime");
            // 消息类型
            String msgType = respData.getString("MsgType");
            logger.info("msgType" + msgType);
            //文本消息内容
            String html = "" + fromUserName;
            String url = "☞ <a href=\"" + html + "\">点击跳转</a>";
            switch (msgType) {
                case "event":
                    logger.info("公众号接受event事件..........");
                    break;
                case "text":
                    logger.info("公众号接受文字..........");
                    sendMessageContext(fromUserName, "这是自动回复，工作日有客服MM专门服务哦", wxConfig.getDdaccesstoken());
                    Map<String, Object> rs = Maps.newHashMap();
                    rs.put("MsgType", "transfer_customer_service");
                    rs.put("ToUserName", fromUserName);
                    rs.put("FromUserName", toUserName);
                    rs.put("CreateTime", createTime);
                    respMessage = JSON.toJSONString(rs);
                    break;
                case "image":
                    logger.info("公众号接受图片..........");
                    Map<String, Object> imgRs = Maps.newHashMap();
                    imgRs.put("MsgType", "transfer_customer_service");
                    imgRs.put("ToUserName", fromUserName);
                    imgRs.put("FromUserName", toUserName);
                    imgRs.put("CreateTime", createTime);
                    respMessage = JSON.toJSONString(imgRs);
                    break;
                case "miniprogrampage":
                    logger.info("公众号接受链接图片..........");
                    respMessage = sendMsToAccount(fromUserName, wxConfig);
//                  JSONObject jsonObject = JSONObject.parseObject(respMessage);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(respMessage + "下发数据resDate");
        return respMessage;
    }

    /**
     * 回复文本消息
     *
     * @param text 消息的参数
     */
    private static void sendMessageContext(String openid, String text, String access_token) throws Exception {
        Map<String, Object> map_content = new HashMap<>();
        map_content.put("content", text);
        JSONObject map = new JSONObject();
        map.put("touser", openid);
        map.put("msgtype", "text");
        map.put("text", map_content);
        customSend(map, access_token);
    }

    /**
     * post请求,发送消息
     */
    private static void customSend(JSONObject json, String accessToken) {

        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(json.toString());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            System.out.println("客服消息result：" + result);
        } catch (Exception e) {
            System.out.println("向客服发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 回复二维码消息给用户
     */
    private String sendMsToAccount(String touser, WxConfig wxConfig) {
        String media_id = wxConfig.getDdmediaid();
        String result = "";
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map_content = new HashMap<>();
        map_content.put("media_id", media_id);
        jsonObject.put("touser", touser);
        jsonObject.put("msgtype", "image");
        jsonObject.put("image", map_content);
        logger.info("我是jsonObject :" + jsonObject.toString());
        try {
            customSend(jsonObject, wxConfig.getDdaccesstoken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 接收微信request data
     *
     * @return JSONObjectData
     * @throws IOException
     */
    private JSONObject getWechatInfo(HttpServletRequest request) throws IOException {
        /** 读取接收到的xml消息 */
        StringBuilder sb = new StringBuilder();
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        String xml = sb.toString();

        //FileUtils.readFileToString()
        logger.info(xml + "___我是xml数据");
        return JSONObject.parseObject(xml);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<CustomerMaterial> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(CustomerMaterial customerMaterial, Map<String, String> searchData) {
        return false;
    }

    @Override
    public List<CustomerMaterial> selectAll(GetParameter parameter) {
        return null;
    }
}