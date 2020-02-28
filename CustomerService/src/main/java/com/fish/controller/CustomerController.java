package com.fish.controller;

import com.fish.dao.primary.model.WxConfig;
import com.fish.interceptor.WeixinToken;
import com.fish.service.WechatAccountService;
import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

@Controller
@RequestMapping("/server")
public class CustomerController {

    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    WechatAccountService wechatAccountService;

    @GetMapping("/game/{appId}")
    public void GETProgram(HttpServletRequest request, HttpServletResponse response, @PathVariable String appId) {

        System.out.println("请求参数" + request.getRequestURI() + ",appId=" + appId);
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
            WxConfig wxConfig = WeixinToken.getWxConfig(appId);
            out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
            if (wxConfig != null && checkSignature(signature, timestamp, nonce, wxConfig.getDdtoken())) {
                logger.info("成功_" + echostr);
                out.print(echostr);
                out.flush(); //必须刷新
            }
            logger.info("失败:" + signature + ",nonce=" + nonce + "config=" + wxConfig);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(out).close();
        }
    }

    @PostMapping("/game/{appId}")
    public void postGameRequest(HttpServletRequest request, HttpServletResponse response, @PathVariable String appId) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String AppId = request.getParameter("AppId");
//       logger.info("AppId : " +AppId +"  gameAppId  : "+ gameAppId  );
        String result = wechatAccountService.acceptCustomerMessage(request, response, appId);
//        String result = "";
        PrintWriter out = response.getWriter();
        out.print(result);
        out.close();

    }
    /**
     * 验证签名
     */
    private boolean checkSignature(String signature, String timestamp, String nonce, String token) {

        String sha = getSHA1(token, timestamp, nonce);
        logger.info("sha:" + sha + ",signature:" + signature);
        return signature.toLowerCase().equals(sha.toLowerCase());
    }
    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     令牌
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 安全签名
     */
    public static String getSHA1(String token, String timestamp, String nonce) {
        try {
            String[] array = new String[]{token, timestamp, nonce};
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 3; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            logger.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }
    /**
     * 将字节数组转换为十六进制字符串
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }
    /**
     * 将字节转换为十六进制字符串
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }
}
