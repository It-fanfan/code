package com.utils.font;

import java.util.Map;

/**
 * 字体本地化
 */
public class FontLocalization
{
    //名称
    public String name;
    //版本
    public String version;
    //对象
    public Map<String, String> dependencies;

    public FontLocalization()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Map<String, String> getDependencies()
    {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies)
    {
        this.dependencies = dependencies;
    }
}