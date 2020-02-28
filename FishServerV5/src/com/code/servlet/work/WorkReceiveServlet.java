package com.code.servlet.work;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.work.Activity;
import com.code.service.work.ActivityWork;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.operator.OperatorBase.updateResponseCode;


@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/work/receive", name = "com.code.protocols.operator.work.Activity.Receive")
public class WorkReceiveServlet extends ServletMain<Activity.ReceiveRequest, Activity.ReceiveResponse>
{
    @Override
    protected Activity.ReceiveResponse doLogic(Activity.ReceiveRequest req, HttpServletRequest request, HttpServletResponse response)
    {
        Activity.ReceiveResponse res = new Activity.ReceiveResponse();
        UserCache userCache = getUserCache(request);
        res.status = true;
        res.code = STATUS_SUCCESS;
        try (WorkService service = new WorkService(userCache, req.platform))
        {
            switch (req.worktype)
            {
                case casework:
                    if (!service.receiveWeekActivity(req.workid))
                    {
                        updateResponseCode(OperatorBase.ERROR.UNSATISFIED, res);
                        return res;
                    } else
                    {
                        ActivityWork work = service.getWork();
                        if (work != null)
                        {
                            res.process = work.caseIds;
                        }
                    }
                    break;
                case activity:
                    if (!service.receiveWork(req.workid))
                    {
                        updateResponseCode(OperatorBase.ERROR.UNSATISFIED, res);
                        return res;
                    } else
                    {
                        ActivityWork work = service.getWork();
                        if (work != null)
                        {
                            res.activity = work.activityValue;
                            int dayFlag = XwhTool.getCurrentDateValue();
                            ActivityWork.DayActivity dayActivity = service.getDayActivity(dayFlag);
                            if (dayActivity != null)
                                res.process = dayActivity.workList;
                        }
                    }
                    break;
                case signup:
                    if (!service.signup())
                    {
                        updateResponseCode(OperatorBase.ERROR.SIGNUP_REPEAT, res);
                        return res;
                    } else
                    {
                        ActivityWork work = service.getWork();
                        if (work != null)
                        {
                            res.process = work.signup;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        res.setUserValue(userCache);
        return res;
    }
}
