package com.code.dao.entity.message;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "apply_friend")
public class ApplyFriend
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @PrimaryKey
    @Column(name = "applyId")
    private long applyId;
    @Column(name = "send")
    private boolean send;
    @Column(name = "nickName")
    private String nickName;
    @Column(name = "applyName")
    private String applyName;
    @Column(name = "receive")
    private int receive;
    @Column(name = "applyTime")
    private Timestamp applyTime;

    public ApplyFriend()
    {
    }

    public ApplyFriend(long userId, long applyId, String nickName, String applyName)
    {
        this.userId = userId;
        this.applyId = applyId;
        this.nickName = nickName;
        this.applyName = applyName;
        this.applyTime = new Timestamp(System.currentTimeMillis());
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(long applyId)
    {
        this.applyId = applyId;
    }


    public boolean getSend()
    {
        return send;
    }

    public void setSend(boolean send)
    {
        this.send = send;
    }


    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }


    public String getApplyName()
    {
        return applyName;
    }

    public void setApplyName(String applyName)
    {
        this.applyName = applyName;
    }


    public int getReceive()
    {
        return receive;
    }

    public void setReceive(int receive)
    {
        this.receive = receive;
    }


    public Timestamp getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime)
    {
        this.applyTime = applyTime;
    }

}
