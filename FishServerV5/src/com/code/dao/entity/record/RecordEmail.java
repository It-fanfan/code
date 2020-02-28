package com.code.dao.entity.record;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_email")
public class RecordEmail
{
    @ReadOnly
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "friendId")
    private long friendId;
    @Column(name = "udp")
    private String udp;
    @Column(name = "msg")
    private String msg;
    @ReadOnly
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;
    //插入事件
    private long insert;

    public RecordEmail()
    {

    }

    public RecordEmail(long userId, long friendId, String msg, String udp)
    {
        this.udp = udp;
        this.userId = userId;
        this.friendId = friendId;
        this.msg = msg;
        this.insert = System.currentTimeMillis();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public long getFriendId()
    {
        return friendId;
    }

    public void setFriendId(long friendId)
    {
        this.friendId = friendId;
    }


    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

    public String getUdp()
    {
        return udp;
    }

    public void setUdp(String udp)
    {
        this.udp = udp;
    }

    public long getInsert()
    {
        return insert;
    }

    public void setInsert(long insert)
    {
        this.insert = insert;
    }
}
