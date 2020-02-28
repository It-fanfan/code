package com.code.servlet.recover;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase.OrderIntroduced;
import com.code.protocols.core.recover.FishOrderInitExt.RequestImpl;
import com.code.protocols.core.recover.FishOrderInitExt.ResponseImpl;
import com.code.protocols.core.recover.OrderFish;
import com.code.protocols.login.LoginRequest;
import com.code.service.order.Order;
import com.code.service.order.OrderService;
import com.code.service.order.UserOrder;
import com.code.service.order.VideoService;
import com.code.servlet.base.ServletMain;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

/**
 * 订单初始化
 */
@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/order/init", name = "com.code.protocols.core.recover.FishOrderInitExt")
public class FishOrderInitServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    /**
     * 進行創建訂單魚
     *
     * @param order  訂單
     * @param index  索引
     * @param fishes 鱼数据
     * @return 订单鱼
     */
    static OrderIntroduced createFishOrder(Order order, int index, Vector<OrderFish> fishes)
    {
        if (order == null)
            return null;
        OrderIntroduced introduce = new OrderIntroduced();
        introduce.bossid = order.bossId;
        introduce.introduce = order.introduce;
        introduce.rewards = order.rewards;
        introduce.price = order.price;
        introduce.index = index;
        introduce.refresh = String.valueOf(order.lastRefresh);
        order.fishes.forEach((key, value) ->
        {
            value.index = index;
            fishes.add(value);
        });
        return introduce;
    }

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        OrderService service = new OrderService(userCache);
        try
        {
            UserOrder userOrder = service.getUserOrder();
            res.finish = String.valueOf(userOrder.lastFinish);
            res.orders = new Vector<>();
            Vector<OrderFish> fishes = new Vector<>();
            int index = 0;
            for (Order order : userOrder.orders)
            {
                res.orders.add(createFishOrder(order, index++, fishes));
            }
            res.fishes = BigData.getBigData(fishes, OrderFish.class);
            if (LoginRequest.Platform.huawei != req.platform)
                res.ordervideo = new VideoService(userCache).getOrderVideo();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        res.code = STATUS_SUCCESS;
        return res;
    }
}
