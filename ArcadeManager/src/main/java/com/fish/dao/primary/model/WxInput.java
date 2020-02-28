package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class WxInput {
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date insertTime;

    private Integer newAddNop;

    private Integer activeNop;

    private Integer visitCount;

    private Double visitAvg;

    private Integer stayAvg;

    private Integer shareNop;

    private Integer shareCount;

    private Integer shareNewAdd;

    private Double keepDay1;

    private Double keepDay3;

    private Double keepDay7;

    private Integer bannerExposure;

    private Integer bannerClick;

    private BigDecimal bannerIncome;

    private Integer videoExposure;

    private Integer videoClick;

    private BigDecimal videoIncome;

    private BigDecimal videoTotal;

    private BigDecimal virtualPay;

    private BigDecimal payTotal;

    private Integer alltotal;

    private Integer promotionCosts;

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getNewAddNop() {
        return newAddNop;
    }

    public void setNewAddNop(Integer newAddNop) {
        this.newAddNop = newAddNop;
    }

    public Integer getActiveNop() {
        return activeNop;
    }

    public void setActiveNop(Integer activeNop) {
        this.activeNop = activeNop;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Double getVisitAvg() {
        return visitAvg;
    }

    public void setVisitAvg(Double visitAvg) {
        this.visitAvg = visitAvg;
    }

    public Integer getStayAvg() {
        return stayAvg;
    }

    public void setStayAvg(Integer stayAvg) {
        this.stayAvg = stayAvg;
    }

    public Integer getShareNop() {
        return shareNop;
    }

    public void setShareNop(Integer shareNop) {
        this.shareNop = shareNop;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getShareNewAdd() {
        return shareNewAdd;
    }

    public void setShareNewAdd(Integer shareNewAdd) {
        this.shareNewAdd = shareNewAdd;
    }

    public Double getKeepDay1() {
        return keepDay1;
    }

    public void setKeepDay1(Double keepDay1) {
        this.keepDay1 = keepDay1;
    }

    public Double getKeepDay3() {
        return keepDay3;
    }

    public void setKeepDay3(Double keepDay3) {
        this.keepDay3 = keepDay3;
    }

    public Double getKeepDay7() {
        return keepDay7;
    }

    public void setKeepDay7(Double keepDay7) {
        this.keepDay7 = keepDay7;
    }

    public Integer getBannerExposure() {
        return bannerExposure;
    }

    public void setBannerExposure(Integer bannerExposure) {
        this.bannerExposure = bannerExposure;
    }

    public Integer getBannerClick() {
        return bannerClick;
    }

    public void setBannerClick(Integer bannerClick) {
        this.bannerClick = bannerClick;
    }

    public BigDecimal getBannerIncome() {
        return bannerIncome;
    }

    public void setBannerIncome(BigDecimal bannerIncome) {
        this.bannerIncome = bannerIncome;
    }

    public Integer getVideoExposure() {
        return videoExposure;
    }

    public void setVideoExposure(Integer videoExposure) {
        this.videoExposure = videoExposure;
    }

    public Integer getVideoClick() {
        return videoClick;
    }

    public void setVideoClick(Integer videoClick) {
        this.videoClick = videoClick;
    }

    public BigDecimal getVideoIncome() {
        return videoIncome;
    }

    public void setVideoIncome(BigDecimal videoIncome) {
        this.videoIncome = videoIncome;
    }

    public BigDecimal getVideoTotal() {
        return videoTotal;
    }

    public void setVideoTotal(BigDecimal videoTotal) {
        this.videoTotal = videoTotal;
    }

    public BigDecimal getVirtualPay() {
        return virtualPay;
    }

    public void setVirtualPay(BigDecimal virtualPay) {
        this.virtualPay = virtualPay;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

    public Integer getAlltotal() {
        return alltotal;
    }

    public void setAlltotal(Integer alltotal) {
        this.alltotal = alltotal;
    }

    public Integer getPromotionCosts() {
        return promotionCosts;
    }

    public void setPromotionCosts(Integer promotionCosts) {
        this.promotionCosts = promotionCosts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", insertTime=").append(insertTime);
        sb.append(", newAddNop=").append(newAddNop);
        sb.append(", activeNop=").append(activeNop);
        sb.append(", visitCount=").append(visitCount);
        sb.append(", visitAvg=").append(visitAvg);
        sb.append(", stayAvg=").append(stayAvg);
        sb.append(", shareNop=").append(shareNop);
        sb.append(", shareCount=").append(shareCount);
        sb.append(", shareNewAdd=").append(shareNewAdd);
        sb.append(", keepDay1=").append(keepDay1);
        sb.append(", keepDay3=").append(keepDay3);
        sb.append(", keepDay7=").append(keepDay7);
        sb.append(", bannerExposure=").append(bannerExposure);
        sb.append(", bannerClick=").append(bannerClick);
        sb.append(", bannerIncome=").append(bannerIncome);
        sb.append(", videoExposure=").append(videoExposure);
        sb.append(", videoClick=").append(videoClick);
        sb.append(", videoIncome=").append(videoIncome);
        sb.append(", videoTotal=").append(videoTotal);
        sb.append(", virtualPay=").append(virtualPay);
        sb.append(", payTotal=").append(payTotal);
        sb.append(", alltotal=").append(alltotal);
        sb.append(", promotionCosts=").append(promotionCosts);
        sb.append("]");
        return sb.toString();
    }
}