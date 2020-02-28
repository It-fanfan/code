package db;

import com.sun.org.glassfish.gmbal.ManagedData;
import tool.CmTool;
import tool.Log4j;

import javax.persistence.Entity;
import java.util.Vector;

/**
 * @author feng
 */
@Entity(name = "gameset")
@ManagedData(name = "persie_deamon")
public class PeDbGameSet extends PeDbObject
{

    private static final long serialVersionUID = -4091234494722767821L;

    //
    // 全部游戏合集的列表
    //
    private static Vector<PeDbObject> allGameSets = new Vector<>();

    //
    // 游戏集代号信息（四位数字，唯一）
    //
    public int ddCode = 0;

    //模式类型
    public int ddState = 0;

    //
    // 游戏集名称信息
    //
    public String ddName = "0";

    //
    // 游戏集排序过滤
    //
    public String ddArrange512a = "0";

    //
    // 游戏集内容信息
    //
    public String ddContent512a = "0";

    public String ddDesc = "0";


    /**
     * 获取游戏合集消息
     */
    public Vector<PeDbGame> getGameSetGames()
    {
        String[] gameStrings = CmTool.cut(ddContent512a, "#");
        String[] gameArranges = CmTool.cut(ddArrange512a, "#");
        int[] gameCodes = new int[gameStrings.length];
        int index = 0;
        for (String gameArrange : gameArranges)
        {
            String arrange = gameArrange;
            arrange = CmTool.trimExt(arrange);

            for (int j = 0; j < gameStrings.length; j++)
            {
                String content = gameStrings[j];
                if (null == content)
                {
                    continue;
                }
                content = CmTool.trimExt(content);
                if (arrange.equals(content))
                {
                    gameStrings[j] = null;
                    gameCodes[index++] = CmTool.parseInt(content, 0);
                    break;
                }
            }
        }
        for (String content : gameStrings)
        {
            if (null == content)
            {
                continue;
            }
            gameCodes[index++] = CmTool.parseInt(content, 0);
        }
        Vector<PeDbGame> gamesInfo = new Vector<>();
        for (int gameCode : gameCodes)
        {
            PeDbGame game = PeDbGame.getGameFast(gameCode);
            if (null == game)
            {
                continue;
            }
            gamesInfo.addElement(game);
        }
        return gamesInfo;
    }

    /**
     * 同步游戏合集列表
     */
    public static void syncGameSets()
    {
        allGameSets = getGameSets();
    }

    /**
     * 获取游戏信息
     */
    public static PeDbGameSet getGameSetFast(int code)
    {
        for (int i = 0; i < allGameSets.size(); i++)
        {
            PeDbGameSet gameSet = (PeDbGameSet) allGameSets.elementAt(i);
            if (gameSet.ddCode == code)
            {
                return gameSet;
            }
        }

        return null;
    }

    /**
     * 获取游戏集列表
     */
    public static Vector<PeDbObject> getGameSets()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<PeDbObject>();

        try
        {
            objects = PeDbGameSet.queryObject(sqlResource, PeDbGameSet.class, "");
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
        syncGameSets();
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbGameSet()
    {
        super();
    }

}
