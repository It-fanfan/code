package com.utils.db;

import com.annotation.KeyAuto;
import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.annotation.TableSub;
import com.utils.XwhTool;
import com.utils.db.druid.DBPoolConnection;
import com.utils.log4j.Log4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过entity注释将resultSet数据解析
 *
 * @author Host-0222
 */
public abstract class XWHResultSetMapper implements Runnable
{
    // 日志打印
    private static final Logger LOG = LoggerFactory.getLogger(XWHResultSetMapper.class);
    private static ScheduledExecutorService scheduler = null;
    private final Vector<String> queues = new Vector<>();

    /**
     * 添加缓存数据参数
     */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> cacheMap = new ConcurrentHashMap<>();

    // 不定时刷新缓存
    private ConcurrentHashMap<String, Object> untimeCache = new ConcurrentHashMap<>();

    // 数据库表群信息
    private Set<String> showTables = null;

    private DBPoolConnection dbn = null;

    /**
     * 初始化线程池
     */
    public static void init(ScheduledExecutorService element)
    {
        scheduler = element;
    }

    /**
     * 获取链接池对象
     */
    private DBPoolConnection getPool()
    {
        if (dbn != null)
        {
            return dbn;
        }
        return dbn = DBPoolConnection.getInstance();
    }

    /**
     * 进行添加队列
     */
    public boolean addQueue(String sql)
    {
        LOG.debug("Q:" + sql);
        return queues.add(sql);
    }

    /**
     * 进行刷新缓存
     */
    public void flushCache(String className)
    {
        for (Class<?> outclass : getCacheClass())
        {
            if (outclass.getSimpleName().equalsIgnoreCase(className))
            {
                setCacheByClassName(outclass);
                return;
            }
        }
    }

    /**
     * 清理非定时缓存
     */
    public boolean flushUntimeCache()
    {
        untimeCache.clear();
        return true;
    }

    /**
     * 清理非定时缓存
     */
    public boolean clearUntimeCache(String key)
    {
        untimeCache.remove(key);
        return true;
    }

    /**
     * 缓存机制1:永不刷新修改缓存
     */
    protected void initCache()
    {
        cacheMap.clear();
        for (Class<?> outclass : getCacheClass())
        {
            setCacheByClassName(outclass);
        }
        //进行启动线程,100毫秒一次请求
        scheduler.scheduleWithFixedDelay(this, 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 通过类进行设置缓存数据
     */
    private void setCacheByClassName(Class<?> outclass)
    {
        String classkey = outclass.getName().toLowerCase();
        cacheMap.remove(classkey);
        Field[] fields = outclass.getDeclaredFields();
        // 获取主键属性结构
        Vector<Field> primaryKey = new Vector<>();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(PrimaryKey.class))
            {
                primaryKey.add(field);
            }
        }
        if (primaryKey.isEmpty())
        {
            return;
        }
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        String tablename = getTableName(outclass);
        String sql = "select * from " + tablename;
        for (Object value : findBySQL(sql, outclass))
        {
            // 获取主键信息,进行配置缓存信息
            try
            {
                Vector<String[]> keys = new Vector<>();
                for (Field field : primaryKey)
                {
                    if (!field.isAccessible())
                    {
                        field.setAccessible(true);
                    }
                    String columsValue = field.get(value).toString();
                    keys.add(new String[]{field.getName(), columsValue});
                }
                String[][] temp = new String[keys.size()][];
                temp = keys.toArray(temp);
                String key = getMapKey(temp);
                LOG.debug(key);
                map.put(key, value);
            } catch (Exception e)
            {
                e.printStackTrace();
                return;
            }
        }
        if (!map.isEmpty())
        {
            cacheMap.put(classkey, map);
        }
    }

