package service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import config.ReadConfig;
import tool.CmTool;

import java.util.List;
import java.util.Vector;

<<<<<<< HEAD
/**
 * @author xuwei
 */
public class WelfareService {
    /**
     * 用户信息
     */
    private final String ddUid;
    private final String appId;

    public WelfareService(String uid, String appId) {
=======
public class WelfareService
{
    //用户信息
    private final String ddUid;
    private final String appId;

    public WelfareService(String uid, String appId)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        this.ddUid = uid;
        this.appId = appId;
    }

    /**
     * 签到奖励处理
     *
     * @return Redis信息
     */
<<<<<<< HEAD
    private static String getSignField() {
=======
    private static String getSignField()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return "sign";
    }

    /**
     * 领取奖励
     *
     * @param result 下发结果
     * @param param  参数
     */
<<<<<<< HEAD
    public void receive(JSONObject result, JSONObject param) {
        String type = param.getString("type");
        int day = CmTool.getCurrentDataValueRough();
        switch (type) {
            //分享,客服
            case "share":
            case "service": {
                receiveShareOrService(result, type, day);
                break;
            }
            //收藏
            case "collect":
                //关注公众号
            case "interest": {
                receiveCollectOrInterest(result, type);
            }
            break;
            //游戏成功
            case "gameSuccess": {
                receiveGameSuccess(result, param, type);
            }
            break;
            case "video": {
                receiveVideo(result, type);
            }
            break;
            //邀请奖励
            case "invite": {
                receiveInvite(result, param, type);
            }
            break;
            default:
                break;

        }
    }

    /**
     * 领取邀请奖励
     *
     * @param result 下发结果
     * @param param  上报参数
     * @param type   类型
     */
    private void receiveInvite(JSONObject result, JSONObject param, String type) {
        UserService service = new UserService(ddUid);
        boolean update = service.receiveUserInvite(param.getString("otherid"), result);
        if (update) {
            Reward reward = CmTool.parseJSONByFastJSON(ReadConfig.get("invite-config"), Reward.class);
            List<Reward> rewards = new Vector<>();
            rewards.add(reward);
            JSONObject extra = new JSONObject();
            extra.put("type", type);
            extra.put("appId", appId);
            result.putAll(UserService.receiveReward(ddUid, rewards, 1, extra));
        }
    }

    /**
     * 领取视频奖励
     *
     * @param result 下发结果
     * @param type   上报类型
     */
    private void receiveVideo(JSONObject result, String type) {
        UserService service = new UserService(ddUid);
        JSONObject value = service.getGoodVideo();
        int count = value.getInteger("count");
        if (count > 0) {
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

    /**
     * 领取游戏结算奖励
     *
     * @param result 下发结果
     * @param param  上报参数
     * @param type   领取类型
     */
    private void receiveGameSuccess(JSONObject result, JSONObject param, String type) {
        boolean video = param.getBoolean("video");
        boolean isPk = param.getBoolean("isPk");
        GameSuccessConfig config = getGameSuccessConfig(type + "-config");
        int value;
        if (isPk) {
            value = config.isPk;

        } else {
            value = config.synergy;
        }
        int base = 1;
        if (video) {
            base = 2;
        }
        List<Reward> rewards = new Vector<>();
        rewards.add(new Reward("coin", value));
        JSONObject extra = new JSONObject();
        extra.put("type", type);
        extra.put("appId", appId);
        extra.put("extra", String.valueOf(isPk));
        result.putAll(UserService.receiveReward(ddUid, rewards, base, extra));
    }

    /**
     * 领取收藏和关注
     *
     * @param result 发送结果
     * @param type   领取类型
     */
    private void receiveCollectOrInterest(JSONObject result, String type) {
        String collect = UserService.getStr(ddUid, type);
        //是否已经收藏
        if (collect != null && Integer.valueOf(collect) > 0) {
            return;
        }
        WelfareConfig config = getWelfareConfig(type + "-config");
        if (config == null) {
            return;
        }
        String value = String.valueOf(config.count);
        String key;
        if ("collect".equals(type)) {
            key = "ddCollected";
        } else {
            key = "ddInterested";
        }
        JSONObject extra = new JSONObject();
        extra.put("type", type);
        extra.put("appId", appId);
        result.putAll(rewardReceive(key, value, type, config.rewards, extra));
    }

    /**
     * 領取分享和客服
     *
     * @param result json結果
     * @param type   领取类型
     * @param day    日期
     */
    private void receiveShareOrService(JSONObject result, String type, int day) {
        String value = UserService.getStr(ddUid, type);
        //当天还未分享
        if (value != null && value.contains(String.valueOf(day))) {
            return;
        }
        WelfareConfig config = getWelfareConfig(type + "-config");
        if (config == null) {
            return;
        }
        value = String.valueOf(day);
        String key = "ddServicedTime";
        if ("share".equals(type)) {
            key = "ddSharedTime";
        }
        JSONObject extra = new JSONObject();
        extra.put("type", type);
        extra.put("appId", appId);
        result.putAll(rewardReceive(key, value, type, config.rewards, extra));
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
<<<<<<< HEAD
    private JSONObject rewardReceive(String key, String value, String field, List<Reward> rewards, JSONObject extra) {
=======
    private JSONObject rewardReceive(String key, String value, String field, List<Reward> rewards, JSONObject extra)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
<<<<<<< HEAD
     */
    public void sign(JSONObject result, boolean share) {
=======
     *
     * @return 是否签到成功
     */
    public boolean sign(JSONObject result, boolean share)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String field = getSignField();
        String sign = UserService.getStr(ddUid, field);
        int day = CmTool.getCurrentDataValueRough();

        //今天已经签到
<<<<<<< HEAD
        if (sign != null && sign.contains(String.valueOf(day))) {
            return;
        }
        StringBuilder data = new StringBuilder();
        if (sign == null) {
            data.append(day);
        } else {
=======
        if (sign != null && sign.contains(String.valueOf(day)))
            return true;
        StringBuilder data = new StringBuilder();
        if (sign == null)
            data.append(day);
        else
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            data.append(sign).append("#").append(day);
        }
        sign = data.toString();
        //当前签到数值
        int count = sign.split("#").length;
        UserService service = new UserService(ddUid);
        service.updateSign(field, sign, count);
        result.put(field, sign);
        SignConfig config = getSignConfig(count);
<<<<<<< HEAD
        if (config != null) {
            int base = 1;
            if (share) {
                base = config.share;
            }
=======
        if (config != null)
        {
            int base = 1;
            if (share)
                base = config.share;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            JSONObject extra = new JSONObject();
            extra.put("type", "sign");
            extra.put("appId", appId);
            extra.put("extra", String.valueOf(count));
            result.putAll(UserService.receiveReward(ddUid, config.rewards, base, extra));
        }
<<<<<<< HEAD
    }

    public static class GameSuccessConfig {
=======
        return true;
    }

    public static class GameSuccessConfig
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        public int isPk;
        public int synergy;
    }

    /**
     * 游戏成功配置
     *
     * @return 配置参数
     */
<<<<<<< HEAD
    private static GameSuccessConfig getGameSuccessConfig(String key) {
=======
    private static GameSuccessConfig getGameSuccessConfig(String key)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String json = ReadConfig.get(key);
        return CmTool.parseJSONByFastJSON(json, GameSuccessConfig.class);
    }

    //奖励类型
<<<<<<< HEAD
    public static class Reward {
        public String type;
        public int total;

        public Reward() {
        }

        public Reward(String type, int total) {
=======
    public static class Reward
    {
        public String type;
        public int total;

        public Reward()
        {
        }

        public Reward(String type, int total)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            this.type = type;
            this.total = total;
        }
    }


<<<<<<< HEAD
    public static class WelfareConfig {
=======
    public static class WelfareConfig
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //次数限制
        public int count;
        //奖励
        public List<Reward> rewards;
    }

<<<<<<< HEAD
    public static class SignConfig {
=======
    public static class SignConfig
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
<<<<<<< HEAD
    private static WelfareConfig getWelfareConfig(String key) {
=======
    public static WelfareConfig getWelfareConfig(String key)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String json = ReadConfig.get(key);
        return CmTool.parseJSONByFastJSON(json, WelfareConfig.class);
    }

    /**
     * 获取当前签到配置
     *
     * @param day 签到天数
     * @return 签到奖励
     */
<<<<<<< HEAD
    private static SignConfig getSignConfig(int day) {
        String json = ReadConfig.get("sign-config");
        if (json == null) {
            return null;
        }
        Vector<SignConfig> config = CmTool.parseJSONByFastJSON(json, new TypeToken<Vector<SignConfig>>() {
        }.getType());
        if (config == null) {
            return null;
        }
        if (config.size() < day) {
            return null;
        }
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        int value = day - 1;
        return config.stream().filter(data -> data.day == value).findFirst().orElse(null);
    }

}
