package com.code.servlet.login;

import com.annotation.AvoidRepeatableCommit;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.*;
import com.code.protocols.basic.BigData;
import com.code.protocols.login.Init;
import com.code.service.angling.AnglingInitService;
import com.code.service.friend.FriendService;
import com.code.service.order.OrderService;
import com.code.service.ui.InviteService;
import com.code.servlet.base.ServletMain;
import com.utils.ReadConfig;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/login/init", name = "com.code.protocols.login.Init")
public class InitServlet extends ServletMain<Init.RequestImpl, Init.ResponseImpl>
{


    @Override
    protected Init.ResponseImpl doLogic(RequestParameter parameter, HttpServletRequest request, HttpServletResponse response)
    {
        Init.RequestImpl req = parameter.getRequestObject();
        Init.ResponseImpl res = new Init.ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        try
        {
            System.out.println("remote addr:" + request.getRemoteAddr());
            String ver = req.serverver;
            if (req.serverver == null || req.serverver.isEmpty())
            {
                ver = Systemstatusinfo.getText(ReadConfig.get("verext"));
            }
            res.version = ver;
            Config_version version = (Config_version) FishInfoDb.instance().getCacheKey(Config_version.class, new String[]{"ver", ver});
            if (version != null)
            {
                res.configurl = version.getUrl();
                res.md5 = version.getMd5();
                res.version = ver;
                res.exmain = version.isStatus();
                res.initnotices = Init.getInitNotices();
            }
            res.angling = AnglingInitService.getInit();
            res.shareconfig = BigData.getBigData(FishInfoDb.instance().getCacheListByClass(ConfigShare.class), ConfigShare.class);
            res.welfareconfig = InviteService.getRewardUtilConfig();
            res.orderinit = OrderService.getInitConfig();
            res.friendinit = FriendService.getInitConfig();
            //res.achievement = AchievementService.getConfigAchievement();
            res.values = getUpdateValue();

        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e));
        } finally
        {
            Log4j.analysisLog(parameter.getPlatform(), "init", req.userid, parameter.getIp(), parameter.getSessionId());
        }
        return res;
    }

    /**
     * 获取数值更新
     *
     * @return 数值
     */
    private Init.ValueConfig getUpdateValue()
    {
        Init.ValueConfig values = new Init.ValueConfig();
        try
        {
            Vector<ConfigFish> data = FishInfoDb.instance().getCacheListByClass(ConfigFish.class);
            values.fishtypes = BigData.getBigData(data, ConfigFish.getInitField());
            Vector<ConfigBasin> basins = FishInfoDb.instance().getCacheListByClass(ConfigBasin.class);
            values.basin =  BigData.getBigData(basins, ConfigBasin.getInitField());
        } catch (Exception e)
        {
            STAR.error(Log4j.getExceptionInfo(e));
        }
        return values;
    }
}
