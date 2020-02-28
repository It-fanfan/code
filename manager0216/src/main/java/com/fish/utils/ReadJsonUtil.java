package com.fish.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ReadJsonUtil
{

    private static final Logger LOG = LoggerFactory.getLogger(ReadJsonUtil.class);

    public static String flushTable(String table, String url)
    {

        JSONObject paramMap = new JSONObject();
        paramMap.put("name", table);
        return HttpUtil.post(url, paramMap.toJSONString());
    }


    /**
     * reduce JSON
     *
     * @param jsonString json
     * @return reduced json
     */
    public static String minify(String jsonString)
    {
        boolean in_string = false;
        boolean in_multiline_comment = false;
        boolean in_singleline_comment = false;
        char string_opener = 'x'; // unused value, just something that makes compiler happy

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < jsonString.length(); i++)
        {
            // get next (c) and next-next character (cc)

            char c = jsonString.charAt(i);
            String cc = jsonString.substring(i, Math.min(i + 2, jsonString.length()));

            // big switch is by what mode we're in (in_string etc.)
            if (in_string)
            {
                if (c == string_opener)
                {
                    in_string = false;
                    out.append(c);
                } else if (c == '\\')
                { // no special treatment needed for \\u, it just works like this too
                    out.append(cc);
                    ++i;
                } else
                    out.append(c);
            } else if (in_singleline_comment)
            {
                if (c == '\r' || c == '\n')
                    in_singleline_comment = false;
            } else if (in_multiline_comment)
            {
                if (cc.equals("*/"))
                {
                    in_multiline_comment = false;
                    ++i;
                }
            } else
            {
                // we're outside of the special modes, so look for mode openers (comment start, string start)
                if (cc.equals("/*"))
                {
                    in_multiline_comment = true;
                    ++i;
                } else if (cc.equals("//"))
                {
                    in_singleline_comment = true;
                    ++i;
                } else if (c == '"' || c == '\'')
                {
                    in_string = true;
                    string_opener = c;
                    out.append(c);
                } else if (!Character.isWhitespace(c))
                    out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * 通过网络访问json并读取文件
     *
     * @param url:http://192.168.1.55:8080/persieRes/
     * @return: json文件的内容
     */
    public static String loadJson(String url)
    {

        StringBuilder json = new StringBuilder();
        try
        {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(inputStreamReader);
            String inputLine = null;
            while ((inputLine = in.readLine()) != null)
            {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 通过本地文件访问json并读取
     *
     * @param path：E:/svn/template_uuid.json
     * @return： json文件的内容
     */
    public static String ReadFile(String path)
    {
        StringBuilder lastStr = new StringBuilder();
        File file = new File(path);// 打开文件
        BufferedReader reader = null;
        if (!file.exists())
        {
            try
            {
                FileInputStream in = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));// 读取文件
                String tempString = null;
                while ((tempString = reader.readLine()) != null)
                {
                    lastStr.append(tempString);
                }
                reader.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (Exception ignored)
                    {
                    }
                }
            }
            return lastStr.toString();
        } else
        {
            return null;
        }
    }
}
