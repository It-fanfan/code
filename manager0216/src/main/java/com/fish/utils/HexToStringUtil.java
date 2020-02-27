package com.fish.utils;

import java.io.UnsupportedEncodingException;

public class HexToStringUtil
{
    /**
     * 从二进制到字符串的方法
     *
     * @param hexString 二进制信息
     * @return 得到的字符串信息
     */
    public static String getStringFromHex(String hexString)
    {
        String gb2312 = "";
        byte[] bytes = new byte[hexString.length() / 2];

        for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
        {
            String value = "0x" + hexString.substring(i, i + 2);

            bytes[j] = Integer.decode(value).byteValue();
        }

        try
        {
            gb2312 = new String(bytes, "gb2312");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return gb2312;
    }

    /**
     * 从字符串到二进制流的方法
     *
     * @param content 源字串信息
     * @return 得到的二进制流的文本描述
     */
    public static String getStringToHex(String content)
    {
        byte[] bytes = new byte[0];
        try
        {
            bytes = content.getBytes("gb2312");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String temp = null;

        for (int i = 0; i < bytes.length; i++)
        {
            temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1)
            {
                sb.append("0");
            }
            sb.append(temp);
        }

        return sb.toString();
    }

}
