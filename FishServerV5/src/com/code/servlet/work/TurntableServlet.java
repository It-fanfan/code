package com.code.servlet.work;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.operator.work.TurntableProto.RequestImpl;
import static com.code.protocols.operator.work.TurntableProto.ResponseImpl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/turntable/play", name = "com.code.protocols.operator.work.TurntableProto")
public class TurntableServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        OperatorBase.updateResponseCode(OperatorBase.ERROR.SUCCESS, res);
        try (WorkService workService = new WorkService(userCache,req.platform))
        {
            OperatorBase.ERROR error = workService.playTurntable(req.type, res);
            if (error == OperatorBase.ERROR.SUCCESS)
            {
                res.setUserValue(userCache);
            } else
            {
                OperatorBase.updateResponseCode(error, res);
            }
        }
        return res;
    }
}
