package com.code.service.rate;

/**
 * 概率接口
 */
public abstract class Rate
{
    public int rate;

    public Rate()
    {
    }

    /**
     * 获取概率值
     *
     * @return
     */
    public int getRate()
    {
        return rate;
    }
}
