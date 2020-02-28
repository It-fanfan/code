package com.code.servlet.achievement;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.achievement.ConfigAchievement;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.achievement.Receive.RequestImpl;
import com.code.protocols.operator.achievement.Receive.ResponseImpl;
import com.code.service.achievement.AchievementService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.operator.OperatorBase.*;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/achievement/receive", name = "com.code.protocols.operator.achievement.Receive")
public class ReceiveServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        AchievementService service = new AchievementService(userCache);
        ConfigAchievement achievement = (ConfigAchievement) FishInfoDao.instance().getCacheKey(ConfigAchievement.class, new String[]{"id", String.valueOf(req.id)});
        if (achievement == null || !achievement.isStatus())
        {
            updateResponseCode(ERROR.UNDEFINED, res);
            return res;
        }
        //检测是否达标
        if (!service.existStandard(achievement))
        {
            updateResponseCode(ERROR.NOTQUALIFIED, res);
            return res;
        }
        //检测是否领取
        if (service.existReceive(req.id))
        {
            updateResponseCode(ERROR.REPEATRECEIVE, res);
            return res;
        }
        if (!service.receive(achievement))
        {
            updateResponseCode(ERROR.RECEIVEFAIL, res);
        } else
        {
            res.setUserValue(userCache);
        }
        res.achievement = BigData.getBigData(service.getUserAchievement(), AchievementInfo.class);
        return res;
    }

    @Override
    protected boolean existFlush()
    {
        return false;
    }
}
