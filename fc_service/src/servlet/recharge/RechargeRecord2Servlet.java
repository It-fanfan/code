package servlet.recharge;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbObject;
import db.PeDbRecharge;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

@WebServlet(urlPatterns = "/recharge/record2")
public class RechargeRecord2Servlet extends CmServletMain
{

    /**
     *
     */
    private static final long serialVersionUID = 6651755056813891659L;

    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        String uid = content.getString("uid");
        String appId = content.getString("appId");

        String openId = UserService.getOpenId(uid, appId);
        if (openId != null)
        {
            Vector<PeDbObject> list = PeDbRecharge.getDbRechargeList(openId);
            if (list != null)
            {
                result.put("apply", PeDbRecharge.getMessage(list));
            }
            result.put("result", "success");
        } else
        {
            result.put("result", "fail");
            result.put("message", "无法查询用户信息");
        }
        return result;
    }


}
