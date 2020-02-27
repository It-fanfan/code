/**
 *
 */
package db;

import com.sun.org.glassfish.gmbal.ManagedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.db.XWHResultSetMapper;
import tool.db.druid.DBQuery;

import javax.persistence.Entity;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author feng
 */
public class PeDbObject implements Serializable
{
    static final Logger LOG = LoggerFactory.getLogger(PeDbObject.class);
    private static final long serialVersionUID = 8303074172047165718L;

    //
    // 数据表名称
    //
    public String tableName = null;

    /**
     * 获取数据库表
     */
    public static String getTableName(Class<?> outClass)
    {
        Entity annotation = outClass.getAnnotation(Entity.class);
        // 获取表名
        String tableName = annotation.name();
        if ("".contentEquals(tableName))
        {
            tableName = outClass.getSimpleName().toLowerCase();
        }
        ManagedData managedData = outClass.getAnnotation(ManagedData.class);
        if (managedData != null)
        {
            tableName = managedData.name() + "." + tableName;
        }
        return tableName;
    }

    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key)
    {
        if (key.length() < 16)
        {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = (int) key.charAt(index);
        int c1 = (int) key.charAt(index + 1);
        int c2 = (int) key.charAt(index + 2);
        int c3 = (int) key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0)
        {
            value = -value;
        }

        return String.valueOf(value);
    }

    /**
     * 根据变量名称获取字符串空间大小
     */
    public static int getStringSizeByName(String name)
    {
        if (name.endsWith("64a") || name.endsWith("64u"))
        {
            return 64;
        }
        if (name.endsWith("128a") || name.endsWith("128u"))
        {
            return 128;
        }
        if (name.endsWith("256a") || name.endsWith("256u"))
        {
            return 256;
        }
        if (name.endsWith("512a") || name.endsWith("512u"))
        {
            return 512;
        }
        if (name.endsWith("1024a") || name.endsWith("1024u"))
        {
            return 1024;
        }
        if (name.endsWith("2048a") || name.endsWith("2048u"))
        {
            return 2048;
        }
        return 0;
    }

    /**
     * 根据变量名称获取字符串是否进行编码
     */
    public static boolean getStringCodecByName(String name)
    {
        int size = getStringSizeByName(name);
        if (0 == size)
        {
            return false;
        }
        if (name.endsWith("u"))
        {
            return true;
        }
        return false;
    }

    /**
     * 获取标题分表参考名称
     */
    public String getTableSplitName()
    {
        return null;
    }

    /**
     * 生成创建一张表的 sql 语句的方法
     */
    private static String generateCreateTableSql(Class<?> classObject, String splitName)
    {
        String tableName = getTableName(classObject);
        StringBuilder sb = new StringBuilder();
        Field[] fields = classObject.getDeclaredFields();
        if (null != splitName)
        {
            tableName += generateTableCodeByKey(splitName);
        }

        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb.append("\n(");

        // id - 主键
        //
        sb.append("id INT(4) PRIMARY KEY NOT NULL AUTO_INCREMENT");

        for (Field field : fields)
        {
            String name = field.getName();
            String type = field.getType().toString();

            if (!name.startsWith("dd"))
            {
                continue;
            }

            switch (type)
            {
                case "int":
                    sb.append(",\n").append(name).append(" INT(4) DEFAULT 0");
                    break;
                case "class java.lang.String":
                    int size = getStringSizeByName(name);
                    if (0 == size)
                    {
                        size = 64;
                    }
                    sb.append(",\n").append(name).append(" VARCHAR(").append(size).append(") DEFAULT '0'");
                    break;
                case "class java.util.Date":
                    sb.append(",\n").append(name).append(" VARCHAR(64) DEFAULT '0'");
                    break;
                case "class java.sql.Timestamp":
                {
                    sb.append(",\n").append(name).append(" timestamp");
                }
                break;
                case "class java.util.HashMap":
                    sb.append(",\n").append(name).append(" VARCHAR(2048) DEFAULT '0'");
                    break;
                case "class java.util.Vector":
                    sb.append(",\n").append(name).append(" VARCHAR(2048) DEFAULT '0'");
                    break;
                case "boolean":
                    sb.append(",\n").append(name).append(" boolean DEFAULT 0");
                    break;
                case "class java.math.BigDecimal":
                    sb.append(",\n").append(name).append(" decimal(10,2) DEFAULT 0");
                    break;
                default:
                    System.out.println("generateCreateTableSql Error: class field type invaild !" + name + ",type=" + type);
                    assert (false);
                    break;
            }
        }

        // end
        //
        sb.append(")");

        return sb.toString();
    }

    /**
     * 生成插入一个条目 sql 语句的方法
     */
    protected String generateInsertObjectSql(String key) throws IllegalArgumentException, IllegalAccessException
    {
        StringBuilder sb = new StringBuilder();
        Class<?> dbClass = getClass();
        Field[] fields = dbClass.getDeclaredFields();
        String tableName = this.tableName;

        if (null != key)
        {
            tableName += generateTableCodeByKey(key);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        sb.append("INSERT INTO ").append(tableName).append("(");

        for (Field field : fields)
        {
            String name = field.getName();
            if (!name.startsWith("dd"))
            {
                continue;
            }
            sb.append(name).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(")VALUES(");

        for (Field field : fields)
        {
            String name = field.getName();
            String type = field.getType().toString();
            if (!name.startsWith("dd"))
            {
                continue;
            }
            switch (type)
            {
                case "int":
                case "long":
                case "double":
                case "float":
                    sb.append(field.get(this)).append(",");
                    break;
                case "class java.lang.String":
                {
                    String value = (String) field.get(this);
                    boolean codec = getStringCodecByName(name);

                    if (codec)
                    {
                        value = CmTool.getStringToHex(value);
                    }

                    sb.append("'").append(value).append("',");
                    break;
                }
                case "class java.util.Date":
                {
                    Date value = (Date) field.get(this);
                    sb.append("'").append(dayFormat.format(value)).append("',");
                    break;
                }
                case "class java.sql.Timestamp":
                {
                    Timestamp date = (Timestamp) field.get(this);
                    if (date == null)
                    {
                        sb.append("NULL,");
                    } else
                    {
                        sb.append("'").append(dateFormat.format(date)).append("',");
                    }
                }
                break;
                case "class java.util.HashMap":
                {
                    HashMap<String, String> value = (HashMap<String, String>) field.get(this);
                    String[] keys = value.keySet().toArray(new String[0]);
                    StringBuilder mapString = new StringBuilder();
                    for (String mapKey : keys)
                    {
                        String mapValue = value.get(mapKey);
                        mapString.append(mapKey).append("=").append(mapValue).append("\n");
                    }
                    String finalMap = CmTool.getStringToHex(mapString.toString());
                    sb.append("'").append(finalMap).append("',");
                    break;
                }
                case "class java.util.Vector":
                    Vector<String> values = (Vector<String>) field.get(this);
                    StringBuilder valueString = new StringBuilder();
                    for (int j = 0; j < values.size(); j++)
                    {
                        valueString.append(values.elementAt(j));
                        if (j < values.size() - 1)
                        {
                            valueString.append("\n");
                        }
                    }
                    String finalValue = CmTool.getStringToHex(valueString.toString());
                    sb.append("'").append(finalValue).append("',");
                    break;
                case "boolean":
                {
                    sb.append(field.get(this)).append(",");
                }
                break;
                case "class java.math.BigDecimal":
                {
                    sb.append(field.get(this)).append(",");
                }
                break;

                default:
                    LOG.debug("generateInsertObjectSql Error: class field type invaild !" + name + ",type=" + type);
                    assert (false);
                    break;
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    /**
     * 生成更新一个条目 sql 语句的方法
     */
    private String generateUpdateObjectSql(String key, String filter, String condition) throws IllegalArgumentException, IllegalAccessException
    {
        StringBuilder sb = new StringBuilder();
        Class<?> dbClass = getClass();
        Field[] fields = dbClass.getDeclaredFields();
        String tableName = this.tableName;
        if (null != key)
        {
            tableName += generateTableCodeByKey(key);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sb.append("UPDATE ").append(tableName).append(" SET ");

        for (Field field : fields)
        {
            String name = field.getName();
            String type = field.getType().toString();

            if (!name.startsWith("dd"))
            {
                continue;
            }

            if (null == filter || filter.contains(name + "#") || filter.endsWith("#" + name) || filter.equals(name))
            {
                switch (type)
                {
                    case "int":
                    case "double":
                    case "long":
                        sb.append(name).append("=").append(field.get(this)).append(",");
                        break;
                    case "class java.lang.String":
                    {
                        String value = (String) field.get(this);
                        boolean codec = getStringCodecByName(name);

                        if (codec)
                        {
                            value = CmTool.getStringToHex(value);
                        }

                        sb.append(name).append("='").append(value).append("',");
                        break;
                    }
                    case "class java.util.Date":
                    {
                        Date value = (Date) field.get(this);
                        sb.append(name).append("='").append(dateFormat.format(value)).append("',");
                        break;
                    }
                    case "class java.sql.Timestamp":
                    {
                        Timestamp date = (Timestamp) field.get(this);
                        if (date != null)
                        {
                            sb.append(name).append("='").append(dateFormat.format(date)).append("',");
                        }
                    }
                    break;
                    case "class java.util.HashMap":
                    {
                        HashMap<String, String> value = (HashMap<String, String>) field.get(this);
                        String[] keys = value.keySet().toArray(new String[0]);
                        StringBuilder mapString = new StringBuilder();
                        for (String mapKey : keys)
                        {
                            String mapValue = value.get(mapKey);
                            mapString.append(mapKey).append("=").append(mapValue).append("\n");
                        }
                        String finalValue = CmTool.getStringToHex(mapString.toString());
                        sb.append(name).append("='").append(finalValue).append("',");
                        break;
                    }
                    case "class java.util.Vector":
                    {
                        Vector<String> values = (Vector<String>) field.get(this);
                        StringBuilder valueString = new StringBuilder();
                        for (int j = 0; j < values.size(); j++)
                        {
                            valueString.append(values.elementAt(j));
                            if (j < values.size() - 1)
                            {
                                valueString.append("\n");
                            }
                        }
                        String finalValue = CmTool.getStringToHex(valueString.toString());
                        sb.append(name).append("='").append(finalValue).append("',");
                        break;
                    }
                    case "boolean":
                    {
                        sb.append(name).append("=").append(field.get(this)).append(",");
                    }
                    break;
                    case "class java.math.BigDecimal":
                    {
                        sb.append(name).append("=").append(field.get(this)).append(",");
                    }
                    break;
                    default:
                        System.out.println("generateUpdateObjectSql Error: class field type invaild !" + name + ",type=" + type);
                        assert (false);
                        break;
                }
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ").append(condition);

        return sb.toString();
    }

    /**
     * 生成删除一个条目 sql 语句的方法
     */
    private String generateDeleteObjectSql(String key, String condition) throws IllegalArgumentException
    {
        StringBuilder sb = new StringBuilder();
        String tableName = this.tableName;

        if (null != key)
        {
            tableName += generateTableCodeByKey(key);
        }

        sb.append("DELETE FROM ").append(tableName);
        sb.append(" ").append(condition);
        return sb.toString();
    }

    /**
     * 创建表格的方法
     */
    public static void createTableWithSplitName(XWHResultSetMapper mapper, Class<?> classObject, String splitName)
    {
        String sqlCreateTable = generateCreateTableSql(classObject, splitName);
        mapper.execSQLCMDInfo(sqlCreateTable);
    }

    /**
     * 创建表格的方法
     */
    static void createTable(XWHResultSetMapper mapper, Class<?> classObject)
    {
        createTableWithSplitName(mapper, classObject, null);
    }

    /**
     * 插入对象的方法
     */
    public void insertObject(XWHResultSetMapper mapper) throws Exception
    {
        insertObject(mapper, false);
    }

    /**
     * 插入对象的方法
     */
    public void insertObject(XWHResultSetMapper mapper, boolean isQueue) throws Exception
    {
        String splitName = getTableSplitName();
        String sqlInsertObject = generateInsertObjectSql(splitName);
        if (isQueue)
            mapper.addQueue(sqlInsertObject);
        else
            mapper.execSQLCMDInfo(sqlInsertObject);
    }

    /**
     * 查询对象的方法
     */
    private static Vector<PeDbObject> queryObjectWithSplitName(CmDbSqlResource sqlResource, Class<?> classObject, String splitName, String condition) throws Exception
    {
        String tableName = getTableName(classObject);
        if (null != splitName)
        {
            tableName += generateTableCodeByKey(splitName);
        }
        Vector<PeDbObject> results = new Vector<>();
        String sql = "SELECT * FROM " + tableName + " " + condition;
        try (DBQuery query = new DBQuery(sqlResource))
        {
            ResultSet rs = query.executeQuery(sql);
            while (rs.next())
            {
                PeDbObject object = (PeDbObject) classObject.newInstance();
                Field[] fields = classObject.getDeclaredFields();
                for (Field field : fields)
                {
                    String name = field.getName();
                    String type = field.getType().toString();

                    if (!name.startsWith("dd"))
                    {
                        continue;
                    }
                    switch (type)
                    {
                        case "int":
                        {
                            int value = rs.getInt(name);
                            field.set(object, value);
                            break;
                        }
                        case "double":
                        {
                            double value = rs.getDouble(name);
                            field.set(object, value);
                            break;
                        }
                        case "long":
                        {
                            long value = rs.getLong(name);
                            field.set(object, value);
                            break;
                        }
                        case "class java.lang.String":
                        {
                            String value = rs.getString(name);
                            boolean codec = getStringCodecByName(name);

                            if (codec)
                            {
                                value = CmTool.getStringFromHex(value);
                            }
                            field.set(object, value);
                            break;
                        }
                        case "class java.util.Date":
                        {
                            Date date = rs.getDate(name);
                            field.set(object, date);
                            break;
                        }
                        case "class java.sql.Timestamp":
                        {
                            Timestamp date = rs.getTimestamp(name);
                            if (date != null)
                            {
                                field.set(object, date);
                            }
                        }
                        break;
                        case "class java.util.HashMap":
                        {
                            String value = rs.getString(name);
                            value = CmTool.getStringFromHex(value);
                            HashMap<String, String> map = CmTool.parseConfigString(value);
                            field.set(object, map);
                            break;
                        }
                        case "class java.util.Vector":
                        {
                            String value = rs.getString(name);
                            value = CmTool.getStringFromHex(value);
                            Vector<String> list = CmTool.parseListString(value);
                            field.set(object, list);
                            break;
                        }
                        case "boolean":
                        {
                            boolean value = rs.getBoolean(name);
                            field.set(object, value);
                        }
                        break;
                        case "class java.math.BigDecimal":
                        {
                            BigDecimal value = rs.getBigDecimal(name);
                            field.set(object, value);
                        }
                        break;
                        default:
                            System.out.println("queryObjectWithSplitName Error: class field type invaild !" + name + ",type=" + type);
                            assert (false);
                            break;
                    }
                }
                results.addElement(object);
            }
        }
        return results;
    }

    /**
     * 查询对象的方法
     */
    static Vector<PeDbObject> queryObject(CmDbSqlResource sqlResource, Class<?> classObject, String condition) throws Exception
    {
        return queryObjectWithSplitName(sqlResource, classObject, null, condition);
    }

    /**
     * 查询对象的方法
     */
    public static PeDbObject queryOneObjectWithSplitName(CmDbSqlResource sqlResource, Class<?> classObject, String splitName, String condition) throws Exception
    {
        Vector<PeDbObject> objects = queryObjectWithSplitName(sqlResource, classObject, splitName, condition);
        if (objects.size() <= 0)
        {
            return null;
        }
        return objects.firstElement();
    }

    /**
     * 查询对象的方法
     */
    static PeDbObject queryOneObject(CmDbSqlResource sqlResource, Class<?> classObject, String condition) throws Exception
    {
        Vector<PeDbObject> objects = queryObjectWithSplitName(sqlResource, classObject, null, condition);
        if (objects.size() <= 0)
        {
            return null;
        }
        return objects.firstElement();
    }

    /**
     * 更新对象的方法
     */
    void updateObject(XWHResultSetMapper mapper, String filter, String condition, boolean isQueue) throws Exception
    {
        String splitName = getTableSplitName();
        String sqlUpdateObject = generateUpdateObjectSql(splitName, filter, condition);
        if (isQueue)
            mapper.addQueue(sqlUpdateObject);
        else
            mapper.execSQLCMDInfo(sqlUpdateObject);
    }

    /**
     * 删除对象的方法
     */
    void deleteObject(XWHResultSetMapper mapper, String condition)
    {
        String splitName = getTableSplitName();
        String sqlDeleteObject = generateDeleteObjectSql(splitName, condition);
        mapper.execSQLCMDInfo(sqlDeleteObject);
    }

    /**
     * 构造一个数据表对象的方法
     */
    public PeDbObject()
    {
        this.tableName = getTableName(this.getClass());
    }
}
