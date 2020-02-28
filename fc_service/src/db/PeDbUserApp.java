package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "user_app")
public class PeDbUserApp extends PeDbObject
{
    //用户openId
    public String ddOId;
    //用户unionId
    public String ddUid;
    //用户注册appId
    public String ddAppId;
    //用户客户端版本
    public String ddClientVersion = "0";
    //
    // 用户首次登录时间
    //
    public Timestamp ddRegisterTime = new Timestamp(System.currentTimeMillis());

    /**
     * 创建一个用户信息
     */
    public static PeDbUserApp createUserApp(String uid, String openId, String version, String appId)
    {
        PeDbUserApp user = new PeDbUserApp();
        Timestamp date = new Timestamp(System.currentTimeMillis());
        user.ddAppId = appId;
        user.ddUid = uid;
        user.ddOId = openId;
        user.ddClientVersion = version;
        user.ddRegisterTime = date;
        return user;
    }

    /**
     * 更新数据
     */
    public void update(String filter)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            filter = filter.concat("#ddLoginTime");
            updateObject(sqlResource, filter, "WHERE ddOId='" + ddOId + "'",true);
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

}
