package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Entity(name = "round_match")
public class PeDbRoundMatch extends PeDbObject
{
    //编号
    public int ddCode;
    //賽名稱
    public String ddName;
    //赛场指定appId
    public String ddAppId;
    //状态
    public boolean ddState;
    //游戏编号
    public int ddGame;
    //赛场类型
    public String ddRound;
    //开启时间
    public Timestamp ddStart;
    //截至时间
    public Timestamp ddEnd;

    //
    // 全部比賽列表
    //
    private static Map<Integer, PeDbRoundMatch> allMatches = new HashMap<>();

     /**
     * 获取游戏信息
     */
    public static PeDbRoundMatch getMatchFast(int code)
    {
        //游戏编号
        PeDbRoundMatch group = allMatches.get(code);
        if (group == null)
            return null;
        return group;
    }

    /**
     * 获取商品信息
     */
    public static Map<Integer, PeDbRoundMatch> getMatchesFast()
    {
        return allMatches;
    }

    /**
     * 获取游戏列表
     */
    private static Map<Integer, PeDbRoundMatch> getGroupMatch()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<Integer, PeDbRoundMatch> data = new HashMap<>();

        try
        {
            Vector<PeDbObject> objects = PeDbRoundMatch.queryObject(sqlResource, PeDbRoundMatch.class, "");
            for (PeDbObject obj : objects)
            {
                PeDbRoundMatch group = (PeDbRoundMatch) obj;
                data.put(group.ddCode, group);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return data;
    }


    /**
     * 同步游戏列表
     */
    private static void syncGoods()
    {
        allMatches = getGroupMatch();
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncGoods();
    }

    @Override
    public String toString()
    {
        return "PeDbRoundGroup{" + "ddCode=" + ddCode + ", ddState=" + ddState + ", ddGame=" + ddGame + ", ddRound='" + ddRound + '\'' + ", ddStart=" + ddStart + ", ddEnd=" + ddEnd + '}';
    }
}
