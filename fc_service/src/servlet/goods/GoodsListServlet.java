package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbGoodsValue;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/goodsList")
public class GoodsListServlet extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        String appId = requestPackage.getString("appId");
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        result.put("goodsList", PeDbGoodsValue.getGamesFast(appId));
        SERVLET_LOG.info(result.toJSONString());
        return result;
    }
}
