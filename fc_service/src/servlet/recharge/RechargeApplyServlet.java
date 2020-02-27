package servlet.recharge;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbRecharge;
import db.PeDbUser;
import service.PayService;
import service.UserService;
import servlet.CmServletMain;
import tool.CmTool;
import tool.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 提现申请api
 *
 * @author Sky
 */
@WebServlet(urlPatterns = "/apply/recharge")
public class RechargeApplyServlet extends CmServletMain
{

    /**
     *
     */
    private static final long serialVersionUID = -6882397790156865843L;


    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        //用户信息
        String ddUid = content.getString("uid");
        PeDbUser user = PeDbUser.gainUserObject(ddUid);
        //获取上一次提现时间
        String lastRecord = user.ddAwardList;
        //用户当前人民币
        int rmb = UserService.getValue(ddUid, "rmb");
        BigDecimal userValue = new BigDecimal(rmb).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal recharge = content.getBigDecimal("rmb");
        if (userValue.compareTo(recharge) < 0)
        {
            result.put("result", "fail");
            //人民币不足
            result.put("message", "rmb is enough");
            return result;
        }
        int day = CmTool.getCurrentDataValueRough();
        if (lastRecord != null && lastRecord.contains(String.valueOf(day)))
        {
            result.put("result", "fail");
            //人民币不足
            result.put("message", "count limit");
            return result;
        }
        try
        {
            user.ddAwardList = String.valueOf(day);
            user.update("ddAwardList");
            result.put("recharge", user.ddAwardList);
            PeDbRecharge record = PayService.createRecharge(content);
            UserService.setCache(ddUid, "recharge", user.ddAwardList);
            JSONObject extra = new JSONObject();
            extra.put("type", "recharge");
            //复活次数
            extra.put("appId", record.ddAppId);
            long current = UserService.addValue(ddUid, "rmb", -recharge.multiply(new BigDecimal(100)).intValue(), extra);
            if(current < 0)
            {
                result.put("result", "fail");
                //人民币不足
                result.put("message", "count limit");
                return result;
            }
            result.put("rmb", current);
            result.put("result", "success");
            //人民币不足
            if (record.ddRechargeOpenId != null)
                result.put("message", "申请提现成功，请等待运营人员审核");
            else
            {
                result.put("message", "当前提现已经申请，提现码为" + record.ddId);
                result.put("rechargeId", record.ddId);
            }
            return result;
        } catch (Exception e)
        {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
        }
        return result;
    }
}
