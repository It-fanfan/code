package com.code.servlet.achievement;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementList;
import com.code.service.achievement.AchievementService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/achievement/list", name = "com.code.protocols.operator.achievement.AchievementList")
public class AchievementListServlet extends ServletMain<AchievementList.RequestImpl, AchievementList.ResponseImpl>
{

    @Override
    protected AchievementList.ResponseImpl doLogic(AchievementList.RequestImpl req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        AchievementList.ResponseImpl res = new AchievementList.ResponseImpl();
        res.achievement = AchievementService.getConfigAchievement(req.platform);
        res.config = AchievementService.getConfig(req.platform);
        UserCache userCache = getUserCache(request);
        AchievementService service = new AchievementService(userCache);
        res.userachievement = BigData.getBigData(service.getUserAchievement(), OperatorBase.AchievementInfo.class);
        res.status = true;
        res.code = STATUS_SUCCESS;
        return res;
    }

    @Override
    protected boolean existFlush()
    {
        return false;
    }
}
