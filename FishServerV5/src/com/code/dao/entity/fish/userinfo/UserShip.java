package com.code.dao.entity.fish.userinfo;

import com.annotation.Comments;
import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_ship")
public class UserShip
{
    @PrimaryKey
    @Column(name = "userId")
    @Comments(name = "玩家编号")
    private long userId;
    @PrimaryKey
    @Column(name = "friendId")
    @Comments(name = "好友编号")
    private long friendId;
    @Column(name = "insertTime")
    @Comments(name = "确认时间")
    private java.sql.Timestamp insertTime;


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


    public java.sql.Timestamp getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime)
    {
        this.insertTime = insertTime;
    }

}
