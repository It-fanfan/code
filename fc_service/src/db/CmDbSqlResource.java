/**
 *
 */
package db;

import tool.db.XWHResultSetMapper;
import tool.db.druid.DBPoolConnection;
import work.CmServletListener;

import java.util.concurrent.TimeUnit;

public class CmDbSqlResource extends XWHResultSetMapper
{
    @Override
    public String alias()
    {
        return DBPoolConnection.DB_ALIAS.DB_MASTER_ALIAS;
    }

    private static CmDbSqlResource instance;

    static
    {
        instance = new CmDbSqlResource();
        //进行1s检测，延迟同步刷新到数据库内
        CmServletListener.scheduler.scheduleWithFixedDelay(instance, 1, 1, TimeUnit.SECONDS);
    }

    private CmDbSqlResource()
    {
    }

    public static CmDbSqlResource instance()
    {
        return instance;
    }
}

