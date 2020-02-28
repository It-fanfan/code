package servlet.config;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/user/recharge")
public class CmServletRecharge extends CmServletMain
{
    @Override
=======
@WebServlet(urlPatterns = "/user/recharge")
public class CmServletRecharge extends CmServletMain
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        return new JSONObject();
    }
}
