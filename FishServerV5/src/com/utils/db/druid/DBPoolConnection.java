package com.utils.db.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.utils.XwhTool;
import com.utils.db.BatchSQL;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 要实现单例模式，保证全局只有一个数据库连接池
 */
public class DBPoolConnection
{
    private static final String ALIBABA = "alibaba.properties";
    private static Logger LOG = LoggerFactory.getLogger(DBPoolConnection.class);
    private static DBPoolConnection dbPoolConnection = null;
    private static Map<String, DruidDataSource> druidDataSource = null;

    static
    {
        Map<String, Properties> properties = loadPropertiesFile();
        try
        {
            druidDataSource = new ConcurrentHashMap<>();
            for (Map.Entry<String, Properties> config : properties.entrySet())
            {
                druidDataSource.put(config.getKey(), (DruidDataSource) DruidDataSourceFactory.createDataSource(config.getValue()));
            }
        } catch (Exception e)
        {
            LOG.error("获取配置失败");
        }
    }

    /**
     * 数据库连接池单例
     */
    public static synchronized DBPoolConnection getInstance()
    {
        if (null == dbPoolConnection)
        {
            dbPoolConnection = new DBPoolConnection();
        }
        return dbPoolConnection;
    }

    /**
     * @return Properties对象
     */
    private static Map<String, Properties> loadPropertiesFile()
    {
        Map<String, Properties> configs = new HashMap<>();

        InputStream in = null;
        try
        {
            in = DBPoolConnection.class.getResourceAsStream("/" + ALIBABA);
            Properties p = new Properties();
            p.load(in);
            in.close();
            //进行配置配置文件信息
            String effect = p.getProperty("effectdb");
            if (effect != null)
            {
                String[] split = effect.split(",");
                for (String db : split)
                {
                    int index = db.length() + 1;
                    Properties element = new Properties();
                    p.stringPropertyNames().stream().filter(key -> key.startsWith(db + ".")).forEachOrdered(key -> element.setProperty(key.substring(index), p.getProperty(key)));
                    configs.put(db, element);

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (null != in)
                {
                    in.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return configs;
    }

    /**
     * 返回druid数据库连接
     */
    public DruidPooledConnection getConnection(String alias)
    {
        try
        {
            DruidDataSource source = druidDataSource.get(alias);
            if (source != null)
                return source.getConnection();
        } catch (SQLException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return null;
    }

    /**
     * 关闭一个连接的方法
     *
     * @param conn 连接句柄信息
     */
    public void closeConnection(Connection conn)
    {
        try
        {
            if (conn != null)
            {
                conn.close();
            }
        } catch (SQLException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 提交一次查询的方法
     *
     * @param conn      连接信息
     * @param sqlString 查询语句信息
     * @return 得到的结果集
     */
    public ResultSet executeQuery(Connection conn, String sqlString)
    {
        try
        {
            if (conn == null)
            {
                return null;
            }
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (stmt == null)
            {
                return null;
            }
            return stmt.executeQuery(sqlString);
        } catch (SQLException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 执行SQL指令
     *
     * @param conn connection
     * @param SQL  sql
     * @return execute
     */
    public int execSQLCMDInfo(Connection conn, String SQL)
    {
        int result = 0;
        try
        {
            LOG.debug(SQL);
            Statement state = conn.createStatement();
            result = state.executeUpdate(SQL);
            state.close();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            if (conn != null)
            {
                closeConnection(conn);
            }
        }

        return result;
    }

    /**
     * 進行批量更新數據
     *
     * @param conn     connection
     * @param batchSQL batchSQL entity
     * @return execute data
     */
    public int[] execBatchSQL(Connection conn, BatchSQL batchSQL)
    {
        int[] executeBatch = null;
        try
        {
            PreparedStatement rest = conn.prepareStatement(batchSQL.getSQL(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            conn.setAutoCommit(false);
            for (int i = 0; i < batchSQL.getLength(); i++)
            {
                batchSQL.addBatch(rest, i);
                rest.addBatch();
            }
            executeBatch = rest.executeBatch();
            conn.commit();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            if (conn != null)
            {
                closeConnection(conn);
            }
        }
        return executeBatch;
    }

    /**
     * 执行 批处理 SQL指令
     *
     * @param conn connection
     * @param sqls sql list
     * @return execute data
     */
    public int[] execBatchSQL(Connection conn, List<String> sqls)
    {
        int[] batch = null;
        try
        {
            Statement state = conn.createStatement();
            if (state == null)
            {
                return null;
            }
            conn.setAutoCommit(false);
            for (String sql : sqls)
            {
                state.addBatch(sql);
            }
            LOG.debug(XwhTool.getJSONByFastJSON(sqls));
            batch = state.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e)
        {
            try
            {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1)
            {
                LOG.error(Log4j.getExceptionInfo(e1));
            }
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            if (conn != null)
            {
                closeConnection(conn);
            }
        }
        return batch;
    }

    public interface DB_ALIAS
    {
        // 鱼类数据库
        String DB_FISH_ALIAS = "fish";
    }

}
