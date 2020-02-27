package work;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import pipe.PeConfigGames;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author feng
 */
@WebServlet(urlPatterns = "/ping", name = "ping")
public class CmServletPing extends CmServletMain
{

    private static final long serialVersionUID = -2811864936687157528L;

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
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        PeConfigGames.gainGamesMessage(result, requestPackage);
        String ddUid = requestPackage.getString("uid");
        if (ddUid != null && !"0".equals(ddUid))
        {
            JSONObject user = new JSONObject();
            UserService.putUserValue(user, ddUid);
            result.put("user", user);
        }
        result.put("serverTime", System.currentTimeMillis());
        result.put("match-save-host", ReadConfig.get("match-save-host"));
        return result;
    }

}
