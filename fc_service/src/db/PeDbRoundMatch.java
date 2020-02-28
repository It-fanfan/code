package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.*;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@Entity(name = "round_match")
@ManagedData(name = "persie_deamon")
public class PeDbRoundMatch extends PeDbObject {
    /**
     * 编号
     */
    public int ddCode;
    /**
     * 賽名稱
     */
    public String ddName;
    /**
     * 赛场指定appId
     */
    public String ddAppId;
    /**
     * 状态
     */
    public boolean ddState;
    /**
     * 游戏编号
     */
    public int ddGame;
    /**
     * 赛场类型
     */
    public String ddRound;
    /**
     * 开启时间
     */
    public Timestamp ddStart;
    /**
     * 截至时间
     */
    public Timestamp ddEnd;

    /**
     * 赛场资源
     */
    public String ddRes;
    /**
     * 赛场版本信息
     */
    public String ddMatchVersion;

    /**
     * 全部比賽列表
     */
=======
@Entity(name = "round_match")
@ManagedData(name = "persie_deamon")
public class PeDbRoundMatch extends PeDbObject
{
    //编号
    public int ddCode;
    //賽名稱
    public String ddName;
    //赛场指定appId
    public String ddAppId;
    //状态
    public boolean ddState;
    //游戏编号
    public int ddGame;
    //赛场类型
    public String ddRound;
    //开启时间
    public Timestamp ddStart;
    //截至时间
    public Timestamp ddEnd;

    //
    // 全部比賽列表
    //
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    private static Map<Integer, PeDbRoundMatch> allMatches = new HashMap<>();

