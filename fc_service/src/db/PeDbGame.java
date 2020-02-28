package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import config.ReadConfig;
import tool.CmTool;
import tool.Log4j;

import javax.persistence.Entity;
import java.text.MessageFormat;
import java.util.Vector;

/**
 * @author feng
 */
@Entity(name = "games")
@ManagedData(name = "persie_deamon")
public class PeDbGame extends PeDbObject
{

    private static final long serialVersionUID = -6943358256367635402L;

    //
    // 全部游戏的列表
    //
    public static Vector<PeDbObject> allGames = new Vector<>();

    //
    // 游戏代号信息（四位数字，唯一）
    //
    public int ddCode = 0;

    //
    // 游戏名称信息
    //
    public String ddName;

    //
    // 游戏单人组队所需金币数
    //
    public int ddSingleCoin = 0;

    //
    // 游戏多人组队所需金币数
    //
    public int ddMultiCoin = 0;

    //
    // 游戏容纳人数上限
    //
    public int ddMaxPlayer = 0;

    //
    // 游戏是否为PK
    //
    public int ddIsPk = 0;
    //
    //是否允许单机
    //
    public boolean ddAllowSingle = false;

    //
    // 游戏角色个数
    //
    public int ddRoleCount = 0;

    //
    // 游戏手柄方案
    //
    public int ddRocker = 0;

    //
    // 游戏引擎方案
    //
    public int ddEngine = 0;

    //
    // 游戏屏幕分辨率
    //
    public int ddResolution = 0;
    //
    // 是否自动选择角色
    //
    public boolean ddAutoSelect = false;
    //
    // 是否自动复活选人
    public boolean ddCanSelect = false;
    //
    // 圈子链接地址
    //
    public String ddFriendUrl;

    //旋转屏幕
    public int ddRotate = 0;

    public String ddShareRes = null;
    //分享链接参数
    public JSONArray shareRes = null;

    /**
     * 同步游戏列表
     */
    public static void syncGames()
    {
        allGames = getGames();
        LOG.debug("有效游戏数据:" + CmTool.getJSONByFastJSON(allGames));
    }

    /**
     * 获取游戏信息
     */
    public static PeDbGame getGameFast(int code)
    {
        for (int i = 0; i < allGames.size(); i++)
        {
            PeDbGame game = (PeDbGame) allGames.elementAt(i);
            if (game.ddCode == code)
            {
                return game;
            }
        }

        return null;
    }

    /**
     * 获取游戏信息
     */
    public static Vector<PeDbObject> getGamesFast()
    {
        return allGames;
    }

    /**
     * 获取游戏列表
     */
    public static PeDbGame getGame(String code)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbGame game = null;

        try
        {
            game = (PeDbGame) PeDbGame.queryOneObject(sqlResource, PeDbGame.class, "WHERE ddCode=" + code);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return game;
    }

    /**
     * 获取游戏列表
     */
    public static Vector<PeDbObject> getGames()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbGame.queryObject(sqlResource, PeDbGame.class, "");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncGames();
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbGame()
    {
        super();
    }

    /**
     * 获取 JSON 对象类型
     */
    public JSONObject getMessage()
    {
        //        JSONObject commonConfig = PeDbConfig.getConfigJSON("common");
        String imageUrl = ReadConfig.get("commonUrl");
        JSONObject gameObject = JSONObject.parseObject("{\"code\":" + ddCode + "}");
        gameObject.put("zipUrl", imageUrl + "g" + ddCode + ".zip");
        gameObject.put("name", ddName);
        gameObject.put("singleCoin", ddSingleCoin);
        gameObject.put("multiCoin", ddMultiCoin);
        gameObject.put("maxPlayer", ddMaxPlayer);
        gameObject.put("isPk", ddIsPk == 1);
        gameObject.put("isSingle", ddAllowSingle);
        gameObject.put("rocker", ddRocker);
        gameObject.put("engine", ddEngine);
        gameObject.put("resolution", ddResolution);
        gameObject.put("roleCount", ddRoleCount);
        gameObject.put("autoSelect", ddAutoSelect);
        gameObject.put("canSelect", ddCanSelect);
        gameObject.put("friendUrl", ddFriendUrl);
        gameObject.put("rotate", ddRotate);
        LOG.debug(ddCode + "=" + gameObject.toJSONString());
        return gameObject;
    }

    /**
     * 针对游戏分享配置
     */
    public JSONArray getShareConfig()
    {
        if (shareRes != null)
            return shareRes;
        return loadRes();
    }

    /**
     * 加载资源
     */
    private JSONArray loadRes()
    {
        String startWith = MessageFormat.format(ReadConfig.get("res-host"), "g" + ddCode);
        try
        {
            String text = ddShareRes;
            if (text != null)
            {
                JSONArray array = JSONObject.parseArray(text);
                shareRes = new JSONArray();
                for (int i = 0; i < array.size(); i++)
                {
                    JSONObject share = array.getJSONObject(i);
                    if (share.containsKey("state") && !share.getBoolean("state"))
                        continue;
                    String url = share.getString("url");
                    share.put("url", url.startsWith("http") ? url : startWith.concat("share/" + url));
                    shareRes.add(share);
                }
                return shareRes;
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return new JSONArray();
    }

}
