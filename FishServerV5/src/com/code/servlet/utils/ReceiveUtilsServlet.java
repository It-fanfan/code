package com.code.servlet.utils;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.utils.RewardReceive;
import com.code.service.ui.InviteService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/add/receiveUtils", name = "com.code.protocols.operator.utils.RewardReceive")
public class ReceiveUtilsServlet extends ServletMain<RewardReceive.UtilRequestImpl, RewardReceive.ResponseImpl>
{
    @Override
    protected RewardReceive.ResponseImpl doLogic(RewardReceive.UtilRequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        RewardReceive.ResponseImpl res = new RewardReceive.ResponseImpl();
        UserCache userCache = getUserCache(request);
        InviteService inviteService = new InviteService(userCache);
        res.status = true;
        OperatorBase.ERROR error = inviteService.receiveUtils(req.type);
        OperatorBase.updateResponseCode(error, res);
        if (res.code == STATUS_SUCCESS)
        {
            res.setUserValue(userCache);
        }
        return res;
    }
}
