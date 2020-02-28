package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_friend_guard")
public class RecordFriendGuard
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "friendId")
    private long friendId;
    @Column(name = "basinId")
    private int basinId;
    @Column(name = "insertTime")
    private java.sql.Timestamp insertTime;


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


    public int getBasinId()
    {
        return basinId;
    }

    public void setBasinId(int basinId)
    {
        this.basinId = basinId;
    }


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

}
