package com.code.dao.entity.achievement;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_achievement")
public class ConfigAchievement
{

    public int process;
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    private boolean status;
    @Column(name = "species")
    private String species;
    @Column(name = "level")
    private int level;
    @Column(name = "hidden")
    private boolean hidden;
    @Column(name = "condition")
    private int condition;
    @Column(name = "processCondition")
    private int processCondition;
    @Column(name = "reward")
    private String reward;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public String getSpecies()
    {
        return species;
    }

    public void setSpecies(String species)
    {
        this.species = species;
    }


    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public int getCondition()
    {
        return condition;
    }

    public void setCondition(int condition)
    {
        this.condition = condition;
    }


    public int getProcessCondition()
    {
        return processCondition;
    }

    public void setProcessCondition(int processCondition)
    {
        this.processCondition = processCondition;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

}
