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
import java.util.concurrent.ThreadLocalRandom;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/friend/select", name = "com.code.protocols.social.friend.FriendProtocol.Select")
public class SelectFriendServlet extends ServletMain<FriendProtocol.SelectRequest, FriendProtocol.SelectResponse>
{
    @Override
    protected FriendProtocol.SelectResponse doLogic(FriendProtocol.SelectRequest req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        FriendProtocol.SelectResponse res = new FriendProtocol.SelectResponse();
        res.status = true;
        res.code = STATUS_SUCCESS;
        FriendService service = new FriendService(userCache);
        if (req.applycode != null && !req.applycode.trim().isEmpty())
        {
            //进行查询好友信息
            long friendId = FriendService.spiltCode(req.applycode);
            if (friendId == Integer.valueOf(req.userid))
            {
                SocialBase.putResponse(SocialBase.ERROR.ONESELF, res);
                return res;
            }
            if (friendId != 0)
            {
                if (!UserCache.exist(String.valueOf(friendId)))
                {
                    SocialBase.putResponse(SocialBase.ERROR.ERROR_STATUS, res);
                    return res;
                }
                res.friend = service.createFriendInfoNode(String.valueOf(friendId), ThreadLocalRandom.current().nextBoolean());
                return res;
            }
        }
        Vector<FriendInfo> recommend = service.getRecommend();
        res.recommend = BigData.getBigData(recommend, FriendInfo.class);
        return res;
    }
}
