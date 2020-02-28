package com.fish.dao.primary.model;

import org.springframework.stereotype.Component;

@Component
public class CheckBoxData
{
    private String title;

    private Integer value;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }
}
