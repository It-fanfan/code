package com.code.servlet.utils;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.protocols.operator.utils.InviteReceive;
import com.code.service.achievement.AchievementService;
import com.code.service.ui.InviteService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit(timeout = 1000)
@WebServlet(urlPatterns = "/add/invite", name = "com.code.protocols.operator.utils.InviteReceive")
public class InviteReceiveServlet extends ServletMain<InviteReceive.RequestImpl, InviteReceive.ResponseImpl>
{
    @Override
    protected InviteReceive.ResponseImpl doLogic(InviteReceive.RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        InviteReceive.ResponseImpl res = new InviteReceive.ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        InviteService inviteService = new InviteService(userCache);
        OperatorBase.ERROR err = inviteService.receiveReward(req.index);
        OperatorBase.updateResponseCode(err, res);
        if (res.code == STATUS_SUCCESS)
        {
            res.setUserValue(userCache);
            //更新进度
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(OperatorBase.ActivityType.invite, 1);
            }
            //成就更新
            new AchievementService(userCache).addAchievement(AchievementType.Invite, 1);
        }
        return res;
    }
}
