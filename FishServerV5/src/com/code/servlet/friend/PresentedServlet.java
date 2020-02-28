package com.code.servlet.friend;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.social.friend.FriendProtocol;
import com.code.service.friend.FriendService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.social.SocialBase.ERROR;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/friend/presented", name = "com.code.protocols.social.friend.FriendProtocol.Presented")
public class PresentedServlet extends ServletMain<FriendProtocol.PresentedRequest, FriendProtocol.PresentedResponse>
{

    @Override
    protected FriendProtocol.PresentedResponse doLogic(FriendProtocol.PresentedRequest req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        FriendProtocol.PresentedResponse res = new FriendProtocol.PresentedResponse();
        res.status = true;
        res.code = STATUS_SUCCESS;
        UserCache userCache = getUserCache(request);
        FriendService service = new FriendService(userCache);
        ERROR error = service.presented(req.applyid);
        if (error != ERROR.SUCCESS)
        {
            res.msg = error.getCode() + ":" + error.getMsg();
        }
        res.friendinfo = service.getUserFriend(req.applyid);
        return res;
    }
}
