package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadConfig
{
    // 配置文件路径
    private static final String CONFIG_FILE_NAME = "common.properties";

    //
    // 定义微信支付回调
    //
    public static String wxNotifyUrl = "";

    //注册赠送金币数
    public static int registerCoin = 0;

    //视频免费开始次数
    public static int videoFreeGame = 0;
    //视频免费复活次数
    public static int videoFreeRelive = 0;

    //公衆號授权
    public static String authorization = null;
    //查看token
    public static String refreshToken = null;
    //公众号获取用户信息
    public static String userinfoSns = null;

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
            wxNotifyUrl = properties.getProperty("wxNotifyUrl");
            registerCoin = Integer.valueOf(properties.getProperty("registerCoin"));
            videoFreeGame = Integer.valueOf(properties.getProperty("videoFreeGame"));
            videoFreeRelive = Integer.valueOf(properties.getProperty("videoFreeRelive"));
            authorization = properties.getProperty("authorization");
            refreshToken = properties.getProperty("refreshToken");
            userinfoSns = properties.getProperty("userinfoSns");
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

    public static int getInt(String key)
    {
        try
        {
            String value = get(key);
            if (value != null)
                return Integer.valueOf(value);
            return 0;
        } catch (Exception e)
        {
            return 0;
        }
    }

}
