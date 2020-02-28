package db;

import com.sun.org.glassfish.gmbal.ManagedData;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Vector;

@Entity(name = "round_receive")
@ManagedData(name = "persie_deamon")
public class PeDbRoundReceive extends PeDbObject
{
    //用户编号
    public String ddUid;
    //赛区编号
    public int ddMCode;
    //群标签
    public boolean ddGroup;
    //轮次编号
    public int ddMIndex;
    //游戏编号
    public int ddGCode;
    //开始时间
    public Timestamp ddMStart;
    //截至时间
    public Timestamp ddMEnd;
    //得分
    public long ddMark;
    //排名
    public int ddRanking;
    //奖励类型
    public String ddType;
    //奖励数量
    public int ddTotal;
    //插入时间
    public Timestamp ddTime;

    /**
     * 保存数据库
     */
    public void insertOrUpdate()
    {
        try
        {
            this.insertObject(CmDbSqlResource.instance());
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }


    /**
     * 获取游戏列表
     */
    public static Vector<PeDbObject> getDbRecord(String ddUid)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbRoundReceive.queryObject(sqlResource, PeDbRoundReceive.class, "WHERE ddUid='" + ddUid + "' order by id desc");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    public static void init()
    {
    }
}
