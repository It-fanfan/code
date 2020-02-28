package servlet.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbGame;
import service.match.Ranking;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/query/recordRanking")
public class CmServletQueryRecord extends CmServletMain {
=======
@WebServlet(urlPatterns = "/query/recordRanking")
public class CmServletQueryRecord extends CmServletMain
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
<<<<<<< HEAD
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
=======
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String uid = requestPackage.getString("uid");
        JSONArray keys = requestPackage.getJSONArray("matchKeys");
        JSONArray games = requestPackage.getJSONArray("gameCodes");
        JSONObject result = new JSONObject();
        result.put("result", "success");

        JSONArray matchData = new JSONArray();
<<<<<<< HEAD
        if (keys != null) {
            for (int i = 0; i < keys.size(); i++) {
=======
        if (keys != null)
        {
            for (int i = 0; i < keys.size(); i++)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                String key = keys.getString(i);
                JSONObject info = getMatchData(key, uid);
                matchData.add(info);
            }
        }
        result.put("matchList", matchData);
        JSONArray gameList = new JSONArray();
<<<<<<< HEAD
        if (games != null) {
            Set<Integer> gameSet = new HashSet<>();
            for (int i = 0; i < games.size(); i++) {
=======
        if (games != null)
        {
            Set<Integer> gameSet = new HashSet<>();
            for (int i = 0; i < games.size(); i++)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                gameSet.add(games.getInteger(i));
            }
            gameSet.forEach(gameCode ->
            {
                PeDbGame game = PeDbGame.getGameFast(gameCode);
<<<<<<< HEAD
                if (game != null) {
=======
                if (game != null)
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    JSONObject message = game.getMessage();
                    JSONObject info = new JSONObject();
                    info.put("gameCode", gameCode);
                    info.put("zipUrl", message.getString("zipUrl"));
                    info.put("name", message.getString("name"));
                    info.put("friendUrl", message.getString("friendUrl"));
                    info.put("isPk", message.getBoolean("isPk"));
                    gameList.add(info);
                }

            });
        }

        result.put("gameList", gameList);
        return result;
    }

    /**
     * 赛场配置
     *
     * @param matchKey 赛场编号
     * @param uid      用户编号
     * @return 赛场信息
     */
<<<<<<< HEAD
    private JSONObject getMatchData(String matchKey, String uid) {
=======
    private JSONObject getMatchData(String matchKey, String uid)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject matchInfo = Ranking.getMatchInfo(matchKey);
        JSONObject result = new JSONObject();
        //赛区信息
        result.put("matchInfo", matchInfo);
        //获取用户排行信息
        result.put("rank", Ranking.getRankingData(uid, matchKey));
        return result;
    }
}
