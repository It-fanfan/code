package com.code.servlet.goods;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.goods.Shop;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.goods.ShopService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/shop/buy", name = "com.code.protocols.goods.Shop.ShopBuy")
public class ShopBuyServlet extends ServletMain<Shop.BuyRequestImpl, Shop.BuyResponseImpl>
{
    @Override
    protected Shop.BuyResponseImpl doLogic(Shop.BuyRequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        Shop.BuyResponseImpl res = new Shop.BuyResponseImpl();
        ShopService service = new ShopService(userCache);
        if (service.buyGoods(req.goodstype, req.goodsid, req.total))
        {
            res.setUserValue(userCache);
            OperatorBase.updateResponseCode(OperatorBase.ERROR.SUCCESS, res);
            //新增商店成就
            new AchievementService(userCache).addAchievement(AchievementType.StoreDeals, 1);
            //进度更新
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(OperatorBase.ActivityType.shop, 1);
            }
        } else
        {
            OperatorBase.updateResponseCode(OperatorBase.ERROR.FAIL_BUY, res);
        }
        return res;
    }
}
