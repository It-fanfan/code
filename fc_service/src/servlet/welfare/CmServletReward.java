package servlet.welfare;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import service.WelfareService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/welfare/reward", name = "welfare reward")
public class CmServletReward extends CmServletMain
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
        String appId = requestPackage.getString("appId");
        JSONObject result = new JSONObject();
        result.put("result", "success");
        WelfareService service = new WelfareService(uid, appId);
        //进行签到
        service.receive(result, requestPackage);
        UserService.putUserValue(result, uid);
        return result;
    }
}
