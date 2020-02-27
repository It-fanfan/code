package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import servlet.CmServletMain;
import tool.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@WebServlet(urlPatterns = "/buy/game", name = "buy game")
public class BuyGameServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        //用户信息
        String ddUid = content.getString("uid");
        //游戏金币
        int coin = content.getInteger("coin");
        String appId = content.getString("appId");
        JSONObject result = new JSONObject();
        result.put("result", "success");
        //是否视频免费
        boolean free = false;
        try
        {
            JSONObject extra = new JSONObject();
            extra.put("type", "gameEnter");
            extra.put("appId", appId);
            extra.put("extra", String.valueOf(content.getInteger("gameCode")));
            if (content.containsKey("free"))
                free = content.getBoolean("free");
            //用户金币数
            int userValue = UserService.getValue(ddUid, "coin");
            if (free)
            {
                result.put("coin", userValue);
                //游戏开局
                UserService.addHistoryCost(ddUid, extra.getString("appId"), "video", userValue, userValue, 1, extra.getString("type"), extra.getString("extra"));
                return result;
            }
            long state;
            if (userValue < coin || (state = UserService.addValue(ddUid, "coin", -coin, extra)) < 0)
            {
                result.put("coin", userValue);
                result.put("message", "当前金币不足");
                return result;
            }
            result.put("coin", state);
        } catch (Exception e)
        {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
        }
        return result;
    }
}
