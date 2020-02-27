package com.code.protocols.login;

public class LoginRequest
{
    //玩家編號
    public String userid;
    //当前本地化
    public String localization;
    //平台
    public Platform platform = Platform.wechat;
    //设备
    public Device device;

    public enum Platform
    {
        wechat,//微信小游戏
        baidu,//百度小游戏
        facebook, //FB小游戏
        appstore,//AppStore
        googleplay, //GooglePlay
        qq,//QQ游戏中心
        android, //国内安卓
        windows,//设备
        huawei,//华为快游戏
        other, //其他
        vivo,//vivo快游戏
        oppo,//oppo快游戏
        xiaomi
    }

    public enum Device
    {
        ios,//iOS
        android,//安卓
        windows,//设备
        other //其他
    }
}
