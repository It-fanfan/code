package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;
import com.code.dao.db.FishInfoDb;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Systemstatusinfo
{
    private static Logger LOG = LoggerFactory.getLogger(Systemstatusinfo.class);
    @PrimaryKey
    @Column(name = "system_name")
    private String system_name;

    @Column(name = "system_value")
    private String system_value;

    @Column(name = "system_description")
    private String system_description;

    /**
     * 常規數據获取
     *
     * @param name key
     * @return {data}
     */
    public static Systemstatusinfo getSystemstatusinfo(String name)
    {
        try
        {
            return (Systemstatusinfo) FishInfoDb.instance().getCacheKey(Systemstatusinfo.class, new String[]{"system_name", name});
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取lONG字符数据
     *
     * @param name         key
     * @param defaultValue 默认值
     * @return 结果
     */
    public static long getLong(String name, String defaultValue)
    {
        String value = Objects.toString(getText(name), defaultValue);
        return Long.valueOf(value);
    }

    /**
     * 进行判断是否状态开关
     *
     * @param name key
     * @return bool
     */
    public static boolean getBoolean(String name)
    {
        return getBoolean(name, "false");
    }

    /**
     * 获取配置数值
     *
     * @param name         key
     * @param defaultValue 默认值
     * @return value
     */
    public static boolean getBoolean(String name, String defaultValue)
    {
        String value = Objects.toString(getText(name), defaultValue);
        return Boolean.valueOf(value);
    }

    /**
     * 获取配置数值
     *
     * @param name key
     * @return value
     */
    public static int getInt(String name)
    {
        return getInt(name, "0");
    }

    /**
     * 获取配置数值
     *
     * @param name         key
     * @param defaultValue 默认值
     * @return value
     */
    public static int getInt(String name, String defaultValue)
    {
        String value = Objects.toString(getText(name), defaultValue);
        return Integer.valueOf(value);
    }

    /**
     * 获取配置数值
     *
     * @param name         key
     * @param defaultValue 默认值
     * @return value
     */
    public static double getDouble(String name, String defaultValue)
    {
        String value = Objects.toString(getText(name), defaultValue);
        return Double.valueOf(value);
    }

    /**
     * 获取配置数值
     *
     * @param name key
     * @return value
     */
    public static double getDouble(String name)
    {
        return getDouble(name, "0");
    }

    /**
     * 获取文本
     *
     * @param name key
     * @return value
     */
    public static String getText(String name)
    {
        try
        {
            Systemstatusinfo info = getSystemstatusinfo(name);
            if (info != null)
            {
                return info.getSystem_value().trim();
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    public String getSystem_value()
    {
        if (this.system_value == null)
        {
            return null;
        }
        return this.system_value.trim();
    }

    public void setSystem_value(String system_value)
    {
        this.system_value = system_value;
    }

    public String getSystem_description()
    {
        return this.system_description;
    }

    public void setSystem_description(String system_description)
    {
        this.system_description = system_description;
    }

    public String getSystem_name()
    {
        return system_name;
    }

    public void setSystem_name(String system_name)
    {
        this.system_name = system_name;
    }
}