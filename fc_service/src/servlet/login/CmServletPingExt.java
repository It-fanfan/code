package servlet.login;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import db.PeDbAppConfig;
import pipe.PeConfigGames;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/pingExt")
public class CmServletPingExt extends CmServletMain {
    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        JSONObject result = new JSONObject();
        String appId = requestPackage.getString("appId");
        String version = requestPackage.getString("version");
        //检测分支
        PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
        switch (appConfig.ddProgram) {
            //小程序
            case 1: {
                result.putAll(PeConfigGames.gainGamesExtMessage(appId, version));
            }
            break;
            //小游戏
            case 0:
                //公众号
            case 2:
            default: {
                result.putAll(PeConfigGames.gainGamesSetMessage(appId, version));
            }
            break;
        }
        String ddUid = requestPackage.getString("uid");
        if (ddUid != null && !"0".equals(ddUid)) {
            JSONObject user = new JSONObject();
            UserService.putUserValue(user, ddUid);
            result.put("user", user);
        }
        result.put("serverTime", System.currentTimeMillis());
        result.put("match-save-host", ReadConfig.get("match-save-host"));
        result.put("result", "success");
        return result;
    }
}