    /**
     * 获取map中key值
     *
     * @param keys 列表
     * @return key
     */
    private String getMapKey(String[]... keys)
    {
        StringBuilder keyBuilder = new StringBuilder();
        for (String[] key : keys)
        {
            if (keyBuilder.length() > 0)
            {
                keyBuilder.append(".");
            }
            keyBuilder.append(key[0].toLowerCase()).append("_").append(key[1].trim());
        }
        return keyBuilder.toString();
    }

    /**
     * 进行获取需要添加缓存的class信息
     */
    public Vector<Class<?>> getCacheClass()
    {
        return new Vector<>();
    }

    /**
     * 进行获取缓存数据
     */
    public Object getCacheKey(Class<?> outclass, String[]... keys)
    {
        synchronized (cacheMap)
        {
            String classkey = outclass.getName().toLowerCase();
            ConcurrentHashMap<String, Object> map = cacheMap.get(classkey);
            if (map == null)
            {
                return null;
            }
            if (keys == null || keys.length == 0)
            {
                return map;
            }
            try
            {
                return map.get(getMapKey(keys));
            } catch (Exception e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
            return null;
        }
    }

    /**
     * 获取不定时缓存
     */
    public synchronized Object getUntimeCache(String key)
    {
        return untimeCache.get(key);
    }

    /**
     * 设置不定时缓存
     */
    public synchronized <T> void putUntimeCache(String key, T t)
    {
        untimeCache.put(key, t);
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
    private Connection openConnection()
    {
        return getPool().getConnection(alias());
    }

    /**
     * 进行查找表群信息
     */
    private void showTable()
    {
        showTables = new HashSet<>();
        String SQL = "show tables";
        Connection conn = null;
        try
        {
            conn = openConnection();
            ResultSet rs = setResultSet(conn, SQL);
            while (rs.next())
            {
                showTables.add(rs.getString(1));
            }
            rs.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            closeConnection(conn);
        }
    }

    /**
     * 数据库进行新增
     */
    public <T> int saveOrUpdate(T t, boolean delay) throws Exception
    {
        Vector<T> vector = new Vector<>();
        vector.add(t);
        return saveOrUpdate(vector, delay);
    }

    /**
     * 检测表列信息
     */
    public boolean containsTable(String key)
    {
        if (showTables == null)
        {
            showTable();
        }
        return !showTables.contains(key);
    }

    /**
     * 获取数据库表
     */
    public String getTableName(Class<?> outClass)
    {
        Entity annotation = outClass.getAnnotation(Entity.class);
        // 获取表名
        String tableName = annotation.name();
        if ("".contentEquals(tableName))
        {
            tableName = outClass.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    /**
     * 生成实体对象SQL
     *
     * @param t   对象信息
     * @param <T> 对象类
     * @return SQL
     */
    public <T> String getEntitySQL(T t)
    {
        SQLEntity sqlEntity = createSQLEntity(t.getClass());
        setEntityData(sqlEntity, t);
        return createBatchSQL(getTableName(t.getClass()), sqlEntity);
    }

    /**
     * 数据库进行新增
     */
    public <T> int saveOrUpdate(Vector<T> ts, boolean delay) throws Exception
    {
        Map<String, SQLEntity> collection = new LinkedHashMap<>();
        String templte = null;
        // 对应的实体类保存，修改
        for (T t : ts)
        {
            Class<?> outClass = t.getClass();
            // 获取对应实体中需要保存数据
            String tableName = templte = getTableName(outClass);
            if (outClass.isAnnotationPresent(TableSub.class))
            {
                Method tablesub = outClass.getDeclaredMethod("getTableSub");
                String subTab = (String) tablesub.invoke(t);
                tableName += "_" + subTab;
            }
            SQLEntity sqlEntity = collection.get(tableName);
            if (sqlEntity == null)
            {
                sqlEntity = createSQLEntity(outClass);
                collection.put(tableName, sqlEntity);
            }
            setEntityData(sqlEntity, t);
        }
        // 进行创建对应表單
        createTemplteTable(collection.keySet(), templte);
        for (Entry<String, SQLEntity> entry : collection.entrySet())
        {
            String name = entry.getKey();
            String SQL = createBatchSQL(name, entry.getValue());
            if (!delay)
            {
                execSQLCMDInfo(SQL);
            } else
            {
                addQueue(SQL);
            }
        }
        return 1;
    }

    /**
     * 创建对应表格
     */
    private void createTemplteTable(Set<String> keySet, String tableName)
    {
        for (String name : keySet)
        {
            createTemplteTable(name, tableName);
        }
    }

    /**
     * 創建表
     */
    private void createTemplteTable(String name, String tableName)
    {
        if (showTables == null)
        {
            showTable();
        }
        if (!showTables.contains(name))
        {
            createTable(name, tableName);
        }
    }

    /**
     * 进行创建SQL
     */
    private String createBatchSQL(String table, SQLEntity entity)
    {
        StringBuilder buffer = new StringBuilder();
        StringBuilder end = new StringBuilder();
        for (String update : entity.updateNodes)
        {
            if (end.length() != 0)
            {
                end.append(",");
            }
            end.append(update).append("=values(").append(update).append(")");
        }
        buffer.append("insert into ");
        buffer.append(table);
        buffer.append("(");

        int len = buffer.length();
        for (String field : entity.columnNodes)
        {
            if (len != buffer.length())
            {
                buffer.append(",");
            }
            buffer.append(field);
        }
        buffer.append(") values ");
        // 设置列表名称
        Iterator<Map<String, Object>> it = entity.queue.iterator();
        len = buffer.length();
        while (it.hasNext())
        {
            if (len != buffer.length())
            {
                buffer.append(",");
            }
            buffer.append("(");
            len = buffer.length();
            Map<String, Object> obj = it.next();
            for (String field : entity.columnNodes)
            {
                if (len != buffer.length())
                {
                    buffer.append(",");
                }
                Object value = obj.get(field);
                if (value instanceof Boolean || value instanceof Long || value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Byte || value == null)
                {
                    buffer.append(value);
                } else
                {
                    buffer.append("\"");
                    String data = Objects.toString(value, "");
                    buffer.append(XwhTool.convertJsonToStr(data));
                    buffer.append("\"");
                }
            }
            buffer.append(")");
        }
        buffer.append("  ON DUPLICATE KEY UPDATE ").append(end);
        return buffer.toString();
    }

    /**
     * 初始定义
     *
     * @param outClass 实体对象
     * @return
     */
    private SQLEntity createSQLEntity(Class<?> outClass)
    {
        SQLEntity entity = new SQLEntity();
        Field[] fields = outClass.getDeclaredFields();
        for (Field field : fields)
        {
            if (!field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(KeyAuto.class))
            {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String name = column.name();
            entity.columnNodes.add(name);
            if (!field.isAnnotationPresent(PrimaryKey.class) && !field.isAnnotationPresent(ReadOnly.class))
                entity.updateNodes.add(name);
            entity.fields.add(field);

        }
        return entity;
    }

    /**
     * 获取对象数据
     *
     * @param entity 数据结构
     * @param <T>    实体
     */
    private <T> void setEntityData(SQLEntity entity, T t)
    {
        try
        {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field field : entity.fields)
            {
                Column column = field.getAnnotation(Column.class);
                String name = column.name();
                if (!field.isAccessible())
                {
                    field.setAccessible(true);
                }
                Object columValue = field.get(t);
                if (columValue == null)
                {
                    continue;
                }
                map.put(name, columValue);
            }
            entity.queue.add(map);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }


    /**
     * 进行创建表
     */
    public void createTable(String name, String modelName)
    {
        String SQL = "CREATE TABLE IF NOT EXISTS " + name + " LIKE " + modelName;
        execSQLCMDInfo(SQL);
    }

    /**
     * 执行操作结果
     */
    public int execSQLCMDInfo(String SQL)
    {
        DBPoolConnection dbn = getPool();
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
     * 通过SQL进行查询数据
     */
    public <T> Vector<T> findBySQL(String sql, Class<T> a)
    {
        Connection conn = null;
        try
        {
            conn = openConnection();
            LOG.debug("select SQL:" + sql);
            ResultSet resultSet = setResultSet(conn, sql);
            return mapResultSetToObject(resultSet, a);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            closeConnection(conn);
        }
        return null;
    }

    /**
     * 通过主键进行查找数据
     */
    public <T> T findById(Class<T> a, String tableName, String[]... ids)
    {
        if (showTables == null)
        {
            showTable();
        }
        if (!showTables.contains(tableName))
            return null;
        StringBuilder builder = createAppendIds(ids);
        if (builder.length() > 0)
            builder.insert(0, " where ");
        String sql = "select * from " + tableName + builder.toString();
        LOG.debug(sql);
        Vector<T> list = findBySQL(sql, a);
        if (list != null && list.size() > 0)
        {
            return list.firstElement();
        }
        return null;
    }

    /**
     * 通过主键进行查找数据
     */
    public <T> T findById(Class<T> a, String[]... ids)
    {
        String tableName = getTableName(a);
        return findById(a, tableName, ids);
    }

    /**
     * 进行创建id拼接数据头
     *
     * @param ids 数据集合
     * @return 返回创建成功的数据节点
     */
    private StringBuilder createAppendIds(String[][] ids)
    {
        StringBuilder builder = new StringBuilder();
        if (ids != null)
            for (String[] data : ids)
            {
                if (builder.length() > 0)
                    builder.append(" and ");
                builder.append(data[0]).append("='").append(data[1]).append("'");
            }
        return builder;
    }

    /**
     * 进行创建一组实体类
     *
     * @param rs          查找结果集
     * @param outputClass 需要构建的实体类
     */
    private <T> Vector<T> mapResultSetToObject(ResultSet rs, Class<T> outputClass)
    {
        Vector<T> outputList = new Vector<>();
        try
        {
            if (rs == null)
            {
                return outputList;
            }
            if (!outputClass.isAnnotationPresent(Entity.class))
            {
                return outputList;
            }
            Field[] fields = outputClass.getDeclaredFields();
            while (rs.next())
            {
                // 进行构建一个实体类
                T bean = outputClass.newInstance();
                // 进行赋值
                for (Field field : fields)
                {
                    if (!field.isAnnotationPresent(Column.class))
                    {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    String name = column.name();
                    Object columnValue = rs.getObject(name);
                    if (columnValue != null)
                    {
                        if (!field.isAccessible())
                        {
                            field.setAccessible(true);
                        }
                        if (field.getType() == Blob.class)
                        {
                            field.set(bean, rs.getBlob(name));
                        } else
                        {
                            field.set(bean, ConvertUtils.convert(columnValue, field.getType()));
                        }
                    }
                }
                outputList.add(bean);
            }
            rs.close();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return outputList;
    }

    /**
     * 获取指定类的缓存集合
     * @param outclass 查询实体类
     * @param <T>
     * @return
     */
    public <T> Vector<T> getCacheListByClass(Class<T> outclass)
    {
        Vector<T> vector = new Vector<>();
        String classkey = outclass.getName().toLowerCase();
        // key: 主键_name value:对象
        ConcurrentHashMap<String, Object> map = cacheMap.get(classkey);
        if (map != null)
        {
            for (Entry<String, Object> entry : map.entrySet())
            {
                Object obj = entry.getValue();
                if (outclass.isInstance(obj))
                {
                    vector.add(outclass.cast(obj));
                }
            }
            return vector;
        }
        return vector;
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

    class SQLEntity
    {
        //数据项
        Queue<Map<String, Object>> queue = new LinkedBlockingQueue<>();
        //表项
        Vector<String> columnNodes = new Vector<>();
        //更新项
        Vector<String> updateNodes = new Vector<>();
        //列
        Vector<Field> fields = new Vector<>();
    }
}