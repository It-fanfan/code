package com.code.service.rate;

import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;

import java.util.Vector;

public class OrderHit implements Hit
{
    private Vector<Rate> data = new Vector<>();

    public OrderHit()
    {
        super();
    }

    @Override
    public Vector<Rate> rates()
    {
        return data;
    }

    @Override
    public String getName()
    {
        return "order-rate";
    }

    @Override
    public Vector<Rate> resetRates()
    {
        String json = Systemstatusinfo.getText(getName());
        Vector<OrderRate> rates = new Vector<>();
        if (json != null)
        {
            rates = XwhTool.parseJSONByFastJSON(json, new TypeToken<Vector<OrderRate>>()
            {
            }.getType());
        }
        if (rates == null || rates.isEmpty())
        {
            rates = new Vector<>();
            rates.add(new OrderRate(20, 1, 11, 1, 1000));
            rates.add(new OrderRate(40, 11, 31, 0.8, 1000));
            rates.add(new OrderRate(40, 31, 50, 0.6, 1000));
        }
        return new Vector<>(rates);
    }

    public static class OrderRate extends Rate
    {
        public int min;
        public int max;
        //单价
        public double single;
        //分母
        public int denominator;

        public OrderRate()
        {
            super();
        }

        private OrderRate(int rate, int min, int max, double single, int denominator)
        {
            this.rate = rate;
            this.min = min;
            this.max = max;
            this.single = single;
            this.denominator = denominator;
        }
    }
}
