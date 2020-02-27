package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_friend_angling")
public class RecordFriendAngling
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "friend")
    private long friend;
    @Column(name = "loseCount")
    private int loseCount;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;
    @Column(name = "json")
    private String json;


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


    public long getFriend()
    {
        return friend;
    }

    public void setFriend(long friend)
    {
        this.friend = friend;
    }


    public int getLoseCount()
    {
        return loseCount;
    }

    public void setLoseCount(int loseCount)
    {
        this.loseCount = loseCount;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }


    public String getJson()
    {
        return json;
    }

    public void setJson(String json)
    {
        this.json = json;
    }

}
