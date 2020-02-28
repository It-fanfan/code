package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "all_cost")
public class PeDbAllCost extends PeDbObject
{
    //用户编号
    public String ddUid;
    //产品信息
    public String ddAppId;
    //数值类型
    public String ddType;
    //历史记录
    public long ddHistory;
    //当前记录
    public long ddCurrent;
    //消耗值
    public int ddValue;
    //消耗类型
    public String ddCostType;
    //类型扩展
    public String ddCostExtra;
    //消耗时间
    public Timestamp ddTime;

    public PeDbAllCost()
    {
    }

    public PeDbAllCost(String ddUid, String ddAppId, String ddType, long ddHistory, long ddCurrent, int ddValue, String ddCostType, Timestamp ddTime)
    {
        this.ddUid = ddUid;
        this.ddAppId = ddAppId;
        this.ddType = ddType;
        this.ddHistory = ddHistory;
        this.ddCurrent = ddCurrent;
        this.ddValue = ddValue;
        this.ddCostType = ddCostType;
        this.ddTime = ddTime;
    }

    public void setDdCostExtra(String ddCostExtra)
    {
        this.ddCostExtra = ddCostExtra;
    }

    public void insert()
    {
        try
        {
            this.insertObject(CmDbSqlResource.instance(), true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    public static void init()
    {
    }
}
