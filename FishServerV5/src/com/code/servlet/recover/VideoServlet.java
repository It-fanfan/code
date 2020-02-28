package com.code.servlet.recover;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.AnglingBase;
import com.code.service.order.VideoService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.core.recover.VideoReceive.RequestImpl;
import static com.code.protocols.core.recover.VideoReceive.ResponseImpl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/order/video", name = "com.code.protocols.core.recover.VideoReceive")
public class VideoServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseImpl res = new ResponseImpl();
        UserCache userCache = getUserCache(request);
        VideoService service = new VideoService(userCache);
        AnglingBase.ERROR error = service.receiveVideo(req.times);
        AnglingBase.updateResponseCode(error, res);
        if (error == AnglingBase.ERROR.SUCCESS)
        {
            res.setUserValue(userCache);
        }
        res.ordervideo = service.getOrderVideo();
        return res;
    }
}
