package com.code.servlet.goods;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.goods.Shop;
import com.code.service.goods.ShopService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/shop/list", name = "com.code.protocols.goods.Shop.List")
public class ShopListServlet extends ServletMain<Shop.ListRequestImpl, Shop.ListResponseImpl>
{
    @Override
    protected Shop.ListResponseImpl doLogic(Shop.ListRequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        Shop.ListResponseImpl res = new Shop.ListResponseImpl();
        res.code = STATUS_SUCCESS;
        ShopService service = new ShopService(userCache);
        res.currenttime = System.currentTimeMillis();
        res.dailygoods = service.getDailyGoods();
        res.valuegoods = service.getValueGoods();
        return res;
    }
}
