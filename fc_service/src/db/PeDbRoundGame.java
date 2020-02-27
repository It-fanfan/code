package db;

import com.sun.org.glassfish.gmbal.ManagedData;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Entity(name = "round_game")
@ManagedData(name = "persie_deamon")
public class PeDbRoundGame extends PeDbObject
{
    //群编号
    public int ddCode;
    //游戲名稱
    public String ddName;
    //游戲狀態
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
    private static Map<Integer, PeDbRoundGame> allMatches = new HashMap<>();

    /**
     * 插入一条数据
     */
    public void insert()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            this.insertObject(sqlResource);
            allMatches.put(ddCode, this);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取最优赛场配置
     *
     * @param ddCode 赛场编号
     * @return 赛场信息
     */
    public static PeDbRoundGame getGameFast(int ddCode)
    {
        return allMatches.get(ddCode);
    }

    /**
     * 获取商品信息
     */
    public static Map<Integer, PeDbRoundGame> getMatchesFast()
    {
        return allMatches;
    }

    /**
     * 获取游戏列表
     */
    private static Map<Integer, PeDbRoundGame> getGroupMatch()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<Integer, PeDbRoundGame> data = new HashMap<>();

        try
        {
            Vector<PeDbObject> objects = PeDbRoundGame.queryObject(sqlResource, PeDbRoundGame.class, "");
            for (PeDbObject obj : objects)
            {
                PeDbRoundGame group = (PeDbRoundGame) obj;
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
        return "PeDbRoundGame{" + "ddCode=" + ddCode + ", ddState=" + ddState + ", ddGame=" + ddGame + ", ddRound='" + ddRound + '\'' + ", ddStart=" + ddStart + ", ddEnd=" + ddEnd + '}';
    }
}
