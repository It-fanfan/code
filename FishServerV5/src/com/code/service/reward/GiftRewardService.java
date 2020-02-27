package com.code.service.reward;

import com.code.cache.UserCache;

/**
 * 礼包奖励
 */
public class GiftRewardService
{

    private UserCache userCache;

    /**
     * 玩家充值金额	100 元	0.32
     * 玩家仅 1 个月活跃天数	20 天	0.28
     * 玩家近 7 天累计活跃值	980 分	0.25
     * 用户激活好友个数	20 个	0.15
     */
    public GiftRewardService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 进行初步构造
     *
     * @return
     */
    public double initWeight()
    {
        //        double price = userCache.
        return 0;
    }

    /**
     * 礼包类型
     */
    public enum GiftType
    {
        wood(1, 1),//木材
        oil(1, 1),//石油
        axe(1, 1),//斧头
        pearl(1, 1);//珍珠

        int rate;
        int total;

        GiftType(int rate, int total)
        {
            this.rate = rate;
            this.total = total;
        }

        public int getRate()
        {
            return rate;
        }

        public int getTotal()
        {
            return total;
        }}

    /**
     * 触发UI界面
     */
    public enum TriggerUI
    {
        sign(0.1, 1),//签到
        day_activity(0.1, 0.5),//日活跃
        week_activity(0.1, 1),//周活跃
        angling(0.1, 0.2),//垂钓
        achievement(0.1, 1);//成就

        private double base;
        private double weight;

        TriggerUI(double base, double weight)
        {
            this.base = base;
            this.weight = weight;
        }

        public double getBase()
        {
            return base;
        }

        public double getWeight()
        {
            return weight;
        }}

    public static class UserGiftParameter
    {

    }
}
