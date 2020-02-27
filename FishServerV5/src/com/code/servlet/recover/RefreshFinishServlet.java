package com.code.servlet.recover;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.AnglingBase;
import com.code.service.order.Order;
import com.code.service.order.OrderService;
import com.code.service.order.UserOrder;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.core.recover.RefreshFinish.RequestImpl;
import static com.code.protocols.core.recover.RefreshFinish.ResponseImpl;

/**
 * 立即完成刷新
 */
@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/order/refreshFinish", name = "com.code.protocols.core.recover.RefreshFinish")
public class RefreshFinishServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        UserCache userCache = getUserCache(request);
        OrderService service = new OrderService(userCache);
        UserOrder userOrder = service.getUserOrder();
        Order order = service.getOrderByIndex(userOrder, req.index);
        int cost = OrderService.getRefreshPearl(order.lastRefresh);
        if (req.cost < cost)
        {
            res.cost = cost;
            AnglingBase.updateResponseCode(AnglingBase.ERROR.TRY_AGAIN, res);
            return res;
        }
        if (cost > 0)
        {
            if (userCache.incrPearl(-cost))
            {
                AnglingBase.updateResponseCode(AnglingBase.ERROR.PEARL_LACKING, res);
                return res;
            }
        }
        AnglingBase.updateResponseCode(AnglingBase.ERROR.SUCCESS, res);
        order.lastRefresh = 0;
        OrderService.putUserOrder(userCache, userOrder);
        res.setUserValue(userCache);
        return res;
    }
}
