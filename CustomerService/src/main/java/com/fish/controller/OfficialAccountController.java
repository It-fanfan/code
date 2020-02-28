package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.service.WechatAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/server")
public class OfficialAccountController {

    private static Logger logger = LoggerFactory.getLogger(OfficialAccountController.class);
    private static String WECHAT_TOKEN = "tjxwgWAU0kk1xJuHYsDGUqvftwFAPBao";

    @Autowired
    WechatAccountService wechatAccountService;

    @RequestMapping("/checkToken")
    public void arcade(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        logger.info("微信消息服务器验证传入数据————" + request.getMethod().toLowerCase() + "___" + isGet);
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            PrintWriter out = null;
            try {
                out = response.getWriter();
                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
                if (checkSignature(signature, timestamp, nonce)) {
                    logger.info("成功_" + echostr);
                    out.print(echostr);
                    out.flush(); //必须刷新
                }
                logger.info("失败");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        } else {//进行客服操作
            try {
                // 进入POST聊天处理
                // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                /** 读取接收到的xml消息 */
                StringBuffer sb = new StringBuffer();
                InputStream is = request.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                String xml = sb.toString(); //次即为接收到微信端发送过来的xml数据
                logger.info(xml + "___我是xml数据");
                JSONObject respData = JSONObject.parseObject(xml);
                // 接收消息并返回消息
                String result = WechatAccountService.acceptMessage(request, response, respData);
                // 响应消息
                PrintWriter out = response.getWriter();
                out.print(result);
                out.close();
            } catch (Exception ex) {
                logger.error("微信帐号接口配置失败！", ex);
                ex.printStackTrace();
            }
        }
    }

    @RequestMapping("/checkTokenVg")
    public void videoGame(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        logger.info("微信消息服务器验证传入数据————" + request.getMethod().toLowerCase() + "___" + isGet);
        PrintWriter print;
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            PrintWriter out = null;
            try {
                out = response.getWriter();
                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
                if (checkSignature(signature, timestamp, nonce)) {
                    logger.info("成功_" + echostr);
                    out.print(echostr);
                    out.flush(); //必须刷新
                }
                logger.info("失败");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        } else {//进行客服操作
            try {
                // 进入POST聊天处理
                // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                /** 读取接收到的xml消息 */
                StringBuffer sb = new StringBuffer();
                InputStream is = request.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                String xml = sb.toString(); //次即为接收到微信端发送过来的xml数据
                logger.info(xml + "___我是xml数据");
                JSONObject respData = JSONObject.parseObject(xml);
                // 接收消息并返回消息
                String result = WechatAccountService.acceptMessage(request, response, respData);
                // 响应消息
                PrintWriter out = response.getWriter();
                out.print(result);
                out.close();
            } catch (Exception ex) {
                logger.error("微信帐号接口配置失败！", ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{WECHAT_TOKEN, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        // Arrays.sort(arr);
        sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public static void sort(String[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }
}
