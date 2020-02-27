package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_order_introduce")
public class ConfigOrderIntroduce
{

    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "msg")
    private String msg;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

}
