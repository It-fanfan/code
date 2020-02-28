package com.fish.utils;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件路径
 *
 * @author xuweihua
 */
public class ReadConfig
{
    // 配置文件路径
    private static final String CONFIG_FILE_NAME = "pictureUrl.properties";

    // 唯一事例
    private static ReadConfig instance;
    private static String classPath;

    static
    {
        init();

        try
        {
            classPath = URLDecoder.decode(ReadConfig.class.getResource("/").getPath(), "UTF-8") + CONFIG_FILE_NAME;
            classPath = classPath.substring(1, classPath.length());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    // 配置缓存
    private Map<String, String> configMap;

    private ReadConfig()
    {
        configMap = new HashMap<String, String>();
        try
        {
            InputStream in = ReadConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            Properties properties = new Properties();
            properties.load(in);
            for (Object key : properties.keySet())
            {
                configMap.put((String) key, properties.getProperty((String) key));
            }
            in.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 初始化配置
     */
    public static void init()
    {
        instance = new ReadConfig();
    }

    /**
     * 进行匹配KEY值
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key)
    {
        return instance.configMap.containsKey(key);
    }

    /**
     * 获取values值
     *
     * @param key
     * @return
     */
    public static String get(String key)
    {
        return instance.configMap.get(key);
    }

    /**
     * 写入value
     *
     * @param value
     * @throws IOException
     */
    public static void writerValue(String key, String value) throws IOException
    {
        InputStream in = ReadConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
        Properties properties = new Properties();
        properties.load(in);

        OutputStream fileOutputStream = new FileOutputStream(classPath);
        properties.setProperty(key, value);
        properties.store(fileOutputStream, "");

        in.close();
        fileOutputStream.close();
    }
}
