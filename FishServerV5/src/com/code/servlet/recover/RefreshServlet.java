package com.code.servlet.recover;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.recover.RefreshExt.RequestImpl;
import com.code.protocols.core.recover.RefreshExt.ResponseImpl;
import com.code.service.order.Order;
import com.code.service.order.OrderService;
import com.code.service.order.UserOrder;
import com.code.servlet.base.ServletMain;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.core.AnglingBase.ERROR;

/**
 * 刷新单个接单
 */
@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/order/refresh", name = "com.code.protocols.core.recover.RefreshExt")
public class RefreshServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        UserCache userCache = getUserCache(request);
        res.code = STATUS_SUCCESS;
        OrderService service = new OrderService(userCache);
        try
        {
            UserOrder userOrder = service.getUserOrder();
            Order order = service.getOrderByIndex(userOrder, req.index);
            if (order != null)
            {
                //刷新索引
                if (OrderService.getRefreshPearl(order.lastRefresh) > 0)
                {
                    res.code = ERROR.NO_CD.getCode();
                    res.msg = ERROR.NO_CD.getMsg();
                    return res;
                }
            }
            ERROR error = service.refreshOrder(userOrder, req.index);
            if (error.getCode() == STATUS_SUCCESS)
            {
                res.order = OrderTakingServlet.getLatestOrder(service, userOrder, req.index);
            } else
            {
                res.code = error.getCode();
                res.msg = error.getMsg();
                return res;
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return res;
    }
}
