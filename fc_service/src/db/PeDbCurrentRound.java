package db;

import com.sun.org.glassfish.gmbal.ManagedData;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "current_round")
@ManagedData(name = "persie_deamon")
public class PeDbCurrentRound extends PeDbObject
{
    //赛场编号
    public int ddCode;
    //当前轮次开始时间
    public Timestamp ddStart;
    //当前轮次结算时间
    public Timestamp ddEnd;
    //当前赛场第几轮
    public int ddIndex;
    //时间
    public Timestamp ddTime;

    /**
     * 初始化数据对象
     */
    public static void init()
    {
    }


}
