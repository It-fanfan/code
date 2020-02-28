package com.code.servlet.friend;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.book.FriendProExt.RequestImpl;
import com.code.protocols.core.book.FriendProExt.ResponseImpl;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.social.SocialBase;
import com.code.service.friend.FriendGuardService;
import com.code.service.friend.FriendService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 添加好友守護
 */

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/friend/guard", name = "com.code.protocols.core.book.FriendProExt")
public class FriendProServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        FriendGuardService service = new FriendGuardService(userCache);
        UserCache friendCache = UserCache.getUserCache(req.applyid);

        if (friendCache == null)
        {
            SocialBase.putResponse(SocialBase.ERROR.ERROR_STATUS, res);
        } else if (!new FriendService(userCache).isFriend(req.applyid))
        {
            SocialBase.putResponse(SocialBase.ERROR.NO_FRIEND, res);
        } else
        {
            SocialBase.ERROR statusCode = service.addFriendGuard(friendCache);
            SocialBase.putResponse(statusCode, res);
        }
        if (res.code == STATUS_SUCCESS)
        {
            //进度更新
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(OperatorBase.ActivityType.friendProtected, 1);
            }
        }
        res.friendgardian = FriendGuardService.getBookGuard(userCache);
        return res;
    }
}
