package tool.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;
import tool.db.druid.DBPoolConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 通过entity注释将resultSet数据解析
 *
 * @author Host-0222
 */
public abstract class XWHResultSetMapper implements Runnable
{
    // 日志打印
    private static final Logger LOG = LoggerFactory.getLogger(XWHResultSetMapper.class);


    private DBPoolConnection dbn = null;
    //记录队列
    private final Vector<String> queues = new Vector<>();

    /**
     * 进行添加队列
     */
    public boolean addQueue(String sql)
    {
        LOG.debug("Q:" + sql);
        return queues.add(sql);
    }

    /**
     * 获取链接池对象
     */
    public DBPoolConnection getPool()
    {
        if (dbn != null)
        {
            return dbn;
        }
        return dbn = DBPoolConnection.getInstance();
    }

    /**
     * 获取数据库别名
     */
    public abstract String alias();

    /**
     * 进行获取查询条件
     */
    private ResultSet setResultSet(Connection conn, String sql)
    {
        return getPool().executeQuery(conn, sql);
    }

    /**
     * 进行关闭SQL
     */
    private void closeConnection(Connection conn)
    {
        getPool().closeConnection(conn);
    }

    /**
     * 进行开启SQL
     */
    public Connection openConnection()
    {
        return getPool().getConnection(alias());
    }

    /**
     * 执行操作结果
     */
    public int execSQLCMDInfo(String SQL)
    {
        DBPoolConnection dbn = getPool();
        LOG.debug(SQL);
        return dbn.execSQLCMDInfo(openConnection(), SQL);
    }

    /**
     * 進行批量更新數據
     */
    public int[] execBatchSQL(BatchSQL batchSQL)
    {
        DBPoolConnection dbn = getPool();
        return dbn.execBatchSQL(openConnection(), batchSQL);
    }

    /**
     * 批量更新數據
     */
    public int[] execBatchSQL(List<String> sqls)
    {
        DBPoolConnection dbn = getPool();
        return dbn.execBatchSQL(openConnection(), sqls);
    }

    /**
     * 插入一条sql 同时返回自增id
     */
    public long insertGenerateKey(String SQL)
    {
        Connection conn = openConnection();
        long id = 0;
        try
        {
            LOG.debug(SQL);
            Statement statemenet = conn.createStatement();
            statemenet.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statemenet.getGeneratedKeys();
            if (generatedKeys.next())
            {
                id = generatedKeys.getLong(1);
            }
            generatedKeys.close();
            statemenet.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (conn != null)
            {
                closeConnection(conn);
            }
        }
        return id;
    }

    @Override
    public void run()
    {
        try
        {
            if (queues.isEmpty())
            {
                return;
            }
            List<String> batchSQLs;
            synchronized (queues)
            {
                batchSQLs = new ArrayList<>(queues);
                queues.clear();
            }
            if (!batchSQLs.isEmpty())
            {
                execBatchSQL(batchSQLs);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }
}