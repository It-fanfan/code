package db;

import com.alibaba.fastjson.JSONObject;
import tool.Log4j;

import javax.persistence.Entity;
import java.util.Vector;

/**
 * @author feng
 */
@Entity(name = "games")
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
    //允许单机
    //
    public boolean ddAllowSingle;

    //
    // 游戏是否为PK
    //
    public int ddIsPk = 0;

    //
    // 游戏是否有效
    //
    public int ddAvailable = 0;

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

    //旋转屏幕
    public int ddRotate = 0;

    /**
     * 同步游戏列表
     */
    public static void syncGames()
    {
        allGames = getGames();
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
     * 获取 JSON 对象类型
     */
    public JSONObject getMessage()
    {
        JSONObject gameObject = JSONObject.parseObject("{\"code\":" + ddCode + "}");
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
        gameObject.put("rotate", ddRotate);
        return gameObject;
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

}