    /**
     * 获取游戏信息
     */
<<<<<<< HEAD
    public static PeDbRoundMatch getMatchFast(int code) {
        //游戏编号
        PeDbRoundMatch group = allMatches.get(code);
        if (group == null) {
            return null;
        }
=======
    public static PeDbRoundMatch getMatchFast(int code)
    {
        //游戏编号
        PeDbRoundMatch group = allMatches.get(code);
        if (group == null)
            return null;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return group;
    }

    /**
     * 获取商品信息
     */
<<<<<<< HEAD
    private static Map<Integer, PeDbRoundMatch> getMatchesFast() {
=======
    public static Map<Integer, PeDbRoundMatch> getMatchesFast()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return allMatches;
    }

    /**
     * 获取游戏列表
     */
<<<<<<< HEAD
    private static Map<Integer, PeDbRoundMatch> getGroupMatch() {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<Integer, PeDbRoundMatch> data = new HashMap<>();

        try {
            Vector<PeDbObject> objects = PeDbRoundMatch.queryObject(sqlResource, PeDbRoundMatch.class, "");
            for (PeDbObject obj : objects) {
                PeDbRoundMatch group = (PeDbRoundMatch) obj;
                data.put(group.ddCode, group);
            }
        } catch (Exception e) {
=======
    private static Map<Integer, PeDbRoundMatch> getGroupMatch()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<Integer, PeDbRoundMatch> data = new HashMap<>();

        try
        {
            Vector<PeDbObject> objects = PeDbRoundMatch.queryObject(sqlResource, PeDbRoundMatch.class, "");
            for (PeDbObject obj : objects)
            {
                PeDbRoundMatch group = (PeDbRoundMatch) obj;
                data.put(group.ddCode, group);
            }
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return data;
    }


    /**
     * 同步游戏列表
     */
<<<<<<< HEAD
    private static void syncGoods() {
=======
    private static void syncGoods()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        allMatches = getGroupMatch();
    }

    /**
     * 初始化数据对象
     */
<<<<<<< HEAD
    public static void init() {
=======
    public static void init()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        syncGoods();
    }

    /**
     * 获取赛制配置
     *
<<<<<<< HEAD
     * @param appId   应用编号
     * @param version 应用版本
     * @return 对应match
     */
    public static Vector<PeDbRoundMatch> getMatchByAppId(String appId, String version) {
=======
     * @param appId 应用编号
     * @return 对应match
     */
    public static Vector<PeDbRoundMatch> getMatchByAppId(String appId)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //當前還未開始，已經已經開始的
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        long current = System.currentTimeMillis();
<<<<<<< HEAD
        list.removeIf(match -> existRemoveMatch(appId, version, match) || match.ddEnd.getTime() < current);
=======
        list.removeIf(match -> !match.ddState || !appId.equals(match.ddAppId) || match.ddEnd.getTime() < current);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return list;
    }

    /**
     * 获取历史赛制
     *
     * @param appId 应用编号
     * @return 对应match
     */
<<<<<<< HEAD
    public static Vector<PeDbRoundMatch> getMatchHistoryByAppId(String appId, String version) {
=======
    public static Vector<PeDbRoundMatch> getMatchHistoryByAppId(String appId)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //當前還未開始或已經開始的
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        long current = System.currentTimeMillis();
<<<<<<< HEAD
        list.removeIf(match -> existRemoveMatch(appId, version, match) || match.ddEnd.getTime() >= current);
        return list;
    }

    /**
     * 检测是否移除赛制
     *
     * @param appId   应用编号
     * @param version 应用版本
     * @param match   需匹配的赛场信息
     * @return 是否移除
     */
    private static boolean existRemoveMatch(String appId, String version, PeDbRoundMatch match) {
        if (!match.ddState || !appId.equals(match.ddAppId)) {
            return true;
        }
        if (match.ddMatchVersion == null || match.ddMatchVersion.trim().isEmpty()) {
            return false;
        }
        return !match.ddMatchVersion.contains(version);
    }

=======
        list.removeIf(match -> !match.ddState || !appId.equals(match.ddAppId) || match.ddEnd.getTime() >= current);
        return list;
    }

>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

    /**
     * 赛制配置
     */
<<<<<<< HEAD
    public JSONObject getMessage() {
=======
    public JSONObject getMessage()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject info = new JSONObject();
        info.put("code", ddCode);
        info.put("isGroup", true);
        info.put("index", 0);
        info.put("appId", ddAppId);
        info.put("gameCode", ddGame);
        info.put("start", ddStart.getTime());
        info.put("end", ddEnd.getTime() + 6 * 1000);
        info.put("round", ddRound);
        info.put("name", ddName);
<<<<<<< HEAD
        info.put("zipRes", ddRes);
        return info;
    }

    public static JSONObject getMessage(Vector<PeDbRoundMatch> matches) {
=======
        return info;
    }

    public static JSONObject getMessage(Vector<PeDbRoundMatch> matches)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject matchNotice = new JSONObject();
        JSONArray matchArray = new JSONArray();
        JSONArray gameArray = new JSONArray();
        JSONArray roundArray = new JSONArray();
        Set<Integer> games = new HashSet<>();
        Set<String> rounds = new HashSet<>();
        matches.forEach(match ->
        {
            JSONObject info = match.getMessage();
            games.add(match.ddGame);
            rounds.add(match.ddRound);
            matchArray.add(info);
        });
        games.forEach(gameCode ->
        {
            PeDbGame dbGame = PeDbGame.getGameFast(gameCode);
<<<<<<< HEAD
            if (dbGame != null) {
=======
            if (dbGame != null)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                JSONObject info = dbGame.getMessage();
                info.put("shareInfo", dbGame.getShareConfig());
                gameArray.add(info);
            }
        });
        rounds.forEach(round ->
        {
            PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(round);
<<<<<<< HEAD
            if (roundExt != null) {
                roundArray.add(roundExt.getMessage());
            }
        });
        //游戲列表
        matchNotice.put("gameList", gameArray);
        //賽区列表
        matchNotice.put("roundList", roundArray);
        //详细赛制
=======
            if (roundExt != null)
            {
                roundArray.add(roundExt.getMessage());
            }
        });
        matchNotice.put("gameList", gameArray);
        matchNotice.put("roundList", roundArray);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        matchNotice.put("matchList", matchArray);
        return matchNotice;
    }


    /**
     * 获取赛制信息
     *
     * @return 赛制公告
     */
<<<<<<< HEAD
    public static JSONObject getMessage(long current) {
=======
    public static JSONObject getMessage(long current)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //赛制预告
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        list.removeIf(match -> !match.ddState || match.ddEnd.getTime() < current);
        return getMessage(list);
    }

    @Override
<<<<<<< HEAD
    public String toString() {
=======
    public String toString()
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        return "PeDbRoundGroup{" + "ddCode=" + ddCode + ", ddState=" + ddState + ", ddGame=" + ddGame + ", ddRound='" + ddRound + '\'' + ", ddStart=" + ddStart + ", ddEnd=" + ddEnd + '}';
    }
}
