package com.code.servlet.user;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.social.friend.FriendInfo;
import com.code.protocols.user.UserInfo;
import com.code.service.achievement.AchievementService;
import com.code.service.angling.AnglingInitService;
import com.code.service.angling.FishDataService;
import com.code.service.book.BookInitService;
import com.code.service.friend.FriendService;
import com.code.service.ui.InviteService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

import static com.code.protocols.core.AnglingBase.FishResult;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/user/info", name = "com.code.protocols.user.UserInfo")
public class UserInfoServlet extends ServletMain<UserInfo.RequestImpl, UserInfo.ResponseImpl>
{
    @Override
    protected UserInfo.ResponseImpl doLogic(UserInfo.RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        UserInfo.ResponseImpl res = new UserInfo.ResponseImpl();
        res.code = STATUS_SUCCESS;
        res.status = true;
        if (req.usertypes != null)
            for (UserInfo.SelectUserType type : req.usertypes)
                switch (type)
                {
                    case angling:
                        setAngling(res, userCache);
                        break;
                    case basic:
                        setBasic(res, userCache);
                        break;
                    case fish:
                        setFishInfo(res, userCache);
                        break;
                    case value:
                        setValue(res, userCache);
                        break;
                    case book:
                        setBook(res, userCache);
                        break;
                    case friend:
                        setFriends(res, userCache);
                        break;
                    case welfare:
                        res.welfare = InviteService.getUserReceiveReward(userCache);
                        break;
                    case guide:
                    {
                        if (req.guide != null && !req.guide.isEmpty())
                        {
                            userCache.hSet(type.name(), req.guide);
                        }
                        res.guide = userCache.getValue(type.name());
                    }
                    break;
                    case achievement:
                    {
                        AchievementService service = new AchievementService(userCache);
                        res.achievement = BigData.getBigData(service.getUserAchievement(), OperatorBase.AchievementInfo.class);
                    }
                    break;
                    case turntable://转盘类型
                    {
                        try (WorkService workService = new WorkService(userCache))
                        {
                            res.turntable = workService.getUserTurntable();
                        }
                    }
                    default:
                        break;
                }
        res.userid = userCache.getUserId();
        return res;
    }

    protected boolean existFlush()
    {
        return false;
    }

    /**
     * 查询好友信息
     *
     * @param res       下发
     * @param userCache 玩家
     */
    private void setFriends(UserInfo.ResponseImpl res, UserCache userCache)
    {
        FriendService service = new FriendService(userCache);
        Vector<FriendInfo> ifs = service.getFriendList();
        res.friends = BigData.getBigData(ifs, FriendInfo.class);
        res.applycode = service.getFriendCode();
        //申请列表
        Vector<FriendInfo> applyList = service.getApplyList();
        res.applylist = BigData.getBigData(applyList, FriendInfo.class);
    }

    /**
     * 设置图鉴数据
     *
     * @param res 下发配置
     */
    private void setBook(UserInfo.ResponseImpl res, UserCache userCache)
    {
        try
        {
            BookInitService bookInitService = new BookInitService(userCache);
            res.basin = bookInitService.getUserBasin();
        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 设置价值属性
     *
     * @param res 下发参数
     */
    private void setValue(UserInfo.ResponseImpl res, UserCache userCache)
    {
        res.setUserValue(userCache);
    }

    /**
     * 设置基础数据
     *
     * @param res 下发数据
     */
    private void setBasic(UserInfo.ResponseImpl res, UserCache userCache)
    {
        res.icon = userCache.getIcon();
        res.nickname = userCache.getNickName();
        res.openid = userCache.getOpenid();
        res.sex = userCache.getSex();
        res.registtime = userCache.getRegisterTime();
        res.unionid = userCache.getUnionId();
    }

    /**
     * 设置垂钓
     *
     * @param res 下发参数
     */
    private void setAngling(UserInfo.ResponseImpl res, UserCache userCache)
    {
        AnglingInitService anglingInitService = new AnglingInitService(userCache);
        res.anglinguser = anglingInitService.getUser();
    }

    /**
     * 设置鱼数据
     *
     * @param res 下发信息
     */
    private void setFishInfo(UserInfo.ResponseImpl res, UserCache userCache)
    {
        try (FishDataService service = new FishDataService(userCache))
        {
            res.fishdata = BigData.getBigData(service.getUserFishes(), FishResult.class);
        }
    }
}
