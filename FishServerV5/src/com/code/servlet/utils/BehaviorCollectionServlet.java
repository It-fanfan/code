package com.code.servlet.utils;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.protocols.operator.utils.BehaviorCollection.Request;
import com.code.protocols.operator.utils.BehaviorCollection.Response;
import com.code.service.achievement.AchievementService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/behavior/collection", name = "com.code.protocols.operator.utils.BehaviorCollection")
public class BehaviorCollectionServlet extends ServletMain<Request, Response>
{
    /**
     *
     */
    private static final long serialVersionUID = 3543325167163183008L;
    private static Logger BEHAVIOR_LOG = LoggerFactory.getLogger("behaviorLog");

    @Override
    protected Response doLogic(Request req, HttpServletRequest request, HttpServletResponse response)
    {
        Response res = new Response();
        res.status = true;
        res.code = STATUS_SUCCESS;
        UserCache userCache = getUserCache(request);
        Map<OperatorBase.ActivityType, Integer> process = new HashMap<>();
        req.datas.forEach(action ->
        {
            switch (action.type)
            {
                case "video":
                {
                    if (null != action.ext && action.ext.contains("action=close_finish"))
                    {
                        Integer count = process.get(OperatorBase.ActivityType.video);
                        if (count == null)
                            count = 0;
                        process.put(OperatorBase.ActivityType.video, ++count);
                        new AchievementService(userCache).addAchievement(AchievementType.Ads, 1);
                    }
                }
                break;
                case "share":
                {
                    Integer count = process.get(OperatorBase.ActivityType.share);
                    if (count == null)
                        count = 0;
                    process.put(OperatorBase.ActivityType.share, ++count);
                }
                break;
                default:
                    break;
            }
        });
        if (!process.isEmpty())
        {
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(process);
            }
        }
        BEHAVIOR_LOG.info(XwhTool.getJSONByFastJSON(req));
        return res;
    }

}
