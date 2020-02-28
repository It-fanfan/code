package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_invite")
public class ConfigInvite
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "inviteNum")
    private int inviteNum;
    @Column(name = "awardNum")
    private int awardNum;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public int getInviteNum()
    {
        return inviteNum;
    }

    public void setInviteNum(int inviteNum)
    {
        this.inviteNum = inviteNum;
    }


    public int getAwardNum()
    {
        return awardNum;
    }

    public void setAwardNum(int awardNum)
    {
        this.awardNum = awardNum;
    }

}
