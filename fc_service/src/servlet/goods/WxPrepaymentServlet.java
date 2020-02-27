package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.PayService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/pay/prePay", name = "prepayment wx")
public class WxPrepaymentServlet extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String appId = requestPackage.getString("appId");
        PayService service = new PayService(appId);
        return service.prePay(requestPackage);
    }
}
