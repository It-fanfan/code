package com.utils.font;

import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进行本地化
 */
public class Localization
{

    //日志打印
    private static final Logger LOGGER = LoggerFactory.getLogger(Localization.class);
    //对象缓存数据
    private static Map<String, Localization> instanceCache = new ConcurrentHashMap<>();
    private FontLocalization fontLocalization = null;

    private Localization(String localization)
    {
        try
        {
            if (localization == null)
                return;
            InputStream in = Localization.class.getResourceAsStream("/font/" + localization + ".json");
            if (in != null)
            {
                String context = XwhTool.readInputStream(in);
                fontLocalization = XwhTool.parseConfigString(context, FontLocalization.class);
                in.close();
            }
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 进行获取本地化记录信息
     *
     * @param localization 本地化参数
     * @return 参数值
     */
    public static Localization getInstance(String localization)
    {
        Localization instance = instanceCache.get(localization);
        if (instance == null)
        {
            instance = new Localization(localization);
            instanceCache.put(localization, instance);
        }
        return instance;
    }

    public static void main(String[] args)
    {
        Test test = new Test();
        test.data = new Vector<>();
        test.data.add("大佬，求带");
        test.data.add("你是谁");
        test.data.add("在吗");
        test.data.add("这是搞啥子呢?");
        test.srfff = "在吗";
        test.t = 22343;
        test.dffg.put("123", "你是谁");
        System.out.println(XwhTool.getJSONByFastJSON(Localization.getInstance("en").convert(test)));
    }

    /**
     * 单词替换
     *
     * @param context 内容
     * @return 替换
     */
    public String convert(String context)
    {
        if (context == null)
            return null;
        if (fontLocalization.dependencies.containsKey(context))
        {
            return fontLocalization.dependencies.get(context);
        }
        return context;
    }

    /**
     * 转换对象
     *
     * @param list 对象
     */
    public void convert(List list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            Object value = list.get(i);
            if (value instanceof String)
            {
                String replace = convert(value.toString());
                if (replace != null && !replace.equals(value.toString()))
                    list.set(i, replace);
            }
        }
    }

    /**
     * 转换对象
     *
     * @param map 对象
     */
    public void convert(Map map)
    {
        try
        {
            map.forEach((key, value) ->
            {
                if (value instanceof String)
                {
                    String replace = convert(value.toString());
                    if (replace != null && !replace.equals(value.toString()))
                        map.replace(key, replace);
                }
            });
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 转换对象
     *
     * @param entity 对象值
     * @return 转换后结果
     */
    public <R> Object convert(R entity)
    {
        //本地化不存在或者字库匹配不存在
        if (fontLocalization == null || fontLocalization.dependencies == null)
            return entity;
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                if (field.getType().isPrimitive())
                    continue;
                field.setAccessible(true);
                Object v = field.get(entity);
                if (v == null)
                    continue;
                if (v instanceof String)
                {
                    String replace = convert(v.toString());
                    if (replace != null && !replace.equals(v.toString()))
                        field.set(entity, convert(v.toString()));
                } else if (v instanceof Map)
                {
                    Map map = (Map) v;
                    convert(map);
                } else if (v instanceof List)
                {
                    List list = (List) v;
                    convert(list);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
        }
        return entity;
    }

    public static class Test
    {
        public Vector<String> data;
        public String srfff;
        public int f;
        public Integer t;
        public Map<String, String> dffg = new HashMap<>();
        public LinkedHashMap<String, String> dfgg = new LinkedHashMap<>();
        public List<String> gg = new ArrayList<>();
        public Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        public Date date = new Date();
    }
}
