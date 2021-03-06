package service.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.PeDbRoundExt;
import tool.CmTool;
import tool.db.RedisUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

<<<<<<< HEAD
/**
 * @author xuwei
 */
public interface Ranking {

    /**
     * 赛场状态
     */
=======
public interface Ranking
{

    //赛场状态
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    String ROUND_REDIS_KEY = "round-status";


    /**
     * 获取排行信息
     */
    RankingService getService();

    /**
     * 获取用户集合信息
     *
     * @param userId 用户编号
     * @return 集合
     */
<<<<<<< HEAD
    static List<String> getUserCollects(String... userId) {
        if (userId.length <= 0) {
            return null;
        }
=======
    static List<String> getUserCollects(String... userId)
    {
        if (userId.length <= 0)
            return null;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String key = getUserCollectsKey();
        return RedisUtils.hmget(key, userId);
    }

    /**
     * 用户集合信息
     *
     * @return 用户集合key
     */
<<<<<<< HEAD
    static String getUserCollectsKey() {
=======
    static String getUserCollectsKey()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return "ranking-user-collect";
    }

    /**
     * 获取pk胜率
     *
     * @param data     用户节点
     * @param gameCode 游戲編號
     */
<<<<<<< HEAD
    static int getPkSuccess(JSONObject data, int gameCode) {
        String key = String.valueOf(gameCode);
        JSONObject pkSuccess = data.getJSONObject("pkSuccess");
        if (pkSuccess != null && pkSuccess.containsKey(key)) {
            return pkSuccess.getInteger(key);
        }
=======
    static int getPkSuccess(JSONObject data, int gameCode)
    {
        String key = String.valueOf(gameCode);
        JSONObject pkSuccess = data.getJSONObject("pkSuccess");
        if (pkSuccess != null && pkSuccess.containsKey(key))
            return pkSuccess.getInteger(key);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return 0;
    }

    /**
     * 胜率详情
     *
     * @param uid 用户编号
     * @return 胜率信息
     */
<<<<<<< HEAD
    static JSONObject getPkCount(String uid) {
        String key = getUserCollectsKey();
        String json = RedisUtils.hget(key, uid);
        JSONObject user = null;
        if (json != null) {
            user = JSONObject.parseObject(json);
        }
        if (user != null) {
            return user.getJSONObject("pkSuccess");
        }
=======
    static JSONObject getPkCount(String uid)
    {
        String key = getUserCollectsKey();
        String json = RedisUtils.hget(key, uid);
        JSONObject user = null;
        if (json != null)
            user = JSONObject.parseObject(json);
        if (user != null)
            return user.getJSONObject("pkSuccess");
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return null;
    }

    /**
     * 添加胜率
     *
     * @param uid      用户编号
     * @param gameCode 游戏编号
     */
<<<<<<< HEAD
    static void addPkSuccess(String uid, int gameCode) {
        String key = getUserCollectsKey();
        String json = RedisUtils.hget(key, uid);
        JSONObject user = null;
        if (json != null) {
            user = JSONObject.parseObject(json);
        }
        if (user == null) {
            user = new JSONObject();
        }
        JSONObject pkSuccess = user.getJSONObject("pkSuccess");
        if (pkSuccess == null) {
            pkSuccess = new JSONObject();
        }
        Integer value = pkSuccess.getInteger(String.valueOf(gameCode));
        if (value == null) {
            value = 1;
        } else {
            value += 1;
        }
=======
    static void addPkSuccess(String uid, int gameCode)
    {
        String key = getUserCollectsKey();
        String json = RedisUtils.hget(key, uid);
        JSONObject user = null;
        if (json != null)
            user = JSONObject.parseObject(json);
        if (user == null)
            user = new JSONObject();
        JSONObject pkSuccess = user.getJSONObject("pkSuccess");
        if (pkSuccess == null)
            pkSuccess = new JSONObject();
        Integer value = pkSuccess.getInteger(String.valueOf(gameCode));
        if (value == null)
            value = 1;
        else
            value += 1;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        pkSuccess.put(String.valueOf(gameCode), value);
        user.put("pkSuccess", pkSuccess);
        RedisUtils.hset(key, uid, user.toJSONString());
    }


    /**
     * 獲取排行榜用戶
     *
     * @param userList 用戶列表
     * @return 用戶詳情
     */
<<<<<<< HEAD
    static JSONObject getPkSuccess(JSONArray userList) {
        JSONObject pkSuccess = new JSONObject();
        String[] field = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
=======
    static JSONObject getPkSuccess(JSONArray userList)
    {
        JSONObject pkSuccess = new JSONObject();
        String[] field = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            field[i] = userList.getString(i);
        }
        //获取用户详情
        List<String> ifs = getUserCollects(field);
<<<<<<< HEAD
        if (ifs != null) {
            for (String info : ifs) {
=======
        if (ifs != null)
        {
            for (String info : ifs)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                JSONObject data = JSONObject.parseObject(info);
                pkSuccess.put(data.getString("uid"), data.getJSONObject("pkSuccess"));
            }
        }
        return pkSuccess;
    }

    /**
     * 獲取排行榜用戶
     *
     * @param userList 用戶列表
     * @return 用戶詳情
     */
<<<<<<< HEAD
    static JSONArray globalRanking(JSONArray userList) {
        JSONArray array = new JSONArray();
        String[] field = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
=======
    static JSONArray globalRanking(JSONArray userList)
    {
        JSONArray array = new JSONArray();
        String[] field = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            field[i] = userList.getString(i);
        }
        //获取用户详情
        List<String> ifs = getUserCollects(field);
<<<<<<< HEAD
        if (ifs != null) {
            for (String info : ifs) {
=======
        if (ifs != null)
        {
            for (String info : ifs)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                JSONObject data = JSONObject.parseObject(info);
                data.remove("pkSuccess");
                array.add(data);
            }
        }
        return array;
    }

    /**
     * 获取用户排行信息
     *
     * @param ddUid 用户ID
     * @param key   排行榜key
     * @return 排行信息
     */
<<<<<<< HEAD
    static JSONObject getUserRanking(String ddUid, String key) {
=======
    static JSONObject getUserRanking(String ddUid, String key)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return RedisUtils.zSetTuple(key, ddUid);
    }

    /**
     * 设置用户排行数据
     *
     * @param uid 排行用户
     */
<<<<<<< HEAD
    static JSONObject getRankingData(String uid, String matchKey) {
=======
    static JSONObject getRankingData(String uid, String matchKey)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject user = getUserRanking(uid, matchKey);
        user.put("uid", uid);
        return user;
    }

    /**
     * 獲取排行榜分數
     *
     * @param uid      用戶信息
     * @param matchKey 賽場編號
     * @return 分數
     */
<<<<<<< HEAD
    static Double getRankingScore(String uid, String matchKey) {
=======
    static Double getRankingScore(String uid, String matchKey)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return RedisUtils.zscore(matchKey, uid);
    }

    /**
     * 獲取賽區詳情
     *
     * @param matchKey 赛区编号
     * @return 赛区信息
     */
<<<<<<< HEAD
    static JSONObject getMatchInfo(String matchKey) {
        String matchJson = RedisUtils.hget(ROUND_REDIS_KEY, matchKey);
        if (matchJson != null) {
            JSONObject matchInfo = JSONObject.parseObject(matchJson);
=======
    static JSONObject getMatchInfo(String matchKey)
    {
        String matchJSON = RedisUtils.hget(ROUND_REDIS_KEY, matchKey);
        if (matchJSON != null)
        {
            JSONObject matchInfo = JSONObject.parseObject(matchJSON);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            matchInfo.put("submit", matchInfo.getLong("submit") + 6000);
            return matchInfo;
        }
        return null;
    }

    /**
     * 获取赛制信息
     *
     * @return 赛制内容
     */
    JSONObject getMatchInfo();

    /**
     * 获取赛场记录标签
     *
     * @param ddCode  赛场编号
     * @param ddGroup 群标签
     * @param ddIndex 轮次编号
     */
<<<<<<< HEAD
    static String getField(int ddCode, boolean ddGroup, int ddIndex) {
=======
    static String getField(int ddCode, boolean ddGroup, int ddIndex)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return String.format("match-c%d-g%d-i%d", ddCode, ddGroup ? 1 : 0, ddIndex);
    }

    /**
     * 获取当前索引
     *
     * @param isPk    pk模式
     * @param now     当前时间
     * @param ddStart 赛场开启时间
     * @return 赛场索引
     */
<<<<<<< HEAD
    static JSONObject getMatchIndex(boolean isPk, long now, Timestamp ddStart, Timestamp ddEnd, PeDbRoundExt ext) {
=======
    static JSONObject getMatchIndex(boolean isPk, long now, Timestamp ddStart, Timestamp ddEnd, PeDbRoundExt ext)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        long ddTime = ext.ddTime + (isPk ? 5 * 60 : 0);
        BigDecimal value = CmTool.div(now - ddStart.getTime(), 1000 * ddTime, 0);
        JSONObject data = new JSONObject();
        //当前索引
        int index = value.intValue();
        data.put("index", index);
        //开始时间
        long start = ddStart.getTime() + (index * 1000 * ddTime);
        //截至时间,PK模式结算往后移
        long submit = ddStart.getTime() + (index + 1) * 1000 * ddTime;
        long end = submit - (isPk ? 5 * 60 : 0);
        //赛制结束立马结束
<<<<<<< HEAD
        if (submit > ddEnd.getTime()) {
            submit = ddEnd.getTime();
        }
        if (end > ddEnd.getTime()) {
=======
        if (submit > ddEnd.getTime())
        {
            submit = ddEnd.getTime();
        }
        if (end > ddEnd.getTime())
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            end = ddEnd.getTime();
        }
        data.put("start", new Timestamp(start));
        data.put("end", new Timestamp(end));
        data.put("submit", new Timestamp(submit));
        //获取当场开始时间
        return data;
    }

    /**
     * 设置排行榜服务
     */
<<<<<<< HEAD
    default RankingService setRedisKey() {
        JSONObject matchInfo = getMatchInfo();
        if (matchInfo != null) {
            if ("running".equals(matchInfo.getString("status"))) {
=======
    default RankingService setRedisKey()
    {
        JSONObject matchInfo = getMatchInfo();
        if (matchInfo != null)
        {
            if ("running".equals(matchInfo.getString("status")))
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                int code = matchInfo.getInteger("code");
                boolean isGroup = matchInfo.getBoolean("isGroup");
                String matchKey = getField(code, isGroup, matchInfo.getInteger("index"));
                int gameCode = matchInfo.getInteger("gameCode");
                String ddRound = matchInfo.getString("round");
<<<<<<< HEAD
                if (!matchInfo.containsKey("name") && ddRound != null) {
                    PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(ddRound);
                    if (roundExt != null) {
=======
                if (!matchInfo.containsKey("name") && ddRound != null)
                {
                    PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(ddRound);
                    if (roundExt != null)
                    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                        matchInfo.put("name", roundExt.ddName);
                    }
                }

                return new RankingService(gameCode, matchKey);
            }
        }
        return null;
    }
}
