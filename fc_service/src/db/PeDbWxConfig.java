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
<<<<<<< HEAD
public class PeDbWxConfig extends PeDbObject implements Serializable {
=======
public class PeDbWxConfig extends PeDbObject implements Serializable
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
<<<<<<< HEAD
    public static PeDbWxConfig getConfigFast(String appId) {
=======
    public static PeDbWxConfig getConfigFast(String appId)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return allConfig.get(appId);
    }

    /**
     * 获取微信配置列表
     */
<<<<<<< HEAD
    private static Map<String, PeDbWxConfig> getAllConfigs() {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<String, PeDbWxConfig> hash = new HashMap<>();
        try {
=======
    private static Map<String, PeDbWxConfig> getAllConfigs()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<String, PeDbWxConfig> hash = new HashMap<>();
        try
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            Vector<PeDbObject> objects = PeDbWxConfig.queryObject(sqlResource, PeDbWxConfig.class, "");
            objects.forEach(obj ->
            {
                PeDbWxConfig config = (PeDbWxConfig) obj;
                config.loadRes();
                hash.put(config.ddAppId, config);
            });
<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return hash;
    }

    /**
     * 加载资源
     */
<<<<<<< HEAD
    private void loadRes() {
        String startWith = MessageFormat.format(ReadConfig.get("res-host"), ddAppId);
        try {
            String text = ddShareRes;
            if (text != null) {
                JSONArray array = JSONObject.parseArray(text);
                if (array != null) {
                    JSONArray data = new JSONArray();
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject share = array.getJSONObject(i);
                        if (share.containsKey("state") && !share.getBoolean("state"))
                            continue;
                        String url = share.getString("url");
                        share.put("url", url.startsWith("http") ? url : startWith.concat("share/" + url));
                        data.add(share);
                    }
                    shareRes = data;
                }

            }

            String skipText = ddAppSkipRes;
            if (skipText != null) {
                skipRes = new JSONObject();
                JSONObject data = JSONObject.parseObject(skipText);
                if (data == null) {
                    return;
                }
                JSONArray banner = data.getJSONArray("banner");
                if (banner != null) {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < banner.size(); i++) {
                        JSONObject info = banner.getJSONObject(i);
                        if (info.containsKey("state") && !info.getBoolean("state")) {
                            continue;
                        }
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                        String url = info.getString("url");
                        info.put("url", url.startsWith("http") ? url : startWith.concat("skip/" + url));
                        array.add(info);
                    }
                    skipRes.put("banner", array);
                }
                JSONArray list = data.getJSONArray("list");
<<<<<<< HEAD
                if (list != null) {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject info = list.getJSONObject(i);
                        if (info.containsKey("state") && !info.getBoolean("state")) {
                            continue;
                        }
                        String url = info.getString("icon");
                        if (info.containsKey("local") && info.getBoolean("local")) {
                            info.put("icon", url);
                        } else {
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                            info.put("icon", url.startsWith("http") ? url : startWith.concat("skip/" + url));
                        }

                        array.add(info);
                    }
                    skipRes.put("list", array);
                }
<<<<<<< HEAD
                String wxBanner = "wxbanner";
                if (data.containsKey(wxBanner)) {
                    skipRes.put(wxBanner, data.getJSONObject(wxBanner));
                }
            }
        } catch (Exception e) {
=======
            }
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取appId跳转配置
     */
<<<<<<< HEAD
    public JSONObject getAppIdConfig() {
        if (skipRes != null && skipRes.size() > 0) {
            return skipRes;
        }
=======
    public JSONObject getAppIdConfig()
    {
        if (skipRes != null && skipRes.size() > 0)
            return skipRes;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return null;
    }

    /**
     * 分享位置
     *
     * @return 配置
     */
<<<<<<< HEAD
    public JSONArray getShareConfig() {
        if (shareRes != null && shareRes.size() > 0) {
            return shareRes;
        }
=======
    public JSONArray getShareConfig()
    {
        if (shareRes != null && shareRes.size() > 0)
            return shareRes;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return null;
    }

    /**
     * 同步游戏列表
     */
<<<<<<< HEAD
    private static void syncConfig() {
=======
    private static void syncConfig()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        allConfig = getAllConfigs();
    }

    /**
     * 初始化数据对象
     */
<<<<<<< HEAD
    public static void init() {
=======
    public static void init()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        syncConfig();
    }
}
