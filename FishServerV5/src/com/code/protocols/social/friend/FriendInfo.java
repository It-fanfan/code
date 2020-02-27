package com.code.protocols.social.friend;

import com.annotation.ReadOnly;

public class FriendInfo
{
    //好友编号
    public String userid;
    //昵称
    public String nickname;
    //头像
    public String icon;
    //玩家水域
    public int basinid;
    //是否拥有共同好友
    public Boolean samefriend;
    public String tip;
    //是否已经申请
    @ReadOnly
    public Boolean isapply;
    //皮肤信息
    public String skin;
    //剩余赠送次数
    public int presented;
    //图鉴数
    public int booknum;

    public int gloal;

    public FriendInfo()
    {
        super();
    }

    public FriendInfo(String userid, String nickname, String icon, int basinid, boolean samefriend, String tip)
    {
        this.userid = userid;
        this.nickname = nickname;
        this.icon = icon;
        this.basinid = basinid;
        this.samefriend = samefriend;
        this.tip = tip;
    }

    public FriendInfo(String userId, String nickName, String icon, int basinid)
    {
        this.userid = userId;
        this.nickname = nickName;
        this.icon = icon;
        this.basinid = basinid;
    }
}
