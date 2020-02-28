package servlet.recharge;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbGoodsValue;
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
 * @author xuwei
 */
@WebServlet(urlPatterns = "/apply/recharge2")
public class RechargeApply2Servlet extends CmServletMain
{

    /**
     *
     */
    private static final long serialVersionUID = -6882397790156865843L;


    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        JSONObject result = new JSONObject();
        //用户信息
        String ddUid = content.getString("uid");
        //获取提现配置列表
        int goodsId = content.getInteger("goodsId");
        String appId = content.getString("appId");
        PeDbGoodsValue goods = PeDbGoodsValue.getGoodsFast(goodsId);
        String goodsType = "recharge";
        if (goods == null || !goods.ddState || !goodsType.equals(goods.ddGoodsType))
        {
            result.put("result", "fail");
            //人民币不足
            result.put("message", "invalid recharge");
            return result;
        }
        //需要消耗的参数
        String costType = goods.ddCostType;
        BigDecimal price = goods.ddPrice;
        price = price.multiply(new BigDecimal(100));
        //获取的数量
        int rmb = goods.ddValue;
        //检测玩家货币是否充裕
        int value = UserService.getValue(ddUid, costType);
        BigDecimal userValue = new BigDecimal(value);
        if (userValue.compareTo(price) < 0)
        {
            result.put("result", "fail");
            //人民币不足
            result.put("message", "rmb is enough");
            return result;
        }
        PeDbUser user = PeDbUser.gainUserObject(ddUid);
        //获取上一次提现时间
        String lastRecord = user.ddAwardList;
        //检测玩家提现次数是否充裕
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
            JSONObject extra = new JSONObject();
            extra.put("type", "recharge");
            //复活次数
            extra.put("appId", appId);
            //进行扣除消耗
            long current = UserService.addValue(ddUid, costType, -price.intValue(), extra);
            if (current < 0)
            {
                result.put("result", "fail");
                //人民币不足
                result.put("message", "rmb is enough");
                return result;
            }
            user.ddAwardList = String.valueOf(day);
            user.update("ddAwardList");
            result.put("recharge", user.ddAwardList);
            PeDbRecharge record = PayService.createRecharge(ddUid, new BigDecimal(rmb).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP), appId);
            UserService.setCache(ddUid, "recharge", user.ddAwardList);
            result.put("rmb", current);
            result.put("result", "success");
            //人民币不足
            if (record.ddRechargeOpenId != null)
            {
                result.put("message", "申请提现成功，请等待运营人员审核");
            } else
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
