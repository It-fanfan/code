package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "area_config")
public class PeDbAreaConfig extends PeDbObject
{
    //省份代码-暂废弃
    public String ddCode;
    //省份名称
    public String ddName;
    //国家
    public String ddCountry;
    //国家代码
    public String ddCountryCode;
    //地区服务器优先级
    public String ddArea;

    //
    // 全部配置信息
    //
    private static Map<String, PeDbAreaConfig> allConfig = new ConcurrentHashMap<>();

    /**
     * 配置内容
     *
     * @param ddName 应用编号
     * @return 合集配置
     */
    public static PeDbAreaConfig getConfigsFast(String ddName)
    {
        for (Map.Entry<String, PeDbAreaConfig> entry : allConfig.entrySet())
        {
            String key = entry.getKey();
            if (ddName.startsWith(key))
            {
                return entry.getValue();
            }
        }
        //默认配置
        PeDbAreaConfig config = new PeDbAreaConfig();
        config.ddArea = "0#1#2#3";
        return config;

    }

    /**
     * 获取配置列表
     */
    private static Vector<PeDbObject> getConfigs()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbAreaConfig.queryObject(sqlResource, PeDbAreaConfig.class, "");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    /**
     * 同步游戏列表
     */
    private static void syncConfigs()
    {
        allConfig.clear();
        Vector<PeDbObject> configs = getConfigs();
        if (configs != null)
        {
            configs.forEach(data ->
            {
                PeDbAreaConfig element = (PeDbAreaConfig) data;
                allConfig.put(element.ddName, element);
            });
        }
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        System.out.println("抵用");
        syncConfigs();
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbAreaConfig()
    {
        super();
    }
}
