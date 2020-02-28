package com.code.servlet.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.*;

public class MD5SignUtil {
    /*
     * =
     */
    public static final String QSTRING_EQUAL = "=";

    /*
     * &
     */
    public static final String QSTRING_SPLIT = "&";


    /**
     * 签名字符串
     * 适用支付宝等
     * @param paraMap 需要签名的参数Map
     * @return 签名结果
     */
    public static String sign(Map<String,String> paraMap, String saltValue, String signFieldName, String signMethodFieldName) {
        String paraStr = createLinkString4Sign(paraMap, signFieldName, signMethodFieldName);
        return sign(paraStr, saltValue);
    }

    /**
     * 签名字符串(包含signMethod字段)
     * 适用财付通，威富通等
     * @param paraMap 需要签名的参数Map
     * @return 签名结果
     */
    public static String sign(Map<String,String> paraMap, String saltValue, String signFieldName) {
        String paraStr = createLinkString4Sign(paraMap, signFieldName,null);
        return sign(paraStr, saltValue);
    }

    /**
     * 验证签名
     * @param paraMap 参数Map
     * @return 验签结果
     */
    public static boolean verify(Map<String, String> paraMap, String sign, String saltValue, String signFieldName, String signMethodFieldName) {
        String paraStr = createLinkString4Sign(paraMap, signFieldName, signMethodFieldName);
        return verify(paraStr, sign, saltValue);
    }


    /**
     * 除去请求要素中的空值和签名参数
     * @param para 请求要素
     * @param signFieldName 签名字段的名称
     * @param signMethodFieldName 签名方法字段的名称
     * @return 去掉空值与签名参数后的请求要素
     */
    public static Map<String, String> paraFilter(Map<String, String> para, String signFieldName, String signMethodFieldName) {

        Set<String> filterParaSet = new HashSet<String>();
        filterParaSet.add(signFieldName);
        filterParaSet.add(signMethodFieldName);

        return paraFilter(para, filterParaSet);
    }

    /**
     * 除去请求要素中的空值和签名参数
     * @param para 请求要素
     * @param filterParaSet 需要过滤的字段名称
     * @return 去掉空值与签名参数后的请求要素
     */
    public static Map<String, String> paraFilter(Map<String, String> para, Set<String> filterParaSet) {

        Map<String, String> result = new HashMap<String, String>();

        if (para == null || para.size() <= 0) {
            return result;
        }

        for (String key : para.keySet()) {
            String value = para.get(key);
            if (value == null || value.equals("")
                    || filterParaSet.contains(key)) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串（需要编码）
     * @param para 请求要素
     * @param charset 编码格式
     * @param isNeedSort 是否需要根据key值作升序排列
     * @param isUrlEncode 是否需要URL编码
     * @return 拼接成的字符串
     */
    public static String createLinkString(Map<String, String> para, String charset, boolean isNeedSort, boolean isUrlEncode) {

        List<String> keys = new ArrayList<String>(para.keySet());

        if (isNeedSort){
            Collections.sort(keys);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = para.get(key);

            if (isUrlEncode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }
    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串(不编码)
     * @param para 请求要素
     * @param isNeedSort 是否需要根据key值作升序排列
     * @return 拼接成的字符串
     */
    public static String createLinkString(Map<String, String> para,  boolean isNeedSort) {
        return createLinkString(para,null,isNeedSort,false);
    }

    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     */
    public static String createLinkString4Sign(Map<String, String> para, String signFieldName, String signMethodFieldName) {
        Map<String, String> filteredPara = paraFilter(para, signFieldName, signMethodFieldName);
        return createLinkString(filteredPara, true);
    }

    /**
     * 签名字符串（编码格式默认为UTF-8）
     * @param text 需要签名的字符串
     * @param saltValue 盐值
     * @return 签名结果
     */
    public static String sign(String text, String saltValue) {
        return sign(text, saltValue, "UTF-8");
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param saltValue 盐值
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String saltValue, String input_charset) {
        text = text +"&"+ DigestUtils.md5Hex(getContentBytes(saltValue, input_charset));
        System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串（编码格式默认为UTF-8）
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param saltValue 盐值
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String saltValue) {
        return verify(text, sign, saltValue, "UTF-8");
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param saltValue 盐值
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String saltValue, String input_charset) {
        text = text + saltValue;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        return mysign.equals(sign);
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }else{
            try {
                return content.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
            }
        }
    }
}
