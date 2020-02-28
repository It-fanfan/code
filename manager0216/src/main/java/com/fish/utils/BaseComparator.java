package com.fish.utils;


import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

public class BaseComparator<T> implements Comparator<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseComparator.class);
    private Method method;
    private String type;

    public BaseComparator(Method method)
    {
        this.method = method;
        type = method.getReturnType().getSimpleName().toLowerCase();
    }

    @Override
    public int compare(T a, T b)
    {
        try
        {
            switch (type)
            {
                case "string":
                    return ((String) method.invoke(a)).compareTo((String) method.invoke(b));
                case "int":
                case "integer":
                    return ((Integer) method.invoke(a)).compareTo((Integer) method.invoke(b));
                case "date":
                    return ((Date) method.invoke(a)).compareTo((Date) method.invoke(b));
                case "timestamp":
                    return ((Timestamp) method.invoke(a)).compareTo((Timestamp) method.invoke(b));
                case "bigdecimal":
                    return ((BigDecimal) method.invoke(a)).compareTo((BigDecimal) method.invoke(b));

                case "long":
                    return ((Long) method.invoke(a)).compareTo((Long) method.invoke(b));
                case "double":
                    return ((Double) method.invoke(a)).compareTo((Double) method.invoke(b));
                case "float":
                    return ((Float) method.invoke(a)).compareTo((Float) method.invoke(b));
                default:
                    return method.invoke(a).toString().compareTo(method.invoke(b).toString());

            }

        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return 1;
    }
}
