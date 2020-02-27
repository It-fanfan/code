package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadConfig
{
    // 配置文件路径
    private static final String CONFIG_FILE_NAME = "config.properties";

    // 唯一事例
    private static ReadConfig instance;

    static
    {
        init();
    }

    // 配置缓存
    private Map<String, String> configMap;

    private ReadConfig()
    {
        configMap = new HashMap<>();
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
     * @param key key
     * @return boolean
     */
    public static boolean containsKey(String key)
    {
        return instance.configMap.containsKey(key);
    }

    /**
     * 获取values值
     *
     * @param key key
     * @return value
     */
    public static String get(String key)
    {
        return instance.configMap.get(key);
    }

}
