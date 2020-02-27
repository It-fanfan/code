package com.code.servlet.angling;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.record.RecordAngling;
import com.code.protocols.operator.OperatorBase;
import com.code.service.angling.AnglingInitService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

import static com.code.protocols.core.AnglingBase.*;
import static com.code.protocols.core.angling.AnglingRod.RequestImpl;
import static com.code.protocols.core.angling.AnglingRod.ResponseImpl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/angling/rod", name = "AnglingRod")
public class AnglingRodServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl impl = new ResponseImpl();
        impl.status = true;
        FishingRodConfig config = AnglingInitService.getFishingRodConfig();
        FishingRod rod = userCache.getFishingRod(config);
        //临近补充，缺1可以进行回复
        if (rod.endurance > 1)
        {
            impl.rod = rod;
            impl.msg = ERROR.UNSATISFIED.getMsg();
            impl.code = ERROR.UNSATISFIED.getCode();
            return impl;
        }
        switch (req.costtype)
        {
            case shell:
            {
                if (rod.video > 0 && !req.videolimit)
                {
                    impl.code = ERROR.VIDEO_EXIST.getCode();
                    impl.msg = ERROR.VIDEO_EXIST.getMsg();
                    return impl;
                }
                int basin = userCache.getBasin();
                if (config.basin == null)
                {
                    config.basin = new Vector<>();
                    config.basin.add(2000);
                }
                int cost = config.basin.lastElement();
                if (basin <= config.basin.size())
                {
                    cost = config.basin.elementAt(basin - 1);
                }
                long shell = userCache.shell();
                //判断贝壳是否充足，消耗是否成功
                if (shell < cost || !userCache.incrShell(-cost))
                {
                    impl.code = ERROR.SHELL_LACKING.getCode();
                    impl.msg = ERROR.SHELL_LACKING.getMsg();
                    return impl;
                }
                impl.setUserValue(userCache);
                userCache.restoreFishingRod(rod, config);
                //更新垂钓记录信息
                RecordAngling record = AnglingInitService.getRecordAngling(userCache);
                record.setCostShell(record.getCostShell() + cost);
                AnglingInitService.updateRecordAngling(userCache, record);
            }
            break;
            case video:
            {
                if (rod.video <= 0)
                {
                    impl.code = ERROR.VIDEO_FULL.getCode();
                    impl.msg = ERROR.VIDEO_FULL.getMsg();
                    return impl;
                }
                rod.video--;
                //默认视频完成
                userCache.restoreFishingRod(rod, config);
            }
            break;
            default:
                return impl;
        }
        try (WorkService service = new WorkService(userCache))
        {
            service.addProcess(OperatorBase.ActivityType.rod, 1);
        }
        impl.rod = rod;
        impl.code = STATUS_SUCCESS;
        return impl;
    }
}
