/**
 *
 */
package pipe;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.*;
import service.match.DayRankingService;
import service.match.Ranking;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * @author feng
 */
public class PeConfigGames
{

    //获取游戏合集并游戏编号
    public static PeDbGameSet getGameSet(String appId, String version)
    {
        PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
        if (appConfig == null)
            return null;
        Integer setId;
        if (version.equals(appConfig.ddCheckVersion))
            setId = appConfig.ddCheckCode;
        else
            setId = appConfig.ddCode;
        return PeDbGameSet.getGameSetFast(setId);
    }

    /**
     * 获取游戏列表信息
     */
    public static void gainGamesMessage(JSONObject result, JSONObject request)
    {
        String appId = request.getString("appId");
        String version = request.getString("version");
        PeDbGameSet gameSet = getGameSet(appId, version);
        //无集合信息
        if (gameSet == null)
            return;

        //集合状态
        result.put("setCode", gameSet.ddCode);
        result.put("setName", gameSet.ddName);
        result.put("state", gameSet.ddState);
        JSONArray gameCode = request.getJSONArray("gameCode");
        if (gameCode != null)
        {
            for (int i = 0; i < gameCode.size(); i++)
            {
                Integer id = gameCode.getInteger(i);
                PeDbGame game = PeDbGame.getGameFast(id);
                if (game != null)
                    result.put("game" + i, game.getMessage());
            }
            return;
        }
        //下发游戏集合
        Vector<PeDbGame> dbGames = gameSet.getGameSetGames();
        for (int i = 0; i < dbGames.size(); i++)
        {
            result.put("game" + i, dbGames.elementAt(i).getMessage());
        }
        System.out.println(result.toJSONString());
    }


    /**
     * 獲取ping节点数据
     *
     * @param result  下发结果
     * @param request 请求参数
     */
    public static void gainGamesExtMessage(JSONObject result, JSONObject request)
    {
        String appId = request.getString("appId");
        String version = request.getString("version");
        //检测分支
        PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
        //小程序分支
        if (appConfig.ddProgram == 1)
        {
            //获取当前赛制
            Vector<PeDbRoundMatch> list = PeDbRoundMatch.getMatchByAppId(appConfig.ddAppId);
            if (list != null)
                result.putAll(PeDbRoundMatch.getMessage(list));
            //获取历史赛制信息
            Vector<PeDbRoundMatch> history = PeDbRoundMatch.getMatchHistoryByAppId(appConfig.ddAppId);
            JSONArray matchHistory = new JSONArray();
            history.forEach(data ->
            {
                JSONObject info = new JSONObject();
                info.put("start", data.ddStart.getTime());
                info.put("end", data.ddEnd.getTime());
                info.put("name", data.ddName);
                info.put("gameCode", data.ddGame);
                info.put("matchKey", Ranking.getField(data.ddCode, true, 0));
                matchHistory.add(info);
            });
            result.put("historyMatch", matchHistory);
            return;
        }
        PeDbGameSet gameSet = getGameSet(appId, version);
        //无集合信息
        if (gameSet == null)
            return;
        //合集数据
        JSONObject data = new JSONObject();
        data.put("setCode", gameSet.ddCode);
        data.put("setName", gameSet.ddName);
        data.put("state", gameSet.ddState);
        JSONArray gameList = new JSONArray();
        //下发游戏集合
        Vector<PeDbGame> dbGames = gameSet.getGameSetGames();
        Set<String> round = new HashSet<>();
        dbGames.forEach(game ->
        {
            JSONObject info = game.getMessage();
            //获取赛制信息
            String matchKey = DayRankingService.getCurrentMatchKey(game.ddCode);
            JSONObject matchInfo = Ranking.getMatchInfo(matchKey);
            if (matchInfo != null)
            {
                info.put("matchInfo", matchInfo);
                round.add(matchInfo.getString("round"));
            }

            info.put("shareInfo", game.getShareConfig());
            gameList.add(info);
        });
        JSONArray rounds = new JSONArray();
        round.forEach(code ->
        {
            PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(code);
            if (roundExt != null)
                rounds.add(roundExt.getMessage());
        });
        data.put("rounds", rounds);
        data.put("gameList", gameList);
        result.put("games", data);
        PeDbWxConfig wxConfig = PeDbWxConfig.getConfigFast(appId);
        if (wxConfig != null)
        {
            result.put("appIdConfig", wxConfig.getAppIdConfig());
            result.put("shareConfig", wxConfig.getShareConfig());
        }
        System.out.println(result.toJSONString());
    }
}
