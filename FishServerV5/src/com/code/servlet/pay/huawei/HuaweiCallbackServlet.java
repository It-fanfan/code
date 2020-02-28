package com.code.servlet.pay.huawei;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.sdk.RecordPay;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.sdk.Huawei;
import com.code.service.goods.ShopService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/pay/huawei/callback", name = "huawei pay callback")
public class HuaweiCallbackServlet extends ServletMain<Huawei.PayRequestCallback, Huawei.PayResponseCallback>
{
    protected Huawei.PayResponseCallback doLogic(Huawei.PayRequestCallback pay, HttpServletRequest request, HttpServletResponse response)
    {
        Huawei.PayResponseCallback res = new Huawei.PayResponseCallback();
        UserCache userCache = getUserCache(request);
        RecordPay recordPay = FishInfoDao.instance().findById(RecordPay.class, new String[]{"orderId", pay.requestid});
        if (recordPay != null)
        {
            if (recordPay.getTransTime() == null)
            {
                recordPay.setPayOrder(pay.orderid);
                recordPay.setState(pay.success);
                recordPay.setTransTime(new Timestamp(System.currentTimeMillis()));
                if (pay.success)
                {
                    ShopService service = new ShopService(userCache);
                    service.buySuccess(recordPay.getGoodsId());
                } else
                {
                    recordPay.setPayError(pay.errormsg);
                }
                FishInfoDao.saveOrUpdate(recordPay);
            }
        }
        if (pay.success)
            res.setUserValue(userCache);
        return res;
    }
}
