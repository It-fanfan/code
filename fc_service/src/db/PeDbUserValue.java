package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author feng
 */
@Entity(name = "user_value")
public class PeDbUserValue extends PeDbObject implements Serializable
{
    public String ddUid = "0";

    //
    // 用户累计奖金
    //
    public long ddAwardMoney = 0;

    //
    // 用户累计奖币
    //
    public long ddAwardCoin = 0;

    //
    // 用户金币数量
    //
    public long ddCoinCount = 0;
    //用户奖金数量
    public long ddMoney = 0;

    //
    // 用户累计充值金额
    //
    public long ddTotalPayMoney = 0;

    //
    //用户管理员标签
    //
    public boolean ddGameMaster = false;

    //
    // 用户最新登录时间
    //
    public Date ddLoginTime = new Date(0);

    public PeDbUserValue(String ddUid, int ddAwardMoney, int ddAwardCoin, int ddCoinCount, int ddMoney, int ddTotalPayMoney, Date ddLoginTime)
    {
        this.ddUid = ddUid;
        this.ddAwardMoney = ddAwardMoney;
        this.ddAwardCoin = ddAwardCoin;
        this.ddCoinCount = ddCoinCount;
        this.ddMoney = ddMoney;
        this.ddTotalPayMoney = ddTotalPayMoney;
        this.ddLoginTime = ddLoginTime;
        this.ddGameMaster = false;
    }

    /**
     * 同步数据库
     */
    public void update(String filter)
    {
        try
        {
            ddLoginTime = new Date();
            filter = filter.concat("#ddLoginTime");
            this.updateObject(CmDbSqlResource.instance(), filter, "where ddUid='" + ddUid + "'", true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

    }

    /**
     * 初始化用户信息
     */
    public static void init()
    {
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbUserValue()
    {
        super();
    }
}
