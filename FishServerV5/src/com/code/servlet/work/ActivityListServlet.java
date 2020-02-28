package com.code.servlet.work;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.work.WorkActivity;
import com.code.dao.entity.work.WorkSign;
import com.code.dao.entity.work.WorkWeekActivity;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.work.Activity;
import com.code.service.work.ActivityWork;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/work/activityList", name = "com.code.protocols.operator.work.Activity.List")
public class ActivityListServlet extends ServletMain<Activity.ListRequest, Activity.ListResponse>
{
    @Override
    protected Activity.ListResponse doLogic(Activity.ListRequest req, HttpServletRequest request, HttpServletResponse response)
    {
        Activity.ListResponse res = new Activity.ListResponse();
        UserCache userCache = getUserCache(request);
        try (WorkService service = new WorkService(userCache, req.platform))
        {
            res.status = true;
            res.code = STATUS_SUCCESS;
            switch (req.worktype)
            {
                case all:
                {
                    res.signuplist = BigData.getBigData(WorkService.getWorkSign(), WorkSign.class);
                    res.worklist = BigData.getBigData(service.getWorkActivity(), WorkActivity.class);
                    res.caselist = BigData.getBigData(service.getWorkWeekActivity(), WorkWeekActivity.class);
                }
                break;
                case signup:
                    res.signuplist = BigData.getBigData(WorkService.getWorkSign(), WorkSign.class);
                    break;
                case activity:
                    res.worklist = BigData.getBigData(service.getWorkActivity(), WorkActivity.class);
                case casework:
                    res.caselist = BigData.getBigData(service.getWorkWeekActivity(), WorkWeekActivity.class);
                    break;
                default:
                    break;
            }
            setWorkReceive(res, service);
        }
        return res;
    }

    /**
     * 设置领取信息
     *
     * @param res     下发
     * @param service 任务
     */
    private void setWorkReceive(Activity.ListResponse res, WorkService service)
    {
        ActivityWork work = service.getUserActivityWork();
        res.receive = new OperatorBase.ActivityReceive();
        res.receive.weekflag = work.weekFlag;
        res.receive.activity = work.activityValue;
        res.receive.caseopen = work.caseIds;
        res.receive.sign = work.signup;
        int dayFlag = XwhTool.getCurrentDateValue();
        for (ActivityWork.DayActivity element : work.activities)
        {
            if (element.dayFlag == dayFlag)
            {
                res.receive.work = element.workList;
                res.receive.workprocess = new LinkedHashMap<>();
                service.getActivityWorkProcess(element, res.receive.workprocess);
                return;
            }
        }
    }
}
