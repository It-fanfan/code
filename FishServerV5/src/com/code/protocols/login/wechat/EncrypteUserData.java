package com.code.protocols.login.wechat;

public class EncrypteUserData
{
    // openid
    public String openId;
    // unionid
    public String unionId;
    // 昵称
    public String nickName;
    // 性别
    public int gender;
    // 城市
    public String city;
    // 省市
    public String province;
    // 国家
    public String country;
    // 头像url
    public String avatarUrl;

    @Override
    public String toString()
    {
        return "EncrypteUserData [openId=" + openId + ", unionId=" + unionId + ", nickName=" + nickName + ", gender=" + gender + ", city=" + city + ", province=" + province + ", country=" + country + ", avatarUrl=" + avatarUrl + "]";
    }
}
