package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.*;

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
    private static Map<Integer, PeDbRoundMatch> allMatches = new HashMap<>();

    /**
     * 获取游戏信息
     */
    public static PeDbRoundMatch getMatchFast(int code)
    {
        //游戏编号
        PeDbRoundMatch group = allMatches.get(code);
        if (group == null)
            return null;
        return group;
    }

    /**
     * 获取商品信息
     */
    public static Map<Integer, PeDbRoundMatch> getMatchesFast()
    {
        return allMatches;
    }

    /**
     * 获取游戏列表
     */
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
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return data;
    }


    /**
     * 同步游戏列表
     */
    private static void syncGoods()
    {
        allMatches = getGroupMatch();
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncGoods();
    }

    /**
     * 获取赛制配置
     *
     * @param appId 应用编号
     * @return 对应match
     */
    public static Vector<PeDbRoundMatch> getMatchByAppId(String appId)
    {
        //當前還未開始，已經已經開始的
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        long current = System.currentTimeMillis();
        list.removeIf(match -> !match.ddState || !appId.equals(match.ddAppId) || match.ddEnd.getTime() < current);
        return list;
    }

    /**
     * 获取历史赛制
     *
     * @param appId 应用编号
     * @return 对应match
     */
    public static Vector<PeDbRoundMatch> getMatchHistoryByAppId(String appId)
    {
        //當前還未開始或已經開始的
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        long current = System.currentTimeMillis();
        list.removeIf(match -> !match.ddState || !appId.equals(match.ddAppId) || match.ddEnd.getTime() >= current);
        return list;
    }


    /**
     * 赛制配置
     */
    public JSONObject getMessage()
    {
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
        return info;
    }

    public static JSONObject getMessage(Vector<PeDbRoundMatch> matches)
    {
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
            if (dbGame != null)
            {
                JSONObject info = dbGame.getMessage();
                info.put("shareInfo", dbGame.getShareConfig());
                gameArray.add(info);
            }
        });
        rounds.forEach(round ->
        {
            PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(round);
            if (roundExt != null)
            {
                roundArray.add(roundExt.getMessage());
            }
        });
        matchNotice.put("gameList", gameArray);
        matchNotice.put("roundList", roundArray);
        matchNotice.put("matchList", matchArray);
        return matchNotice;
    }


    /**
     * 获取赛制信息
     *
     * @return 赛制公告
     */
    public static JSONObject getMessage(long current)
    {
        //赛制预告
        Map<Integer, PeDbRoundMatch> matches = PeDbRoundMatch.getMatchesFast();
        Vector<PeDbRoundMatch> list = new Vector<>(matches.values());
        list.removeIf(match -> !match.ddState || match.ddEnd.getTime() < current);
        return getMessage(list);
    }

    @Override
    public String toString()
    {
        return "PeDbRoundGroup{" + "ddCode=" + ddCode + ", ddState=" + ddState + ", ddGame=" + ddGame + ", ddRound='" + ddRound + '\'' + ", ddStart=" + ddStart + ", ddEnd=" + ddEnd + '}';
    }
}
