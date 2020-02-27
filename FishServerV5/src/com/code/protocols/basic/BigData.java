package com.code.protocols.basic;

import com.annotation.ReadOnly;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Vector;

/**
 * 大数据模板信息
 */
public class BigData
{
    private static final Logger LOG = LoggerFactory.getLogger(BigData.class);
    //标题
    public String[] fields;
    //数据
    public Vector<Object[]> data;

    /**
     * 构造数据节点
     *
     * @param data   数据
     * @param fields 节点
     * @param <T>    节点类型
     * @return 完成节点
     */
    public static <T> BigData getBigData(Vector<T> data, Field[] fields)
    {
        BigData bigData = new BigData();
        bigData.fields = new String[fields.length];
        //构建标题
        for (int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];

            bigData.fields[i] = field.getName().toLowerCase();
        }
        //构建数据
        bigData.data = new Vector<>();
        try
        {
            if (data != null)
                for (T instance : data)
                {
                    Object[] value = new Object[fields.length];
                    for (int i = 0; i < fields.length; i++)
                    {
                        Field field = fields[i];
                        field.setAccessible(true);
                        Object _val = field.get(instance);
                        if (field.getType() == Long.class && _val != null)
                        {
                            _val = _val.toString();
                        }
                        value[i] = _val;
                    }
                    bigData.data.add(value);
                }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return bigData;
    }

    /**
     * 构建数据节点
     *
     * @param data 数据信息
     * @param <T>  节点
     * @return 数据节点
     */
    public static <T> BigData getBigData(Vector<T> data, Class<T> outClass)
    {
        Field[] fields = outClass.getDeclaredFields();
        Vector<Field> values = new Vector<>();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(ReadOnly.class))
            {
                continue;
            }
            values.add(field);
        }
        Field[] _temp = new Field[values.size()];
        for (int i = 0; i < values.size(); i++)
        {
            _temp[i] = values.get(i);
        }
        return getBigData(data, _temp);
    }
}
