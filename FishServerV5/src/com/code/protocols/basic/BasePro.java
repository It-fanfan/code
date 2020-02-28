package com.code.protocols.basic;

import java.util.List;

public class BasePro
{
    public enum RewardType
    {
        shell,//贝壳
        book,//图鉴
        pearl,//珍珠
        props,//道具
        material,//材料
        none,//空奖励
    }

    public static class RewardInfo
    {
        public RewardType type;
        public int id;
        public int total;

        public RewardInfo()
        {
        }

        public RewardInfo(String type, int id, int total)
        {
            this.type = RewardType.valueOf(type);
            this.id = id;
            this.total = total;
        }
    }

    /**
     * 被偷猜玩家信息
     */
    public static class RewardUser
    {
        //被猜用户userid
        public String userid;
        //用户头像
        public String icon;
        //昵称
        public String nickname;
        //玩家段位
        public int basin;
        //是否為機器人
        public boolean robot;
    }

    /**
     * 玩家图鉴数据
     */
    public static class Book
    {
        //好友守护信息
        public List<FriendPro> gardians;
        //当前点亮图标数据
        public List<Integer> lightens;
        //被偷图鉴信息
        public List<Integer> stolen;
    }

    /**
     * 玩家图鉴水域信息
     */
    public static class Basin
    {
        // 水域等级
        public int basinid;
        //图鉴信息
        public Book book;
        //材料数据 :Vector<MaterialResult>
        public BigData material;
    }

    /**
     * 好友守护信息
     */
    public static class FriendPro
    {
        //玩家id
        public String userid;
        //是否守护中
        public boolean status;
        //cd开始时间
        public String cdtime;

        public FriendPro(String userid, boolean status, String cdtime)
        {
            this.userid = userid;
            this.status = status;
            this.cdtime = cdtime;
        }
    }

    public static class UserTurntable
    {
        /**
         * 免费次数
         */
        public int freecount;

        /**
         * 通过广告获取次数
         */
        public int adcount;
    }
}
