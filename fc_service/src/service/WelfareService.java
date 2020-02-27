package service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import config.ReadConfig;
import tool.CmTool;

import java.util.List;
import java.util.Vector;

public class WelfareService
{
    //用户信息
    private final String ddUid;
    private final String appId;

    public WelfareService(String uid, String appId)
    {
        this.ddUid = uid;
        this.appId = appId;
    }

    /**
     * 签到奖励处理
     *
     * @return Redis信息
     */
    private static String getSignField()
    {
        return "sign";
    }

    /**
     * 领取奖励
     *
     * @param result 下发结果
     * @param param  参数
     */
    public void receive(JSONObject result, JSONObject param)
    {
        String type = param.getString("type");
        int day = CmTool.getCurrentDataValueRough();
        switch (type)
        {
            //分享,客服
            case "share":
            case "service":
            {
                String value = UserService.getStr(ddUid, type);
                //当天还未分享
                if (value != null && value.contains(String.valueOf(day)))
                    break;
                WelfareConfig config = getWelfareConfig(type + "-config");
                if (config == null)
                    break;
                value = String.valueOf(day);
                String key = "ddServicedTime";
                if ("share".equals(type))
                    key = "ddSharedTime";
                JSONObject extra = new JSONObject();
                extra.put("type", type);
                extra.put("appId", appId);
                result.putAll(rewardReceive(key, value, type, config.rewards, extra));
            }
            break;
            case "collect"://收藏
            case "interest"://关注公众号
            {
                String collect = UserService.getStr(ddUid, type);
                //是否已经收藏
                if (collect != null && Integer.valueOf(collect) > 0)
                    break;
                WelfareConfig config = getWelfareConfig(type + "-config");
                if (config == null)
                    break;
                String value = String.valueOf(config.count);
                String key;
                if ("collect".equals(type))
                    key = "ddCollected";
                else
                    key = "ddInterested";
                JSONObject extra = new JSONObject();
                extra.put("type", type);
                extra.put("appId", appId);
                result.putAll(rewardReceive(key, value, type, config.rewards, extra));
            }
            break;
            case "gameSuccess"://游戏成功
            {
                boolean video = param.getBoolean("video");
                boolean isPk = param.getBoolean("isPk");
                GameSuccessConfig config = getGameSuccessConfig(type + "-config");
                int value;
                if (isPk)
                {
                    value = config.isPk;

                } else
                {
                    value = config.synergy;
                }
                int base = 1;
                if (video)
                    base = 2;
                List<Reward> rewards = new Vector<>();
                rewards.add(new Reward("coin", value));
                JSONObject extra = new JSONObject();
                extra.put("type", type);
                extra.put("appId", appId);
                extra.put("extra", String.valueOf(isPk));
                result.putAll(UserService.receiveReward(ddUid, rewards, base, extra));
            }
            break;
            case "video":
            {
                UserService service = new UserService(ddUid);
                JSONObject value = service.getGoodVideo();
                int count = value.getInteger("count");
                if (count > 0)
                {
                    List<Reward> rewards = new Vector<>();
                    rewards.add(new Reward("coin", value.getInteger("coin")));
                    JSONObject extra = new JSONObject();
                    extra.put("type", type);
                    extra.put("appId", appId);
                    extra.put("extra", String.valueOf(count));
                    result.putAll(UserService.receiveReward(ddUid, rewards, 1, extra));
                    value.put("count", count - 1);
                    UserService.setCache(ddUid, "goodsVideo", value.toJSONString());
                }
                result.put("goodsVideo", value.toJSONString());
            }
            break;
            case "invite"://邀请奖励
            {
                UserService service = new UserService(ddUid);
                boolean update = service.receiveUserInvite(param.getString("otherid"), result);
                if (update)
                {
                    WelfareService.Reward reward = CmTool.parseJSONByFastJSON(ReadConfig.get("invite-config"), WelfareService.Reward.class);
                    List<Reward> rewards = new Vector<>();
                    rewards.add(reward);
                    JSONObject extra = new JSONObject();
                    extra.put("type", type);
                    extra.put("appId", appId);
                    result.putAll(UserService.receiveReward(ddUid, rewards, 1, extra));
                }
            }
            break;

        }
    }

    /**
     * 奖励内容
     *
     * @param key     数据库关键字
     * @param value   数据库存储值
     * @param field   Redis保存关键字
     * @param rewards 奖励详细
     * @param extra   输出
     */
    private JSONObject rewardReceive(String key, String value, String field, List<Reward> rewards, JSONObject extra)
    {
        JSONObject result = new JSONObject();
        JSONObject update = new JSONObject();
        update.put("key", key);
        update.put("value", value);
        UserService service = new UserService(ddUid);
        service.updateValue(field, value, update);
        result.put(field, value);
        result.putAll(UserService.receiveReward(ddUid, rewards, 1, extra));
        return result;
    }

    /**
     * 进行签到
     *
     * @return 是否签到成功
     */
    public boolean sign(JSONObject result, boolean share)
    {
        String field = getSignField();
        String sign = UserService.getStr(ddUid, field);
        int day = CmTool.getCurrentDataValueRough();

        //今天已经签到
        if (sign != null && sign.contains(String.valueOf(day)))
            return true;
        StringBuilder data = new StringBuilder();
        if (sign == null)
            data.append(day);
        else
        {
            data.append(sign).append("#").append(day);
        }
        sign = data.toString();
        //当前签到数值
        int count = sign.split("#").length;
        UserService service = new UserService(ddUid);
        service.updateSign(field, sign, count);
        result.put(field, sign);
        SignConfig config = getSignConfig(count);
        if (config != null)
        {
            int base = 1;
            if (share)
                base = config.share;
            JSONObject extra = new JSONObject();
            extra.put("type", "sign");
            extra.put("appId", appId);
            extra.put("extra", String.valueOf(count));
            result.putAll(UserService.receiveReward(ddUid, config.rewards, base, extra));
        }
        return true;
    }

    public static class GameSuccessConfig
    {
        public int isPk;
        public int synergy;
    }

    /**
     * 游戏成功配置
     *
     * @return 配置参数
     */
    private static GameSuccessConfig getGameSuccessConfig(String key)
    {
        String json = ReadConfig.get(key);
        return CmTool.parseJSONByFastJSON(json, GameSuccessConfig.class);
    }

    //奖励类型
    public static class Reward
    {
        public String type;
        public int total;

        public Reward()
        {
        }

        public Reward(String type, int total)
        {
            this.type = type;
            this.total = total;
        }
    }


    public static class WelfareConfig
    {
        //次数限制
        public int count;
        //奖励
        public List<Reward> rewards;
    }

    public static class SignConfig
    {
        //签到天
        public int day;
        //签到奖励
        public List<Reward> rewards;
        //是否分享翻倍
        public int share;
    }

    /**
     * 分享配置
     *
     * @return 配置参数
     */
    public static WelfareConfig getWelfareConfig(String key)
    {
        String json = ReadConfig.get(key);
        return CmTool.parseJSONByFastJSON(json, WelfareConfig.class);
    }

    /**
     * 获取当前签到配置
     *
     * @param day 签到天数
     * @return 签到奖励
     */
    public static SignConfig getSignConfig(int day)
    {
        String json = ReadConfig.get("sign-config");
        if (json == null)
            return null;
        Vector<SignConfig> config = CmTool.parseJSONByFastJSON(json, new TypeToken<Vector<SignConfig>>()
        {
        }.getType());
        if (config == null)
            return null;
        if (config.size() < day)
            return null;
        int value = day - 1;
        return config.stream().filter(data -> data.day == value).findFirst().orElse(null);
    }

}
