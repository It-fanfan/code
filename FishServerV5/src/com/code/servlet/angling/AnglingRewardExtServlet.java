package com.code.servlet.angling;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.angling.AnglingInitService;
import com.code.service.angling.AnglingRewardService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

import static com.code.protocols.core.AnglingBase.*;
import static com.code.protocols.core.angling.AnglingRewardExt.RequestImpl;
import static com.code.protocols.core.angling.AnglingRewardExt.ResponseV2Impl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/angling/rewardV2", name = "AnglingRewardExt")
public class AnglingRewardExtServlet extends ServletMain<RequestImpl, ResponseV2Impl>
{
    private static Logger LOG = LoggerFactory.getLogger(AnglingRewardExtServlet.class);

    @Override
    protected ResponseV2Impl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserCache userCache = getUserCache(request);
        ResponseV2Impl res = new ResponseV2Impl();
        AnglingRewardService rewardService = new AnglingRewardService(userCache);
        res.type = req.type;
        String otherId = req.otherid;
        int index = req.index;
        AnglingBase.updateResponseCode(ERROR.SUCCESS, res);
        ERROR error;
        switch (res.type)
        {
            //垂钓贝壳
            case takeshell:
            {
                ShellResult result = new ShellResult();
                res.result = result;
                long userShell = rewardService.getTakeShellData(otherId);
                Vector<StealShellConfig> configs = AnglingInitService.getStealShellConfig();
                StealShellConfig config = configs.stream().filter(data -> data.index == index).findFirst().orElse(null);
                if (config == null)
                {
                    break;
                }
                AnglingInitService service = new AnglingInitService(userCache);
                int base = req.propid == 7 ? 2 : 1;
                if (req.propid > 0)
                {
                    if (!service.existAndUpdateProp(req.propid))
                    {
                        AnglingBase.updateResponseCode(ERROR.NO_ALLOW, res);
                        res.props = service.getCurrentProp();
                        return res;
                    }
                    res.props = service.getCurrentProp();
                }
                //设置偷贝壳结果
                LOG.debug("最大贝壳用户:" + otherId + ",shell:" + userShell + ",基数" + XwhTool.getJSONByFastJSON(config));
                result.shell = config.getHit(userShell);
                int shell = result.shell * base;
                error = rewardService.setTakeResult(otherId, shell);
                AnglingBase.updateResponseCode(error, res);
                rewardService.addRecord(AnglingExtraType.takeshell, otherId, String.valueOf(shell), error == ERROR.SUCCESS);
                result.othershell = String.valueOf(userShell);

                res.setUserValue(userCache);
                //成就:偷贝壳:perfect
                if (config.index == OperationType.perfect.getValue())
                {
                    //进度更新
                    try (WorkService workService = new WorkService(userCache))
                    {
                        workService.addProcess(OperatorBase.ActivityType.stealShell, 1);
                    }
                    new AchievementService(userCache).addAchievement(AchievementType.StealShellPerfect, 1);
                }
                return res;
            }
            case guessbook:
            {
                BookResult result = rewardService.checkGuessBook(otherId);
                res.result = result;
                if (result.status != StealStatus.success)
                {
                    result.shell = rewardService.getSubsidy();
                }
                rewardService.addRecord(AnglingExtraType.guessbook, otherId, String.valueOf(result.book), result.status == StealStatus.success);
                return res;
            }
            default:
                break;
        }
        AnglingBase.updateResponseCode(ERROR.UNSATISFIED, res);
        res.type = AnglingExtraType.none;
        return res;
    }
}
