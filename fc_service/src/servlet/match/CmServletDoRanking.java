package servlet.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import db.PeDbGame;
import service.match.DayRankingService;
import service.match.Ranking;
import service.match.RankingService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author feng
 */
@WebServlet(urlPatterns = "/doranking")
public class CmServletDoRanking extends CmServletMain
{

    private static final long serialVersionUID = -535259216745644822L;

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
        JSONArray users = requestPackage.getJSONArray("users");
        //游戏编号
        int gameCode = requestPackage.getInteger("gameCode");
        //游戏模式
        int gameMode = requestPackage.getInteger("gameMode");
        //0:正常匹配，1：好友匹配
        int type = requestPackage.getInteger("type");
        //是否通关
        boolean allFinish = requestPackage.getBoolean("allFinish");

        PeDbGame game = PeDbGame.getGameFast(gameCode);
        if (game == null)
            return null;
        //是否PK模式
        boolean isPk = game.ddIsPk == 1;
        if (null == users || gameCode < 0)
        {
            return null;
        }
        //群比赛编号
        String matchKey = requestPackage.getString("matchKey");
        if (matchKey == null)
        {
            matchKey = DayRankingService.getCurrentMatchKey(gameCode);
        }
        if (matchKey == null)
            return null;
        JSONObject matchInfo = Ranking.getMatchInfo(matchKey);
        if (matchInfo == null || !"running".equals(matchInfo.getString("status")))
        {
            return null;
        }
        RankingService service = new RankingService(gameCode, matchKey);
        //用户得分情况
        Map<String, Integer> userScore = new HashMap<>();
        for (int i = users.size() - 1; i >= 0; i--)
        {
            JSONObject object = users.getJSONObject(i);
            String uid = object.getString("uid");
            int mark = object.getInteger("mark");
            if (null == uid)
            {
                continue;
            }
            //复活次数
            int lose = object.getInteger("lose");
            if (gameMode == 1 && isPk)
            {
                mark = allFinish ? ReadConfig.getInt("pkThroughSuccess") : ReadConfig.getInt("pkThroughFail");
            } else
                mark = calUserMark(uid, type, mark, allFinish, users, isPk, lose, matchKey, service);
            userScore.put(uid, mark);
            if (gameMode == 1)
                break;
            if (isPk)
                service.setMatchRecord(uid, mark > 0);
            else
                service.setMatchRecord(uid, allFinish);
            //记录胜场
            if (isPk && mark > 0)
            {
                Ranking.addPkSuccess(uid, gameCode);
            }
        }

        if (!isPk || type != 1)
        {
            //添加分数
            if (!isPk || userScore.size() > 0)
                service.zAdd(isPk, userScore);
        }
        JSONArray usersArray = new JSONArray();
        String finalMatchKey = matchKey;
        userScore.forEach((k, v) ->
        {
            JSONObject user = new JSONObject();
            user.put("uid", k);
            user.put("mark", v);
            JSONObject rank = Ranking.getRankingData(k, finalMatchKey);
            if (rank != null)
            {
                user.put("highMark", rank.get("mark"));
                user.put("index", rank.get("index"));
            }
            usersArray.add(user);
        });
        JSONObject result = new JSONObject();
        result.put("result", "success");
        result.put("users", usersArray);
        return result;
    }

    /**
     * * 计算排名分数
     *
     * @param mark      上报分数
     * @param type      游戏类型
     * @param allFinish 通关标志
     * @param users     用户信息
     * @param isPk      模式
     * @param lose      复活次数
     * @param service   排名服
     * @return 用户真实得分
     */
    private int calUserMark(String uid, int type, int mark, boolean allFinish, JSONArray users, boolean isPk, int lose, String matchKey, RankingService service)
    {
        if (isPk)
        {
            if (mark != 1)
                return 0;
            //邀请不做排行，只做基础分
            if (type == 1)
                return 50;
            //公式 (基础分+加层分)*n%
            int base = 50;
            //获取相差分
            Set<String> keys = new HashSet<>();
            long other = 0, own = 0;
            for (int i = users.size() - 1; i >= 0; i--)
            {
                JSONObject object = users.getJSONObject(i);
                String temp = object.getString("uid");
                keys.add(temp);
                Double rank = Ranking.getRankingScore(temp, matchKey);
                if (uid.equals(temp))
                    own = rank.longValue();
                else
                    other = rank.longValue();
            }
            long same = service.getSamePlayCount(keys);
            BigDecimal n = new BigDecimal(1).subtract(new BigDecimal(same * 0.3));
            if (n.compareTo(new BigDecimal(0)) < 0)
                n = new BigDecimal(0);
            int add = 10;
            long sub = own - other;
            if (sub < -50)
                add = 50;
            else if (sub < 0)
                add = 30;
            else if (sub < 50)
                add = 20;
            return new BigDecimal(base + add).multiply(n).intValue();
        }
        if (lose > 100)
            lose = 100;
        double sub = (1.1 - 0.003 * lose) * mark;
        if (sub < 0)
            sub = 0;
        //非pk模式
        if (allFinish)
        {
            if (type == 1 && users.size() > 1)
                return new BigDecimal(sub).multiply(BigDecimal.valueOf(1.2)).intValue();
        }
        return (int) sub;
    }
}
