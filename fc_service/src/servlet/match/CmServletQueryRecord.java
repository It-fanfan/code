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

@WebServlet(urlPatterns = "/query/recordRanking")
public class CmServletQueryRecord extends CmServletMain
{
    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String uid = requestPackage.getString("uid");
        JSONArray keys = requestPackage.getJSONArray("matchKeys");
        JSONArray games = requestPackage.getJSONArray("gameCodes");
        JSONObject result = new JSONObject();
        result.put("result", "success");

        JSONArray matchData = new JSONArray();
        if (keys != null)
        {
            for (int i = 0; i < keys.size(); i++)
            {
                String key = keys.getString(i);
                JSONObject info = getMatchData(key, uid);
                matchData.add(info);
            }
        }
        result.put("matchList", matchData);
        JSONArray gameList = new JSONArray();
        if (games != null)
        {
            Set<Integer> gameSet = new HashSet<>();
            for (int i = 0; i < games.size(); i++)
            {
                gameSet.add(games.getInteger(i));
            }
            gameSet.forEach(gameCode ->
            {
                PeDbGame game = PeDbGame.getGameFast(gameCode);
                if (game != null)
                {
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
    private JSONObject getMatchData(String matchKey, String uid)
    {
        JSONObject matchInfo = Ranking.getMatchInfo(matchKey);
        JSONObject result = new JSONObject();
        //赛区信息
        result.put("matchInfo", matchInfo);
        //获取用户排行信息
        result.put("rank", Ranking.getRankingData(uid, matchKey));
        return result;
    }
}
