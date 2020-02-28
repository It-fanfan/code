package db;

import tool.db.XWHResultSetMapper;
import tool.db.druid.DBPoolConnection;

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
    }

    private CmDbSqlResource()
    {
    }

    public static CmDbSqlResource instance()
    {
        return instance;
    }
}
