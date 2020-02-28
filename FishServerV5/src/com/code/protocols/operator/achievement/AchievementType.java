package com.code.protocols.operator.achievement;

/**
 * 成就类型
 */
public enum AchievementType
{
    none("空"),
    FishingFrequency("捕鱼 - 捕鱼次数"),
    FishingGood("捕鱼 - 不错"),
    FishingNumber("捕鱼 - 鱼数量"),
    FishingPerfectCombo("捕鱼 - 完美连击"),
    FishingPropBombs("捕鱼 - 道具 - 炸弹"),
    FishingPropEel("捕鱼 - 道具 - 电鳗"),
    FishingPropIce("捕鱼 - 道具 - 冰鱼"),
    FishingPropShell("捕鱼 - 道具 - 贝壳鱼"),
    Friends("好友数"),
    Invite("邀新数"),
    StealBookSuccess("偷图鉴 - 成功"),
    StealShellPerfect("偷贝壳 - 完美"),
    StoreDeals("商店交易"),
    Ads("看广告"),
    Basin("海域"),
    Book("图鉴"),
    Combo("捕鱼 - combo"),
    Order("订单"),
    UnFish("捕鱼 - 没鱼啦"),
    ;

    private String desc;

    AchievementType(String desc)
    {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }}
