package com.code.dao.entity.work;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;
import com.code.protocols.basic.BasePro;
import com.code.service.work.WorkConfig;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import org.apache.commons.jexl3.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Entity(name = "work_sign")
public class WorkSign implements WorkConfig
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "dayFlag")
    private int dayFlag;
    @ReadOnly
    @Column(name = "state")
    private boolean state;
    @Column(name = "reward")
    private String reward;
    @Column(name = "tip")
    private String tip;
    @ReadOnly
    @Column(name = "startDate")
    private java.sql.Date startDate;
    @ReadOnly
    @Column(name = "endDate")
    private java.sql.Date endDate;
    @Column(name = "limitFlag")
    private int limitFlag;

    private Vector<SignReward> signRewards = null;
    private Map<String, JexlExpression> expressions = null;

    /**
     * 签到奖励配置
     *
     * @return 最终奖励
     */
    public Vector<BasePro.RewardInfo> getSignReward(int dayTotal)
    {
        if (signRewards == null)
        {
            signRewards = XwhTool.parseJSONByFastJSON(reward, new TypeToken<Vector<SignReward>>()
            {
            }.getType());
            expressions = new HashMap<>();
        }
        for (SignReward reward : signRewards)
        {
            if (reward.format != null)
            {
                JexlExpression expression = expressions.get(reward.format);
                if (expression == null)
                {
                    JexlEngine engine = new JexlBuilder().create();
                    expression = engine.createExpression(reward.format);
                }
                JexlContext jexlContext = new MapContext();
                jexlContext.set("day", dayTotal);
                Object result = expression.evaluate(jexlContext);
                if (result != null)
                {
                    reward.total = (int) result;
                }
            }
        }
        return new Vector<>(signRewards);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getDayFlag()
    {
        return dayFlag;
    }

    public void setDayFlag(int dayFlag)
    {
        this.dayFlag = dayFlag;
    }

    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    public String getTip()
    {
        return tip;
    }

    public void setTip(String tip)
    {
        this.tip = tip;
    }

    public java.sql.Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate)
    {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate)
    {
        this.endDate = endDate;
    }

    public int getLimitFlag()
    {
        return limitFlag;
    }

    public void setLimitFlag(int limitFlag)
    {
        this.limitFlag = limitFlag;
    }

    public static class SignReward extends BasePro.RewardInfo
    {

        public String format;

        public SignReward()
        {
            super();
        }

        public SignReward(String type, int id, int total)
        {
            super(type, id, total);
        }
    }
}
