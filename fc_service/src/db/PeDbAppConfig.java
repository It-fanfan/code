package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "app_config")
public class PeDbAppConfig extends PeDbObject implements Serializable
{
    public static final Logger LOG = LoggerFactory.getLogger(PeDbAppConfig.class);
    /**
     * 配置
     */
    //审核版本
    public String ddCheckVersion;
    //AppId
    public String ddAppId;
    //是否为小程序
    public int ddProgram;
    //商品描述
    public String ddGameUrl;
    //游戏集合
    public int ddCode;
    //审核集合
    public int ddCheckCode;

    //
    // 全部配置信息
    //
    private static Map<String, PeDbAppConfig> allConfig = new ConcurrentHashMap<>();

    /**
     * 配置内容
     *
     * @param appId 应用编号
     * @return 合集配置
     */
    public static PeDbAppConfig getConfigsFast(String appId)
    {
        return allConfig.get(appId);
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
            objects = PeDbAppConfig.queryObject(sqlResource, PeDbAppConfig.class, "");
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
                PeDbAppConfig element = (PeDbAppConfig) data;
                allConfig.put(element.ddAppId, element);
            });
        }
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncConfigs();
    }
}


