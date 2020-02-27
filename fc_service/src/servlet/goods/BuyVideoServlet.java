package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import servlet.CmServletMain;
import tool.CmTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@WebServlet(urlPatterns = "/buy/video", name = "buy video")
public class BuyVideoServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        String ddUid = content.getString("uid");
        String appId = content.getString("appId");
        String type = content.getString("type");
        JSONObject result = new JSONObject();
        if (ddUid == null || ddUid.isEmpty())
        {
            result.put("result", "success");
            result.put("free_relive", 10);
            result.put("free_game", 10);
            return result;
        }
        UserService.VideoUse use = UserService.userVideoUse(ddUid);

        result.put("result", "success");
        switch (type)
        {
            case "relive":
            {
                use.relive--;
                if (use.relive < 0)
                {
                    result.put("result", "fail");
                    return result;
                }
                //游戏开局
                int userValue = UserService.getValue(ddUid, "coin");
                //游戏编号
                String gameCode = content.getString("gameCode");
                if (gameCode == null)
                    gameCode = String.valueOf(use.relive);
                UserService.addHistoryCost(ddUid, appId, "video", userValue, userValue, 1, type, gameCode);
            }
            break;
            case "game":
            {
                use.game--;
            }
            break;
        }
        result.put("free_relive", use.relive);
        result.put("free_game", use.game);
        if (use.relive < 0 || use.game < 0)
        {
            result.put("result", "fail");
        }
        if (use.relive < 0)
            use.relive = 0;
        if (use.game < 0)
            use.game = 0;
        UserService.setCache(ddUid, "video", CmTool.getJSONByFastJSON(use));

        return result;
    }

}
