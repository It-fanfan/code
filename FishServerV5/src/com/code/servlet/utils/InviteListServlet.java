package com.code.servlet.utils;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.utils.InviteList;
import com.code.service.ui.InviteService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit(timeout = 1000)
@WebServlet(urlPatterns = "/add/inviteList", name = "com.code.protocols.operator.utils.InviteList")
public class InviteListServlet extends ServletMain<InviteList.RequestImpl, InviteList.ResponseImpl>
{
    @Override
    protected InviteList.ResponseImpl doLogic(InviteList.RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        InviteList.ResponseImpl res = new InviteList.ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        InviteService inviteService = new InviteService(userCache);
        res.receives = InviteService.getInviteReceiveConfig();
        res.invites = BigData.getBigData(inviteService.getInviteList(), XwhTool.getDeclaredFieldsAll(OperatorBase.Invite.class));
        return res;
    }
}
