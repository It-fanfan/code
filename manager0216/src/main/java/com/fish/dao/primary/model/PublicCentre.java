package com.fish.dao.primary.model;

public class PublicCentre {
    private Integer id;

    private Integer locationId;

    private Integer showId;

    private Integer recommendType;

    private String recommendName;

    private Byte skipType;

    private Integer skipSet;

    private String resourceName;

    private String detailName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public Integer getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Integer recommendType) {
        this.recommendType = recommendType;
    }

    public String getRecommendName() {
        return recommendName;
    }

    public void setRecommendName(String recommendName) {
        this.recommendName = recommendName == null ? null : recommendName.trim();
    }

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public Integer getSkipSet() {
        return skipSet;
    }

    public void setSkipSet(Integer skipSet) {
        this.skipSet = skipSet;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName == null ? null : resourceName.trim();
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName == null ? null : detailName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", locationId=").append(locationId);
        sb.append(", showId=").append(showId);
        sb.append(", recommendType=").append(recommendType);
        sb.append(", recommendName=").append(recommendName);
        sb.append(", skipType=").append(skipType);
        sb.append(", skipSet=").append(skipSet);
        sb.append(", resourceName=").append(resourceName);
        sb.append(", detailName=").append(detailName);
        sb.append("]");
        return sb.toString();
    }
}