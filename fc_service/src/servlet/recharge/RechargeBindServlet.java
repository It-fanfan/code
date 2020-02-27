package servlet.recharge;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbRecharge;
import db.PeDbWxConfig;
import service.UserService;
import service.match.Ranking;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(urlPatterns = "/recharge/bindOrQuery")
public class RechargeBindServlet extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        String uid = content.getString("uid");
        String rechargeId = content.getString("rechargeId");
        String appId = content.getString("appId");
        boolean query = content.getBoolean("query");
        String openId = UserService.getOpenId(uid, appId);
        //进行转义
        BigInteger amount = new BigInteger(rechargeId, 16);
        String id = amount.toString();
        PeDbRecharge record = PeDbRecharge.searchRecharge(id);
        if (openId == null)
        {
            result.put("result", "fail");
            result.put("message", "用户未授权，请授权后再来");
            return result;
        }
        if (record == null || record.ddStatus != 1)
        {
            result.put("result", "fail");
            result.put("message", "当前提现码已过期，请检查后再处理");
            return result;
        }
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        result.put("recharge", PeDbRecharge.getJsonObject(format, record));
        if (record.ddUid != null)
        {
            List<String> users = Ranking.getUserCollects(record.ddUid);
            if (users != null)
            {
                JSONArray array = new JSONArray();
                for (String json : users)
                {
                    array.add(JSONObject.parseObject(json));
                }
                result.put("users", array);
            }
        }
        if (query)
        {
            result.put("result","success");
            return result;
        }
        PeDbWxConfig wxConfig = PeDbWxConfig.getConfigFast(appId);
        if (wxConfig == null || wxConfig.ddMchId == null)
        {
            result.put("result", "fail");
            result.put("message", "当前appId无法绑定提现参数");
            return result;
        }
        record.ddStatus = 0;
        record.ddRechargeOpenId = openId;
        record.ddRechargeAppId = appId;
        record.update("ddStatus#ddRechargeOpenId#ddRechargeAppId");
        result.put("result","success");
        return result;
    }
}
