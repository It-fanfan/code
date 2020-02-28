package com.code.servlet.work;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.work.TurntableListProto.RequestImpl;
import com.code.protocols.operator.work.TurntableListProto.ResponseImpl;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/turntable/list", name = "com.code.protocols.operator.work.TurntableListProto")
public class TurntableListServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        try (WorkService workService = new WorkService(userCache, req.platform))
        {
            return workService.getTurntableData();
        }
    }
}
