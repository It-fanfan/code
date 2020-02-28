package com.code.servlet.pay.huawei;

import com.annotation.AvoidRepeatableCommit;
import com.code.dao.sdk.RecordPay;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.sdk.Huawei;
import com.code.service.pay.HuaweiService;
import com.code.servlet.base.ServletMain;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/pay/huawei/sign", name = "huawei sign")
public class HuaSignServlet extends ServletMain<Huawei.Pay, Huawei.PaySignResponse>
{
    protected Huawei.PaySignResponse doLogic(Huawei.Pay pay, HttpServletRequest request, HttpServletResponse response)
    {
        Huawei.PaySignResponse res = new Huawei.PaySignResponse();
        res.sign = HuaweiService.getSign(pay);
        //内部调用逻辑
        try
        {
            RecordPay recordPay = new RecordPay();
            recordPay.setGoodsId(pay.goodsid);
            recordPay.setInsertTime(new Timestamp(System.currentTimeMillis()));
            recordPay.setMoney(new BigDecimal(pay.amount));
            recordPay.setOrderId(pay.requestId);
            recordPay.setPayType(LoginRequest.Platform.huawei.name());
            recordPay.setPlatform(pay.platform.name());
            recordPay.setUserId(Integer.valueOf(pay.userid));
            FishInfoDao.saveOrUpdate(recordPay, true);
        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e));
        }
        return res;
    }
}
