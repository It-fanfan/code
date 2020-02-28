package com.fish.dao.second.model;

import java.util.Date;

public class UserInfo {
    private Long userid;

    private String openid;

    private String unionid;

    private String nickname;

    private String icon;

    private String platform;

    private Integer sex;

    private String city;

    private String province;

    private String country;

    private Long invite;

    private String groupid;

    private Date registtime;

    private Date logintime;

    private String invitegroupid;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public Long getInvite() {
        return invite;
    }

    public void setInvite(Long invite) {
        this.invite = invite;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    public Date getRegisttime() {
        return registtime;
    }

    public void setRegisttime(Date registtime) {
        this.registtime = registtime;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getInvitegroupid() {
        return invitegroupid;
    }

    public void setInvitegroupid(String invitegroupid) {
        this.invitegroupid = invitegroupid == null ? null : invitegroupid.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userid=").append(userid);
        sb.append(", openid=").append(openid);
        sb.append(", unionid=").append(unionid);
        sb.append(", nickname=").append(nickname);
        sb.append(", icon=").append(icon);
        sb.append(", platform=").append(platform);
        sb.append(", sex=").append(sex);
        sb.append(", city=").append(city);
        sb.append(", province=").append(province);
        sb.append(", country=").append(country);
        sb.append(", invite=").append(invite);
        sb.append(", groupid=").append(groupid);
        sb.append(", registtime=").append(registtime);
        sb.append(", logintime=").append(logintime);
        sb.append(", invitegroupid=").append(invitegroupid);
        sb.append("]");
        return sb.toString();
    }
}