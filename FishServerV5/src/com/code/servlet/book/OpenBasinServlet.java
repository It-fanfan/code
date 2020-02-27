package com.code.servlet.book;


import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.book.BookInitService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.core.AnglingBase.ERROR;
import static com.code.protocols.core.book.UpgradeBasin.RequestImpl;
import static com.code.protocols.core.book.UpgradeBasin.ResponseImpl;

/**
 * 海域升级
 */

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/book/openBasin", name = "com.code.protocols.core.book.UpgradeBasin", description = "海域升级")
public class OpenBasinServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        UserCache userCache = getUserCache(request);
        BookInitService bookService = new BookInitService(userCache);

        res.status = true;
        res.code = STATUS_SUCCESS;
        int basinId = userCache.getBasin();
        int up = basinId + 1;
        ConfigBasin config = (ConfigBasin) FishInfoDb.instance().getCacheKey(ConfigBasin.class, new String[]{"basinId", String.valueOf(up)});
        ERROR statusCode;
        //无图鉴水域，不存在
        if (config == null || config.getArchiveName() <= 0)
        {
            statusCode = ERROR.NO_WATER;
        } else if (userCache.shell() < config.getOpenCost())
        {
            statusCode = ERROR.UNSATISFIED;
        } else
        {
            statusCode = bookService.openBasin(config);
        }
        res.code = statusCode.getCode();
        if (res.code == STATUS_SUCCESS && config != null)
        {
            res.setUserValue(userCache);
            userCache.setBasinData(config.getBasinId(), config.getArchiveName());
            res.basin = bookService.getUserBasin();
            //进度更新
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(OperatorBase.ActivityType.openBasin, 1);
            }
            //成就
            new AchievementService(userCache).addAchievement(AchievementType.Basin, 1);
        } else
        {
            res.msg = statusCode.getMsg();//异常描述
        }
        return res;
    }

}
