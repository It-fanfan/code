package com.fish.dao.primary.model;

import java.math.BigDecimal;
import java.util.Date;

public class BuyPay
{


    private String buyDate;

    private String buyAppId;

    private String buyProductName;

    private BigDecimal buyCost;

    private Integer buyClickNumber;

    private BigDecimal buyClickPrice;

    private Date insertTime;


    public String getBuyDate()
    {
        return buyDate;
    }

    public void setBuyDate(String buyDate)
    {
        this.buyDate = buyDate == null ? null : buyDate.trim();
    }

    public String getBuyAppId()
    {
        return buyAppId;
    }

    public void setBuyAppId(String buyAppId)
    {
        this.buyAppId = buyAppId == null ? null : buyAppId.trim();
    }

    public String getBuyProductName()
    {
        return buyProductName;
    }

    public void setBuyProductName(String buyProductName)
    {
        this.buyProductName = buyProductName == null ? null : buyProductName.trim();
    }

    public BigDecimal getBuyCost()
    {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost)
    {
        this.buyCost = buyCost;
    }

    public Integer getBuyClickNumber()
    {
        return buyClickNumber;
    }

    public void setBuyClickNumber(Integer buyClickNumber)
    {
        this.buyClickNumber = buyClickNumber;
    }

    public BigDecimal getBuyClickPrice()
    {
        return buyClickPrice;
    }

    public void setBuyClickPrice(BigDecimal buyClickPrice)
    {
        this.buyClickPrice = buyClickPrice;
    }

    public Date getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(Date insertTime)
    {
        this.insertTime = insertTime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", buyDate=").append(buyDate);
        sb.append(", buyAppId=").append(buyAppId);
        sb.append(", buyProductName=").append(buyProductName);
        sb.append(", buyCost=").append(buyCost);
        sb.append(", buyClickNumber=").append(buyClickNumber);
        sb.append(", buyClickPrice=").append(buyClickPrice);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}