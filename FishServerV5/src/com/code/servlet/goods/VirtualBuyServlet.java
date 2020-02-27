package com.code.servlet.goods;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.LoginCode;
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
@WebServlet(urlPatterns = "/midas/buy", name = "com.code.protocols.goods.Shop.Virtual")
public class VirtualBuyServlet extends ServletMain<Shop.VirtualBuyRequstImpl, Shop.BuyResponseImpl>
{
    @Override
    protected Shop.BuyResponseImpl doLogic(Shop.VirtualBuyRequstImpl req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserCache userCache = getUserCache(request);
        Shop.BuyResponseImpl res = new Shop.BuyResponseImpl();
        ShopService service = new ShopService(userCache);
        LoginCode loginCode = getLoginCode(request);
        if (UnifiedOrderServlet.existPlatform(loginCode, res))
            return res;
        OperatorBase.ERROR error = service.buyVirtual(req.orderid, req.success, loginCode);
        OperatorBase.updateResponseCode(error, res);
        if (error == OperatorBase.ERROR.SUCCESS && req.success)
        {
            res.setUserValue(userCache);
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(OperatorBase.ActivityType.shop, 1);
            }
            //新增商店成就
            new AchievementService(userCache).addAchievement(AchievementType.StoreDeals, 1);
        }
        return res;
    }
}
