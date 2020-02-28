package servlet.login.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import pipe.PeConfigGames;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 2020.02.21 第二版小程序入口
 *
 * @author xuwei
 */
@WebServlet(urlPatterns = "/season")
public class CmServletNewPing extends CmServletMain {
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
        result.put("result", "success");
        //获取合集信息
        String appId = requestPackage.getString("appId");
        String version = requestPackage.getString("version");
        JSONArray seasonType = requestPackage.getJSONArray("seasonType");
        for (int i = 0; i < seasonType.size(); i++) {
            String type = seasonType.getString(i);
            switch (type) {
                case "match": {
                    result.put("match", PeConfigGames.gainGamesExtMessage(appId, version));
                }
                break;
                case "regular": {
                    result.put("regular", PeConfigGames.gainGamesSetMessage(appId, version));
                }
                break;
                case "user": {
                    String ddUid = requestPackage.getString("uid");
                    if (ddUid != null && !"0".equals(ddUid)) {
                        JSONObject user = new JSONObject();
                        UserService.putUserValue(user, ddUid);
                        result.put("user", user);
                    }
                }
                break;
                default:
                    break;
            }
        }
        result.put("match-save-host", ReadConfig.get("match-save-host"));
        result.put("serverTime", System.currentTimeMillis());
        return result;
    }
}
