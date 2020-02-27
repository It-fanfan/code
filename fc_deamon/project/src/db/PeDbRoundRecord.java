package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Vector;

@Entity(name = "round_record")
public class PeDbRoundRecord extends PeDbObject
{
    //赛场编号
    public int ddCode;
    //赛场群标签
    public boolean ddGroup;
    //赛场轮次
    public int ddIndex;
    //赛场名称
    public String ddName;
    //游戏编号
    public int ddGame;
    //赛区编号
    public String ddRound;
    //赛场开启时间
    public Timestamp ddStart;
    //赛场截至时间
    public Timestamp ddEnd;
    //赛场结算时间
    public Timestamp ddSubmit;
    //赛场更新时间
    public Timestamp ddTime;
    //结果数
    public int ddResult;

    //当前赛场优先级
    public int priority;


    /**
     * 进行插入数据
     */
    public void insert()
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
     * 更新数据库字段
     *
     * @param filter 更新字段
     */
    public void update(String filter)
    {
        ddTime = new Timestamp(System.currentTimeMillis());
        filter += "#ddTime";
        String condition = MessageFormat.format("where ddCode={0} and ddGroup={1} and ddIndex={2}", ddCode, ddGroup, ddIndex);
        try
        {
            this.updateObject(CmDbSqlResource.instance(), filter, condition);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }


    /**
     * 获取游戏列表
     */
    private static Vector<PeDbRoundRecord> getRoundRecord()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbRoundRecord> data = new Vector<>();
        try
        {
            Vector<PeDbObject> objects = PeDbRoundRecord.queryObject(sqlResource, PeDbRoundRecord.class, "");
            for (PeDbObject obj : objects)
            {
                PeDbRoundRecord group = (PeDbRoundRecord) obj;
                data.add(group);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return data;
    }

    public static void init()
    {

    }
}
