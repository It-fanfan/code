package com.code.servlet.user;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.user.UiExist;
import com.code.service.message.NoticeService;
import com.code.service.order.OrderService;
import com.code.service.ui.InviteService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/ui/exist", name = "com.code.protocols.user.UiExist")
public class UIExistServlet extends ServletMain<UiExist.RequestImpl, UiExist.ResponseImpl>
{
    @Override
    protected UiExist.ResponseImpl doLogic(UiExist.RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        UiExist.ResponseImpl res = new UiExist.ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        res.flags = new HashMap<>();
        for (UiExist.UIType type : req.ui)
        {
            boolean value = false;
            switch (type)
            {
                case order:
                    value = new OrderService(userCache).uiFlag();
                    break;
                case invite:
                    value = new InviteService(userCache).uiFlag();
                    break;
                case activity:
                    //进度更新
                    try (WorkService workService = new WorkService(userCache, req.platform))
                    {
                        value = workService.uiFlag();
                    }
                    break;
                case email:
                    value = new NoticeService(userCache).uiFlag();
                    break;
                default:
                    break;
            }
            res.flags.put(type, value);
        }
        return res;
    }
}
