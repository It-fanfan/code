package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Vector;

@Entity(name = "init_config")
public class PeDbInitConfig extends PeDbObject implements Serializable {
    public static final Logger LOG = LoggerFactory.getLogger(PeDbInitConfig.class);
    /**
     * 配置
     */
    //版本
    public String ddVersion;
    //AppI
    public String ddAppId;
    //壳子标识
    public boolean ddJokeLogo;
    //商品描述
    public String ddGameUrl;
    //游戏集合
    public int ddCode;

    //
    // 全部配置信息
    //
    private static Vector<PeDbObject> allGoods = new Vector<>();
    /**
     * 获取游戏信息
     */
    public static PeDbInitConfig getConfigsFast(String appId,String version)
    {
        for (int i = 0; i < allGoods.size(); i++)
        {
            PeDbInitConfig config = (PeDbInitConfig) allGoods.elementAt(i);
            if(config != null){
                if (config.ddAppId.equals(appId) && config.ddVersion.equals(version))
                {
                    LOG.info("_config :"+config);
                    return config;
                }
            }

        }
        return null;
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
            objects = PeDbInitConfig.queryObject(sqlResource, PeDbInitConfig.class,
                    "");
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
        allGoods = getConfigs();
    }
    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncConfigs();
    }
}
