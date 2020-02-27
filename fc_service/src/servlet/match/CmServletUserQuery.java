package servlet.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.match.Ranking;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 进行查询用户信息
 */
@WebServlet(urlPatterns = "/query/user")
public class CmServletUserQuery extends CmServletMain
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
        JSONArray userList = requestPackage.getJSONArray("userList");
        JSONObject result = JSONObject.parseObject("{\"result\":\"success\"}");
        if (userList == null)
            return result;
        JSONArray rankArray = Ranking.globalRanking(userList);
        result.put("ranks", rankArray);
        return result;
    }
}
