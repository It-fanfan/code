package com.code.servlet.goods;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.goods.GoodsValue;
import com.code.protocols.AbstractResponse;
import com.code.protocols.LoginCode;
import com.code.protocols.goods.UnifiedOrder;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.midas.GetBalance;
import com.code.protocols.operator.OperatorBase;
import com.code.service.goods.ShopService;
import com.code.service.trade.MarketService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.operator.OperatorBase.ERROR.*;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/midas/unifiedOrder", name = "com.code.protocols.goods.UnifiedOrder")
public class UnifiedOrderServlet extends ServletMain<UnifiedOrder.RequestImpl, UnifiedOrder.ResponseImpl>
{

    /**
     * 检测平台
     *
     * @param loginCode 登录态
     * @param res       下发资源
     */
    static boolean existPlatform(LoginCode loginCode, AbstractResponse res)
    {
        if (loginCode.getPlatform() != LoginRequest.Platform.wechat)
        {
            OperatorBase.updateResponseCode(PLATFORM_NONSUPPORT, res);
            return true;
        }
        return false;
    }

    @Override
    protected UnifiedOrder.ResponseImpl doLogic(UnifiedOrder.RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        UnifiedOrder.ResponseImpl res = new UnifiedOrder.ResponseImpl();
        LoginCode loginCode = getLoginCode(request);
        if (existPlatform(loginCode, res))
            return res;
        MarketService.WealthType wealthType;
        int value;
        GoodsValue goods = ShopService.getGoodsValue(req.goodsid);
        if (goods != null)
        {
            res.virtualvalue = goods.getVirtualValue() * req.total;
            wealthType = MarketService.WealthType.valueOf(goods.getWealthType());
            value = goods.getTotal() * req.total;
        } else
        {
            OperatorBase.updateResponseCode(UNDEFINE_GOODS, res);
            return res;
        }
        ShopService service = new ShopService(userCache);
        GetBalance.ResponseImpl balance = service.getBalance(loginCode.weChatAppId(), loginCode.weChatSessionKey());
        if (balance != null && balance.status && balance.code == SUCCESS.getCode() && balance.errcode == 0)
        {
            res.currentvirtual = balance.balance;
            res.first = balance.first_save;
            res.orderid = service.getVirtualOrder(wealthType,goods.getId(), value, res.virtualvalue, balance);
            OperatorBase.updateResponseCode(SUCCESS, res);
            return res;

        } else if (balance == null)
        {
            OperatorBase.updateResponseCode(COIN_UNENOUGH, res);
            return res;
        } else
        {
            res.code = balance.errcode;
            res.msg = balance.errmsg;
            return res;
        }

    }
}
