package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.PayService;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/pay/callback")
public class WxCallbackServlet extends CmServletMain {
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        String resultStr = "result", successStr = "success";
        String appId = requestPackage.getString("appId");
        PayService service = new PayService(appId);
        JSONObject result = service.wxClientCallback(requestPackage);
        if (result.getString(resultStr).equals(successStr)) {
            String ddUid = requestPackage.getString("uid");
            UserService.putUserValue(result, ddUid);
        }
        return result;
    }
}
