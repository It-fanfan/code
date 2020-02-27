package servlet.recharge;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbObject;
import db.PeDbRecharge;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

@WebServlet(urlPatterns = "/recharge/record", name = "recharge record")
public class RechargeRecordServlet extends CmServletMain
{

    /**
     *
     */
    private static final long serialVersionUID = 6651755056813891659L;

    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        String uid = content.getString("uid");
        result.put("result", "success");
        Vector<PeDbObject> list = PeDbRecharge.getDbRecharge(uid);
        if (list != null)
        {
            result.put("apply", PeDbRecharge.getMessage(list));
        }
        return result;
    }
}
