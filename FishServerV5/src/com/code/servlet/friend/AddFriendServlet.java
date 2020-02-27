package com.code.servlet.friend;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.social.SocialBase;
import com.code.protocols.social.friend.FriendInfo;
import com.code.protocols.social.friend.FriendProtocol;
import com.code.service.friend.FriendService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/friend/add", name = "com.code.protocols.social.friend.FriendProtocol.Add")
public class AddFriendServlet extends ServletMain<FriendProtocol.AddRequest, FriendProtocol.AddResponse>
{
    @Override
    protected FriendProtocol.AddResponse doLogic(FriendProtocol.AddRequest req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        FriendProtocol.AddResponse res = new FriendProtocol.AddResponse();
        FriendService service = new FriendService(userCache);
        if (service.submitFriend(req.applyid, req.result))
        {
            Vector<FriendInfo> ifs = service.getFriendList();
            res.firendlist = BigData.getBigData(ifs, FriendInfo.class);
            SocialBase.putResponse(SocialBase.ERROR.SUCCESS, res);
        } else
        {
            SocialBase.putResponse(SocialBase.ERROR.UNDEFINE_USER, res);
        }
        res.status = true;

        return res;
    }
}
