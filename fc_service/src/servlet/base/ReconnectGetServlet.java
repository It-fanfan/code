package servlet.base;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
<<<<<<< HEAD
import db.PeDbConfigConfirm;
=======
import db.PeDbGame;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
<<<<<<< HEAD
import java.io.Serializable;

/**
 * 进行重连响应
 *
 * @author xuwei
 */
@WebServlet(urlPatterns = "/reconnectGet")
public class ReconnectGetServlet extends CmServletMain implements Serializable {
=======

/**
 * 进行重连响应
 */
@WebServlet(urlPatterns = "/reconnectGet")
public class ReconnectGetServlet extends CmServletMain
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    /**
     * 街机服务器进行与客户端额外重连处理
     * 类型：
     */
<<<<<<< HEAD
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        JSONObject result = new JSONObject();
        result.put("result", "success");
        String type = requestPackage.getString("type");
        switch (type) {
            //二维码请求
            case "rq": {
                PeDbConfigConfirm confirm = PeDbConfigConfirm.getConfirm(type);
                if (confirm != null) {
                    result.put("friendUrl", confirm.ddStatus ? confirm.ddYes : confirm.ddNo);
                    result.put("groupStatus", confirm.ddStatus);
                }
            }
            break;
            default:
                break;
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        }
        return result;
    }
}
