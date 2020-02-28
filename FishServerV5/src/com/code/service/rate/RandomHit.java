package com.code.service.rate;

import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;

import java.lang.reflect.Type;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机命中
 */
public class RandomHit implements Hit
{

    private Vector<Rate> rates = new Vector<>();
    private String name;

    @Override
    public Vector<Rate> rates()
    {
        return rates;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Vector<Rate> resetRates()
    {
        String json = Systemstatusinfo.getText(getName());
        Vector<RandomRate> rates = null;
        if (json != null)
        {
            Type type = new TypeToken<Vector<RandomRate>>()
            {
            }.getType();
            rates = XwhTool.parseJSONByFastJSON(json, type);
        }
        if (rates == null)
        {
            rates = new Vector<>();
        }
        return new Vector<>(rates);
    }

    public static class RandomRate extends Rate
    {
        public int min;
        public int max;

        public RandomRate()
        {
            super();
        }

        public int value()
        {
            if (max == min)
                return max;
            return ThreadLocalRandom.current().nextInt(min, max);
        }
    }
}
