package service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.match.Ranking;
import tool.CmTool;
import tool.EncrypteUserData;
import tool.Log4j;
import tool.db.RedisUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.sql.Timestamp;
import java.util.*;


/**
 * 用户服务
<<<<<<< HEAD
 *
 * @author xuwei
 */
public class UserService {
=======
 */
public class UserService
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static final Logger PAY = LoggerFactory.getLogger("MoneyLog");

    /**
     * 解析微信上传用户加密数据
     *
     * @param code          上传code
     * @param encryptedData 加密数据
     * @param iv            iv
     * @return 用户信息
     */
<<<<<<< HEAD
    public static EncrypteUserData getUserInfo(String wxAppId, String code, String encryptedData, String iv) {
        String sessionKey = null;
        String jsonResult = null;
        try {
=======
    public static EncrypteUserData getUserInfo(String wxAppId, String code, String encryptedData, String iv)
    {
        String sessionKey = null;
        String jsonResult = null;
        try
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            byte[] dataByte = Base64.decode(encryptedData);
            byte[] ivByte = Base64.decode(iv);
            PeDbWxConfig config = PeDbWxConfig.getConfigFast(wxAppId);
            jsonResult = CmTool.makeHttpConnect("https://api.weixin.qq.com/sns/jscode2session" + "?appid=" + config.ddAppId + "&secret=" + config.ddAppSecret + "&js_code=" + code + "&grant_type=authorization_code");
            JSONObject jsonObject = JSONObject.parseObject(jsonResult);
            sessionKey = jsonObject.getString("session_key");
            byte[] keyByte = Base64.decode(sessionKey);
            String openId = jsonObject.getString("openid");
            String encryptedResult = encryptedData(keyByte, dataByte, ivByte);
            LOG.debug("解析用户信息，返回结果:" + jsonResult + ",解密数据:" + encryptedResult);
            JSONObject datasObject = JSONObject.parseObject(encryptedResult);
            EncrypteUserData userData = new EncrypteUserData();

            userData.openId = openId;
            userData.unionId = datasObject.getString("unionId");
            userData.nickName = datasObject.getString("nickName");
            userData.sex = datasObject.getInteger("gender");
            userData.language = datasObject.getString("language");
            userData.city = datasObject.getString("city");
            userData.province = datasObject.getString("province");
            userData.country = datasObject.getString("country");
            userData.avatarUrl = datasObject.getString("avatarUrl");

            return userData;
<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error("EncrypteUserData:" + wxAppId + ",code=" + code + ",encryptedData=" + encryptedData + ",iv=" + iv + ",sessionKey=" + sessionKey + ",jsonResult=" + jsonResult);
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 获取一份随机用户 ID 的方法
     */
<<<<<<< HEAD
    public static String getOnceRandomUserId() {
=======
    public static String getOnceRandomUserId()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        StringBuilder sb = new StringBuilder();
        long currentDateValue = CmTool.getCurrentDateValue();
        Random random = new Random();

        random.setSeed(currentDateValue);

<<<<<<< HEAD
        for (int i = 0; i < 24; i++) {
=======
        for (int i = 0; i < 24; i++)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            int value = Math.abs((random.nextInt() + (int) currentDateValue) % 10);
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     * 微信完整的解密工具类
     *
     * @param keyByte  session key
     * @param dataByte 加密数据
     * @param ivByte   iv
     * @return 解密字串
     */
<<<<<<< HEAD
    private static String encryptedData(byte[] keyByte, byte[] dataByte, byte[] ivByte) {
        try {
            int base = 16;
            if (keyByte.length % base != 0) {
=======
    private static String encryptedData(byte[] keyByte, byte[] dataByte, byte[] ivByte)
    {
        try
        {
            int base = 16;
            if (keyByte.length % base != 0)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(2, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
<<<<<<< HEAD
            if ((resultByte != null) && (resultByte.length > 0)) {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
=======
            if ((resultByte != null) && (resultByte.length > 0))
            {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 保存用户数据
     *
     * @param user 用户数据
     */
<<<<<<< HEAD
    public static void saveUserData(JSONObject user) {
        //设置用户信息
        String uid = user.getString("uid");
        Map<String, String> hash = new HashMap<>();
        for (Map.Entry<String, Object> entry : user.entrySet()) {
=======
    public static void saveUserData(JSONObject user)
    {
        //设置用户信息
        String uid = user.getString("uid");
        Map<String, String> hash = new HashMap<>();
        for (Map.Entry<String, Object> entry : user.entrySet())
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            String key = entry.getKey();
            Object value = entry.getValue();
            hash.put(key, Objects.toString(value, "0"));
        }
        //用户数据保存一份到Redis内
        RedisUtils.hmset(getUserKey(uid), hash);
    }

    /**
     * 获取用户数值
     *
     * @param ddUid    用户编号
     * @param costType 类型
     * @return 数值
     */
<<<<<<< HEAD
    public static int getValue(String ddUid, String costType) {
=======
    public static int getValue(String ddUid, String costType)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String value = getStr(ddUid, costType);
        return Integer.valueOf(Objects.toString(value, "0"));
    }

    /**
     * 获取用户缓存值
     *
     * @param ddUid 用户编号
     * @param field 参数
     * @return 集合
     */
<<<<<<< HEAD
    public static String getStr(String ddUid, String field) {
=======
    public static String getStr(String ddUid, String field)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String key = getUserKey(ddUid);
        return RedisUtils.hget(key, field);
    }

    /**
     * 用户视频使用情况
     *
     * @param ddUid 用户编号
     * @return data
     */
<<<<<<< HEAD
    public static VideoUse userVideoUse(String ddUid) {
        String json = UserService.getStr(ddUid, "video");
        UserService.VideoUse use = new UserService.VideoUse();
        if (json != null) {
            use = CmTool.parseJSONByFastJSON(json, UserService.VideoUse.class);
        }
        int day = CmTool.getCurrentDataValueRough();
        if (use == null || use.dayFlag != day) {
=======
    public static VideoUse userVideoUse(String ddUid)
    {
        String json = UserService.getStr(ddUid, "video");
        UserService.VideoUse use = new UserService.VideoUse();
        if (json != null)
        {
            use = CmTool.parseJSONByFastJSON(json, UserService.VideoUse.class);
        }
        int day = CmTool.getCurrentDataValueRough();
        if (use == null || use.dayFlag != day)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            use = new UserService.VideoUse();
            use.dayFlag = day;
            use.game = ReadConfig.videoFreeGame;
            use.relive = ReadConfig.videoFreeRelive;
        }
        return use;
    }

<<<<<<< HEAD
    public static void setCache(String ddUid, String field, String value) {
=======
    public static void setCache(String ddUid, String field, String value)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String key = getUserKey(ddUid);
        RedisUtils.hset(key, field, value);
    }


    /**
     * 创建历史节点信息
     *
     * @param uid      用户编号
     * @param appId    用户所属appId
     * @param costType 用户消耗类型
     * @param history  用户历史
     * @param current  用户当前
     * @param value    用户消耗
     * @param type     用户操作类型
     * @param extra    用户附加
     * @return 历史
     */
<<<<<<< HEAD
    public static void addHistoryCost(String uid, String appId, String costType, long history, long current, int value, String type, String extra) {

        Timestamp ddTime = new Timestamp(System.currentTimeMillis());
        PeDbAllCost allCost = new PeDbAllCost(uid, appId, costType, history, current, value, type, ddTime);
        if (null != extra) {
=======
    public static void addHistoryCost(String uid, String appId, String costType, long history, long current, int value, String type, String extra)
    {

        Timestamp ddTime = new Timestamp(System.currentTimeMillis());
        PeDbAllCost allCost = new PeDbAllCost(uid, appId, costType, history, current, value, type, ddTime);
        if (null != extra)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            allCost.setDdCostExtra(extra);
        }
        allCost.insert();
        JSONObject data = new JSONObject();
        data.put("uid", uid);
        data.put("appId", appId);
        data.put("costType", costType);
        data.put("history", history);
        data.put("current", current);
        data.put("value", value);
        data.put("type", type);
        data.put("extra", extra);
        PAY.info(data.toJSONString());
    }

    /**
     * 添加用户数值表
     *
     * @param costType 获取类型
     * @param value    获取金额
     */
<<<<<<< HEAD
    public static long addValue(String ddUid, String costType, int value, JSONObject extra) {
        String key = getUserKey(ddUid);
        //进行自增处理
        long price = RedisUtils.hincrby(key, costType, value);
        if (price < 0 && value < 0) {
=======
    public static long addValue(String ddUid, String costType, int value, JSONObject extra)
    {
        String key = getUserKey(ddUid);
        //进行自增处理
        long price = RedisUtils.hincrby(key, costType, value);
        if (price < 0 && value < 0)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            RedisUtils.hincrby(key, costType, -value);
            return -1;
        }
        //进行更新数据库
        PeDbUserValue userValue = new PeDbUserValue();
        userValue.ddUid = ddUid;
        String filter = "";
<<<<<<< HEAD
        switch (costType) {
            case "rmb": {
                userValue.ddMoney = price;
                filter = filter.concat("ddMoney");
                if (value > 0) {
=======
        switch (costType)
        {
            case "rmb":
            {
                userValue.ddMoney = price;
                filter = filter.concat("ddMoney");
                if (value > 0)
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    userValue.ddAwardMoney = RedisUtils.hincrby(key, costType + "Total", value);
                    filter = filter.concat("#ddAwardMoney");
                }
            }
            break;
<<<<<<< HEAD
            case "coin": {
                userValue.ddCoinCount = price;
                filter = filter.concat("ddCoinCount");
                if (value > 0) {
=======
            case "coin":
            {
                userValue.ddCoinCount = price;
                filter = filter.concat("ddCoinCount");
                if (value > 0)
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    userValue.ddAwardCoin = RedisUtils.hincrby(key, costType + "Total", value);
                    filter = filter.concat("#ddAwardCoin");
                }
            }
            break;
            default:
                break;
        }
        userValue.update(filter);
        //添加記錄
        addHistoryCost(ddUid, extra.getString("appId"), costType, price - value, price, -value, extra.getString("type"), extra.getString("extra"));
        return price;
    }

    /**
     * 用户信息
     *
     * @param ddUid 用户唯一标志
     * @return Redis key
     */
<<<<<<< HEAD
    private static String getUserKey(String ddUid) {
=======
    private static String getUserKey(String ddUid)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return "user-" + ddUid;
    }

    /**
     * 获取openId
     *
     * @param ddUid   用户编号
     * @param ddAppId appId
     * @return 用户openId
     */
<<<<<<< HEAD
    public static String getOpenId(String ddUid, String ddAppId) {
        String json = getStr(ddUid, ddAppId);
        if (json != null) {
=======
    public static String getOpenId(String ddUid, String ddAppId)
    {
        String json = getStr(ddUid, ddAppId);
        if (json != null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            JSONObject object = JSONObject.parseObject(json);
            return object.getString("openId");
        }
        return null;
    }


    /**
     * 进行更新签到
     *
     * @param field 类型
     * @param sign  数值
     * @param count 个数
     */
<<<<<<< HEAD
    void updateSign(String field, String sign, int count) {
=======
    void updateSign(String field, String sign, int count)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject update = new JSONObject();
        update.put("key", "ddDayLoginGift");
        update.put("value", String.valueOf(count));
        updateValue(field, sign, update);
    }

    /**
     * 进行更新分享
     *
     * @param field  类型
     * @param value  数值
     * @param update 个数
     */
<<<<<<< HEAD
    public void updateValue(String field, String value, JSONObject update) {
=======
    public void updateValue(String field, String value, JSONObject update)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        setCache(ddUid, field, value);
        //进行更新数据库
        String table = PeDbObject.getTableName(PeDbUser.class);
        String key = update.getString("key"), context = update.getString("value");
        String SQL = String.format("update %s set %s='%s' where ddUid='%s'", table, key, context, ddUid);
        CmDbSqlResource.instance().addQueue(SQL);
    }

    /**
     * 进行设置用户数据
     *
     * @param result 用户设置参数
     * @param ddUid  用户编号
     */
<<<<<<< HEAD
    public static void putUserValue(JSONObject result, String ddUid) {
        String key = getUserKey(ddUid);
        Map<String, String> hash = RedisUtils.hgetall(key);
        if (hash != null) {
            hash.forEach((k, v) ->
            {
                if (k.endsWith("record") || k.startsWith("dd")) {
                    return;
                }
                result.put(k, v);
            });
        }
        result.put("avatarFrameGain", getUserFrameGain(ddUid));
        VideoUse use = userVideoUse(ddUid);
        result.put("free_relive", use.relive);
        result.put("free_game", use.game);
        if (result.containsKey("goodsVideo")) {
            JSONObject data = JSONObject.parseObject(result.getString("goodsVideo"));
            int now = CmTool.getCurrentDataValueRough();
            if (data.getInteger("dayFlag") != null) {
=======
    public static void putUserValue(JSONObject result, String ddUid)
    {
        String key = getUserKey(ddUid);
        Map<String, String> hash = RedisUtils.hgetall(key);
        if (hash != null)
        {
            hash.forEach((k, v) ->
            {
                if (k.endsWith("record") || k.startsWith("dd"))
                    return;
                result.put(k, v);
            });
        }
        VideoUse use = userVideoUse(ddUid);
        result.put("free_relive", use.relive);
        result.put("free_game", use.game);
        if (result.containsKey("goodsVideo"))
        {
            JSONObject data = JSONObject.parseObject(result.getString("goodsVideo"));
            int now = CmTool.getCurrentDataValueRough();
            if (data.getInteger("dayFlag") != null)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                data.put("dayFlag", now);
                data.put("count", 10);
            }
            result.put("goodsVideo", data.toJSONString());
        }
        JSONObject pkSuccess = Ranking.getPkCount(ddUid);
<<<<<<< HEAD
        if (pkSuccess != null) {
            result.put("pkSuccess", pkSuccess);
        }
=======
        if (pkSuccess != null)
            result.put("pkSuccess", pkSuccess);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    }


    /**
     * 设置用户邀请信息
     *
     * @param uid        用户编号
     * @param userGained 注册用户信息
     */
<<<<<<< HEAD
    public static void setUserInvite(String uid, PeDbUser userGained) {
        String invite = RedisUtils.hget(getUserKey(uid), "inviteList");
        JSONArray list = null;
        if (invite != null) {
            list = JSONObject.parseArray(invite);
        }
        if (list == null) {
            list = new JSONArray();
        }
        for (int i = 0; i < list.size(); i++) {
            JSONObject temp = list.getJSONObject(i);
            //重复领取
            if (temp.getString("uid").equals(userGained.ddUid)) {
                return;
            }
=======
    public static void setUserInvite(String uid, PeDbUser userGained)
    {
        String invite = RedisUtils.hget(getUserKey(uid), "inviteList");
        JSONArray list = null;
        if (invite != null)
        {
            list = JSONObject.parseArray(invite);
        }
        if (list == null)
            list = new JSONArray();
        for (int i = 0; i < list.size(); i++)
        {
            JSONObject temp = list.getJSONObject(i);
            //重复领取
            if (temp.getString("uid").equals(userGained.ddUid))
                return;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        }
        JSONObject user = new JSONObject();
        user.put("uid", userGained.ddUid);
        user.put("headUrl", userGained.ddAvatarUrl);
        user.put("name", userGained.ddName);
        user.put("sex", userGained.ddSex);
        user.put("times", System.currentTimeMillis());
        user.put("receive", false);
        list.add(user);
        RedisUtils.hset(getUserKey(uid), "inviteList", CmTool.getJSONByFastJSON(list));
        addFlushNode(uid, "inviteList");
    }

    /**
     * 添加被动刷新接口.8
     *
     * @param ddUid 用户编号
     * @param field 被动刷新参数
     */
<<<<<<< HEAD
    private static void addFlushNode(String ddUid, String field) {
        String key = getUserKey(ddUid);
        String flag = RedisUtils.hget(key, "flushFlag");
        List<String> nodes = null;
        if (flag != null) {
            nodes = JSONObject.parseArray(flag, String.class);
        }
        if (nodes == null) {
            nodes = new Vector<>();
        }
        if (nodes.contains(field)) {
            return;
        }
=======
    private static void addFlushNode(String ddUid, String field)
    {
        String key = getUserKey(ddUid);
        String flag = RedisUtils.hget(key, "flushFlag");
        List<String> nodes = null;
        if (flag != null)
            nodes = JSONObject.parseArray(flag, String.class);
        if (nodes == null)
            nodes = new Vector<>();
        if (nodes.contains(field))
            return;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        nodes.add(field);
        RedisUtils.hset(key, "flushFlag", CmTool.getJSONByFastJSON(nodes));
    }

<<<<<<< HEAD
    public static class VideoUse {
=======
    public static class VideoUse
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        public int dayFlag;
        public int game;
        public int relive;
    }


    private String ddUid;

<<<<<<< HEAD
    public UserService(String ddUid) {
=======
    public UserService(String ddUid)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        this.ddUid = ddUid;
    }

    /**
     * 获取用户的刷新标签
     *
     * @return 标签项
     */
<<<<<<< HEAD
    public List<String> getFlushFlag() {
        String flag = RedisUtils.hget(getUserKey(ddUid), "flushFlag");
        if (flag == null) {
            return null;
        }
=======
    public List<String> getFlushFlag()
    {
        String flag = RedisUtils.hget(getUserKey(ddUid), "flushFlag");
        if (flag == null)
            return null;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return JSONObject.parseArray(flag, String.class);
    }

    /**
     * 进行读取刷新节点
     *
     * @param flushFlag 刷新节点
     */
<<<<<<< HEAD
    public JSONObject readFlushNode(List<String> flushFlag) {
=======
    public JSONObject readFlushNode(List<String> flushFlag)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject result = new JSONObject();
        String key = getUserKey(ddUid);
        RedisUtils.hdel(key, "flushFlag");
        String[] field = new String[flushFlag.size()];
<<<<<<< HEAD
        if (field.length <= 0) {
=======
        if (field.length <= 0)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            return null;
        }
        field = flushFlag.toArray(field);
        List<String> data = RedisUtils.hmget(key, field);
<<<<<<< HEAD
        if (data == null || data.isEmpty()) {
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
=======
        if (data == null || data.isEmpty())
            return null;
        for (int i = 0; i < data.size(); i++)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            result.put(field[i], data.get(i));
        }
        return result;
    }

    /**
     * 用户领取奖励
     *
     * @param rewards 奖励内容
     * @param base    翻倍数
     * @param extra   额外数据
     */
<<<<<<< HEAD
    static JSONObject receiveReward(String ddUid, List<WelfareService.Reward> rewards, int base, JSONObject extra) {
=======
    public static JSONObject receiveReward(String ddUid, List<WelfareService.Reward> rewards, int base, JSONObject extra)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject result = new JSONObject();
        rewards.forEach(reward ->
        {
            int total = reward.total;
<<<<<<< HEAD
            switch (reward.type) {
                case "rmb":
                case "coin": {
=======
            switch (reward.type)
            {
                case "rmb":
                case "coin":
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    total = total * base;
                    result.put(reward.type, addValue(ddUid, reward.type, total, extra));
                }
                break;
<<<<<<< HEAD
                case "avatarFrameGain": {
                    result.put(reward.type, addUserFrame(ddUid, total));
                }
                break;
                default:
                    break;
=======
                case "avatarFrameGain":
                {
                    result.put(reward.type, addUserFrame(ddUid, total));
                }
                break;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

            }
        });
        return result;
    }

    /**
     * 检测当前头像使用情况
     *
     * @param ddUid       用户编号
     * @param avatarFrame 头像数据
     */
<<<<<<< HEAD
    public static boolean existUseFrame(String ddUid, int avatarFrame) {
=======
    public static boolean existUseFrame(String ddUid, int avatarFrame)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String frame = getStr(ddUid, "avatarFrame");
        return frame != null && frame.equals(String.valueOf(avatarFrame));
    }

    /**
     * 检测头像是否存在
     *
     * @param ddUid       用户编号
     * @param avatarFrame 存在头像
     */
<<<<<<< HEAD
    public static boolean existAvatarFrame(String ddUid, int avatarFrame) {
        String avatarFrameGain = getStr(ddUid, "avatarFrameGain");
        // 默认头像框拥有
        if (avatarFrame == 0) {
            return true;
        }
        if (avatarFrameGain != null) {
=======
    public static boolean existAvatarFrame(String ddUid, int avatarFrame)
    {
        String avatarFrameGain = getStr(ddUid, "avatarFrameGain");
        if (avatarFrameGain != null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            return Arrays.stream(avatarFrameGain.split("#")).anyMatch(temp -> temp.equals(String.valueOf(avatarFrame)));
        }
        return false;
    }

    /**
     * 更新头像
     *
     * @param update 头像信息
     */
<<<<<<< HEAD
    public static void updateAvatarFrame(JSONObject update) {
=======
    public static void updateAvatarFrame(JSONObject update)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String uid = update.getString("uid");
        int avatarFrame = update.getInteger("avatarFrame");
        setCache(uid, "avatarFrame", String.valueOf(avatarFrame));
        PeDbUser user = new PeDbUser();
        user.ddUid = uid;
        user.ddAvatarFrame = avatarFrame;
        user.update("ddAvatarFrame");
    }

<<<<<<< HEAD
    /**
     * 获取用户头像记录
     * 默认头像存在
     *
     * @param ddUid 用户编号
     * @return 记录信息
     */
    public static String getUserFrameGain(String ddUid) {
        String avatarFrameGain = getStr(ddUid, "avatarFrameGain");
        String gain = Objects.toString(avatarFrameGain, "0");
        if (!gain.startsWith("0")) {
            return "0#" + gain;
        }
        return gain;
    }
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

    /**
     * 添加用户头像框
     *
     * @param ddUid 用户编号
     * @param frame 头像框
     * @return 当前用户头像框
     */
<<<<<<< HEAD
    public static String addUserFrame(String ddUid, int frame) {

        String avatarFrameGain = getUserFrameGain(ddUid);
        if (Arrays.stream(avatarFrameGain.split("#")).anyMatch(temp -> temp.equals(String.valueOf(frame)))) {
            return avatarFrameGain;
        }
        avatarFrameGain = avatarFrameGain.concat("#" + frame);
=======
    public static String addUserFrame(String ddUid, int frame)
    {
        String avatarFrameGain = getStr(ddUid, "avatarFrameGain");
        if (avatarFrameGain != null)
        {
            if (Arrays.stream(avatarFrameGain.split("#")).anyMatch(temp -> temp.equals(String.valueOf(frame))))
                return avatarFrameGain;
            avatarFrameGain = avatarFrameGain.concat("#" + frame);
        } else
        {
            avatarFrameGain = String.valueOf(frame);
        }
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //
        PeDbUser user = new PeDbUser();
        user.ddUid = ddUid;
        user.ddAvatarFrameGain = avatarFrameGain;
        user.update("ddAvatarFrameGain");
        setCache(ddUid, "avatarFrameGain", avatarFrameGain);
        return avatarFrameGain;
    }


    /**
     * 获取视频商品次数
     *
     * @return 节点
     */
<<<<<<< HEAD
    JSONObject getGoodVideo() {
=======
    public JSONObject getGoodVideo()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String video = getStr(ddUid, "goodsVideo");
        String json = ReadConfig.get("video-config");
        JSONObject reward = JSONObject.parseObject(json);
        int coin = reward.getInteger("coin");
        int dayCount = reward.getInteger("dayCount");
        JSONObject object = new JSONObject();
<<<<<<< HEAD
        if (video != null) {
=======
        if (video != null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            object = JSONObject.parseObject(video);
        }
        Integer dayFlag = object.getInteger("dayFlag");
        int now = CmTool.getCurrentDataValueRough();
<<<<<<< HEAD
        if (dayFlag == null || dayFlag != now) {
=======
        if (dayFlag == null || dayFlag != now)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            object.put("dayFlag", now);
            object.put("count", dayCount);
            object.put("coin", coin);
        }
        return object;
    }

    /**
     * 设置用户邀请信息
     */
<<<<<<< HEAD
    boolean receiveUserInvite(String gainUid, JSONObject result) {
        String invite = RedisUtils.hget(getUserKey(ddUid), "inviteList");
        JSONArray list = null;
        if (invite != null) {
            list = JSONObject.parseArray(invite);
        }
        if (list == null) {
            list = new JSONArray();
        }
        boolean update = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject data = list.getJSONObject(i);
            if (data.getString("uid").equals(gainUid)) {
                if (data.getBoolean("receive")) {
                    return false;
                }
=======
    public boolean receiveUserInvite(String gainUid, JSONObject result)
    {
        String invite = RedisUtils.hget(getUserKey(ddUid), "inviteList");
        JSONArray list = null;
        if (invite != null)
        {
            list = JSONObject.parseArray(invite);
        }
        if (list == null)
            list = new JSONArray();
        boolean update = false;
        for (int i = 0; i < list.size(); i++)
        {
            JSONObject data = list.getJSONObject(i);
            if (data.getString("uid").equals(gainUid))
            {
                if (data.getBoolean("receive"))
                    return false;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                data.put("receive", true);
                update = true;
                break;
            }
        }
        String json = CmTool.getJSONByFastJSON(list);
<<<<<<< HEAD
        if (update) {
=======
        if (update)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            RedisUtils.hset(getUserKey(ddUid), "inviteList", json);
        }
        result.put("inviteList", json);
        return update;
    }
}
