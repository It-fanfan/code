package com.code.servlet.recover;


import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.recover.OrderFish;
import com.code.protocols.core.recover.OrderTakingExt.RequestImpl;
import com.code.protocols.core.recover.OrderTakingExt.ResponseImpl;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.angling.FishDataService;
import com.code.service.order.Order;
import com.code.service.order.OrderService;
import com.code.service.order.UserOrder;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.code.protocols.core.AnglingBase.*;

/**
 * 处理接单
 */
@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/order/taking", name = "com.code.protocols.core.recover.OrderTakingExt")
public class OrderTakingServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    /**
     * 获取最新订单数据
     *
     * @return 最新订单
     */
    static OrderInfo getLatestOrder(OrderService service, UserOrder userOrder, int index)
    {
        Order order = service.getOrderByIndex(userOrder, index);
        Vector<OrderFish> fishes = new Vector<>();
        OrderInfo info = new OrderInfo();
        info.order = FishOrderInitServlet.createFishOrder(order, index, fishes);
        info.orderfishes = BigData.getBigData(fishes, OrderFish.class);
        return info;
    }

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        UserCache userCache = getUserCache(request);
        res.status = true;
        res.code = STATUS_SUCCESS;
        OrderService service = new OrderService(userCache);
        try
        {
            UserOrder userOrder = service.getUserOrder();
            if (System.currentTimeMillis() - userOrder.lastFinish <= Systemstatusinfo.getInt("order_finish_cd"))
            {
                res.code = ERROR.NO_CD.getCode();
                res.msg = ERROR.NO_CD.getMsg();
                return res;
            }
            Order order = service.getOrderByIndex(userOrder, req.index);
            if (order == null)
            {
                res.code = ERROR.NO_ORDER.getCode();
                res.msg = ERROR.NO_ORDER.getMsg();
                return res;
            }
            if (OrderService.getRefreshPearl(order.lastRefresh) > 0)
            {
                res.code = ERROR.NO_CD.getCode();
                res.msg = ERROR.NO_CD.getMsg();
                return res;
            }
            int orderGet = order.price;
            ERROR error = service.setOrderResult(req.index, userOrder);
            if (error.getCode() == STATUS_SUCCESS)
            {
                res.setUserValue(userCache);
                res.order = getLatestOrder(service, userOrder, req.index);
                try (FishDataService dataService = new FishDataService(userCache))
                {
                    res.userfishes = BigData.getBigData(dataService.getUserFishes(), FishResult.class);
                }
                res.finish = String.valueOf(userOrder.lastFinish);
                //添加活跃进度
                Map<OperatorBase.ActivityType, Integer> process = new HashMap<>();
                process.put(OperatorBase.ActivityType.order, 1);
                process.put(OperatorBase.ActivityType.orderGet, orderGet);
                try (WorkService workService = new WorkService(userCache))
                {
                    workService.addProcess(process);
                }

                //添加成就
                new AchievementService(userCache).addAchievement(AchievementType.Order, 1);
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
