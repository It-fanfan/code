package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
<<<<<<< HEAD
=======
import db.PeDbUser;
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
import service.PayService;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

<<<<<<< HEAD
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
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        }
        return result;
    }
}
