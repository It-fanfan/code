package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbUser;
import service.PayService;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/pay/callback")
public class WxCallbackServlet extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String appId = requestPackage.getString("appId");
        PayService service = new PayService(appId);
        JSONObject result = service.wxClientCallback(requestPackage);
        if (result.getString("result").equals("success"))
        {
            String ddUid = requestPackage.getString("uid");
            UserService.putUserValue(result,ddUid);
        }
        return result;
    }
}
