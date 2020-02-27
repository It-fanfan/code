package com.code.servlet.friend;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.social.SocialBase;
import com.code.protocols.social.friend.FriendInfo;
import com.code.protocols.social.friend.FriendProtocol;
import com.code.service.friend.FriendService;
import com.code.service.gm.RobotService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/friend/apply", name = "com.code.protocols.social.friend.FriendProtocol.Apply")
public class ApplyFriendServlet extends ServletMain<FriendProtocol.ApplyRequest, FriendProtocol.ApplyResponse>
{
    @Override
    protected FriendProtocol.ApplyResponse doLogic(FriendProtocol.ApplyRequest req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        FriendProtocol.ApplyResponse res = new FriendProtocol.ApplyResponse();
        res.code = STATUS_SUCCESS;
        res.status = true;
        if (req.applyid.startsWith(RobotService.startRobot()))
        {
            return res;
        }
        if (req.applyid.equals(req.userid))
        {
            SocialBase.putResponse(SocialBase.ERROR.ONESELF, res);
            return res;
        }
        UserCache friendCache = UserCache.getUserCache(req.applyid);
        if (friendCache == null)
        {
            SocialBase.putResponse(SocialBase.ERROR.UNDEFINE_USER, res);
            return res;
        }
        FriendService service = new FriendService(userCache);
        if (!service.addApplyUser(friendCache))
        {
            LOG.error("friend apply:", SocialBase.ERROR.DUPLICATE.getCode(), SocialBase.ERROR.DUPLICATE.getMsg());
        }
        Vector<FriendInfo> recommend = service.getRecommend();
        res.recommend = BigData.getBigData(recommend, FriendInfo.class);
        return res;
    }
}
