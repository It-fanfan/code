package com.code.servlet.angling;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.AnglingBase;
import com.code.service.angling.DrifterService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.core.angling.DrifterReceive.RequestImpl;
import static com.code.protocols.core.angling.DrifterReceive.ResponseImpl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/core/drifterReceive", name = "com.code.protocols.core.angling.DrifterReceive")
public class DrifterReceiveServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        UserCache userCache = getUserCache(request);
        DrifterService service = new DrifterService(userCache, req.platform);
        AnglingBase.ERROR error = service.receive(req.index);
        AnglingBase.updateResponseCode(error, res);
        res.setUserValue(userCache);
        return res;
    }
}
