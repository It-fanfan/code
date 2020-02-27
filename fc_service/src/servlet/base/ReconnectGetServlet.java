package servlet.base;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbGame;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 进行重连响应
 */
@WebServlet(urlPatterns = "/reconnectGet")
public class ReconnectGetServlet extends CmServletMain
{
    /**
     * 街机服务器进行与客户端额外重连处理
     * 类型：
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        JSONObject result = new JSONObject();
        result.put("result", "success");
        String type = requestObject.getParameter("type");
        switch (type)
        {
            //二维码请求
            case "rq":
            {
                int gameCode = requestPackage.getInteger("gameCode");
                PeDbGame game = PeDbGame.getGameFast(gameCode);
                if (game != null)
                {
                    result.put("friendUrl", game.ddFriendUrl);
                }
            }
            break;
        }
        return result;
    }
}
