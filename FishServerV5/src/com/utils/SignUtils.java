package com.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class SignUtils {
    /**
     * 生成签名
     */
    public static StringBuilder createStr(JSONObject jsonObject) {
        Map<String, Object> signMap = jsonObject.getInnerMap();
        Vector<String> keys = new Vector<>(signMap.keySet());
        keys.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        int len = sb.length();
        for (String key : keys) {
            if (len != sb.length())
                sb.append("&");
            sb.append(key).append("=").append(jsonObject.get(key));
        }
        return sb;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
