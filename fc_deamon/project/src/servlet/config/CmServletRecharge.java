package servlet.config;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/user/recharge")
public class CmServletRecharge extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        return new JSONObject();
    }
}
