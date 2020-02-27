package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Entity(name = "wx_config")
public class PeDbWxConfig extends PeDbObject implements Serializable
{
    //app ID
    public String ddAppId;
    //app secret
    public String ddAppSecret;
    //mchid
    public String ddMchId;
    //p12 password
    public String ddP12Password;
    // app key
    public String ddKey;
    // p12 path
    public String ddP12;
    //分享图
    public String ddShareRes;
    //跳转配置
    public String ddAppSkipRes;

    public JSONArray shareRes = null;

    public JSONObject skipRes = null;

    //
    // 全部微信配置
    //
    private static Map<String, PeDbWxConfig> allConfig = new HashMap<>();

    /**
     * 获取微信配置信息
     */
    public static PeDbWxConfig getConfigFast(String appId)
    {
        return allConfig.get(appId);
    }

    /**
     * 获取微信配置列表
     */
    private static Map<String, PeDbWxConfig> getAllConfigs()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<String, PeDbWxConfig> hash = new HashMap<>();
        try
        {
            Vector<PeDbObject> objects = PeDbWxConfig.queryObject(sqlResource, PeDbWxConfig.class, "");
            objects.forEach(obj ->
            {
                PeDbWxConfig config = (PeDbWxConfig) obj;
                config.loadRes();
                hash.put(config.ddAppId, config);
            });
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return hash;
    }

    /**
     * 加载资源
     */
    private void loadRes()
    {
        String startWith = MessageFormat.format(ReadConfig.get("res-host"), ddAppId);
        try
        {
            String text = ddShareRes;
            if (text != null)
            {
                JSONArray data = new JSONArray();
                JSONArray array = JSONObject.parseArray(text);
                for (int i = 0; i < array.size(); i++)
                {
                    JSONObject share = array.getJSONObject(i);
                    if (share.containsKey("state") && !share.getBoolean("state"))
                        continue;
                    String url = share.getString("url");
                    share.put("url", url.startsWith("http") ? url : startWith.concat("share/" + url));
                    data.add(share);
                }
                shareRes = data;
            }

            String skipText = ddAppSkipRes;
            if (skipText != null)
            {
                LOG.debug("skipText:" + skipText);
                skipRes = new JSONObject();
                JSONObject data = JSONObject.parseObject(skipText);
                if (data == null)
                    return;
                JSONArray banner = data.getJSONArray("banner");
                if (banner != null)
                {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < banner.size(); i++)
                    {
                        JSONObject info = banner.getJSONObject(i);
                        if (info.containsKey("state") && !info.getBoolean("state"))
                            continue;
                        String url = info.getString("url");
                        info.put("url", url.startsWith("http") ? url : startWith.concat("skip/" + url));
                        array.add(info);
                    }
                    skipRes.put("banner", array);
                }
                JSONArray list = data.getJSONArray("list");
                if (list != null)
                {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < list.size(); i++)
                    {
                        JSONObject info = list.getJSONObject(i);
                        if (info.containsKey("state") && !info.getBoolean("state"))
                            continue;
                        String url = info.getString("icon");
                        if (info.containsKey("local") && info.getBoolean("local"))
                        {
                            info.put("icon", url);
                        } else
                        {
                            info.put("icon", url.startsWith("http") ? url : startWith.concat("skip/" + url));
                        }

                        array.add(info);
                    }
                    skipRes.put("list", array);
                }
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取appId跳转配置
     */
    public JSONObject getAppIdConfig()
    {
        if (skipRes != null && skipRes.size() > 0)
            return skipRes;
        return null;
    }

    /**
     * 分享位置
     *
     * @return 配置
     */
    public JSONArray getShareConfig()
    {
        if (shareRes != null && shareRes.size() > 0)
            return shareRes;
        return null;
    }

    /**
     * 同步游戏列表
     */
    private static void syncConfig()
    {
        allConfig = getAllConfigs();
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncConfig();
    }
}
