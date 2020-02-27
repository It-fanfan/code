package servlet.goods;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 购买复活次数
 */
@WebServlet(urlPatterns = "/buy/relive", name = "buy relive")
public class BuyReliveServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        //用户信息
        String ddUid = content.getString("uid");
        //复活次数
        int relive = content.getInteger("relive");
        int coin = content.getInteger("coin");
        //用户金币数
        int userValue = UserService.getValue(ddUid, "coin");
        //参数判断
        //        int actual = (int) (100 * Math.pow(1.3, relive));
        JSONObject result = new JSONObject();
        //        if (actual != coin)
        //        {
        //            result.put("result", "fail");
        //            result.put("message", "参数不匹配");
        //            return result;
        //        }
        JSONObject extra = new JSONObject();
        extra.put("type", "relive");
        //复活次数
        extra.put("extra", String.valueOf(relive));
        extra.put("appId", content.getString("appId"));
        long state;
        if (userValue < coin || (state = UserService.addValue(ddUid, "coin", -coin, extra)) < 0)
        {
            result.put("result", "fail");
            result.put("coin", userValue);
            result.put("message", "当前金币不足");
            return result;
        }
        result.put("result", "success");
        result.put("coin", state);
        return result;
    }

}