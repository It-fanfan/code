package com.code.dao.entity.fish.userinfo;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity(name = "user_data")
public class UserData
{
    @PrimaryKey
    @Column(name = "userId")
    private long userId;
    @Column(name = "shell")
    private Long shell;
    @Column(name = "pearl")
    private Long pearl;
    @Column(name = "shellTotal")
    private Long shellTotal;
    @Column(name = "pearlTotal")
    private Long pearlTotal;
    @Column(name = "modiftyTime")
    private Timestamp modiftyTime;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public Long getShell()
    {
        return shell;
    }

    public void setShell(Long shell)
    {
        this.shell = shell;
    }

    public Long getPearl()
    {
        return pearl;
    }

    public void setPearl(Long pearl)
    {
        this.pearl = pearl;
    }

    public Long getShellTotal()
    {
        return shellTotal;
    }

    public void setShellTotal(Long shellTotal)
    {
        this.shellTotal = shellTotal;
    }

    public Long getPearlTotal()
    {
        return pearlTotal;
    }

    public void setPearlTotal(Long pearlTotal)
    {
        this.pearlTotal = pearlTotal;
    }

    public Timestamp getModiftyTime()
    {
        return modiftyTime;
    }

    public void setModiftyTime(Timestamp modiftyTime)
    {
        this.modiftyTime = modiftyTime;
    }
}
