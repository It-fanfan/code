package com.code.servlet.utils;

import java.util.Map;

public class PartnerSignUtil {
    /**
     * 签名字段名称
     */
    public final static String SIGNATURE = "sign";

    /**
     * 签名方法字段名称
     */
    public final static String SIGN_METHOD = "sign_type";


    /**
     * 签名字符串
     * @param paraMap 需要签名的参数Map
     * @return 签名结果
     */
    public static String sign(Map<String,String> paraMap, String key) {
        String saltValue = key;
        return MD5SignUtil.sign(paraMap, saltValue, SIGNATURE, SIGN_METHOD);
    }

    /**
     * 验证签名
     * @param paraMap 参数Map
     * @return 验签结果
     */
    public static boolean verify(Map<String, String> paraMap, String key) {
        String saltValue = key;
        String sign = paraMap.get("sign");
        return MD5SignUtil.verify(paraMap, sign, saltValue, SIGNATURE, SIGN_METHOD);
    }
}
