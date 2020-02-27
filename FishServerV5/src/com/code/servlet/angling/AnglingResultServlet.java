package com.code.servlet.angling;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.core.angling.AnglingResultExt.RequestImpl;
import com.code.protocols.core.angling.AnglingResultExt.ResponseImpl;
import com.code.protocols.operator.OperatorBase;
import com.code.service.achievement.AchievementService;
import com.code.service.angling.AnglingInitService;
import com.code.service.angling.AnglingRewardService;
import com.code.service.angling.DrifterService;
import com.code.service.book.BookInitService;
import com.code.service.work.WorkService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import static com.code.protocols.core.AnglingBase.*;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/angling/result", name = "AnglingResultExt")
public class AnglingResultServlet extends ServletMain<RequestImpl, ResponseImpl>
{

    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        AnglingRewardService rewardService = new AnglingRewardService(userCache);
        try
        {
            AnglingInitService service = new AnglingInitService(userCache);
            BookInitService bookService = new BookInitService(userCache);
            //判断是否钓上鱼 没有则返回提示语..
            if (req.fishes == null || req.fishes.isEmpty())
            {
                res.msg = UN_FISH_TIP[ThreadLocalRandom.current().nextInt(UN_FISH_TIP.length)];
            } else
            {
                //获取未拥有的图鉴
                Vector<Integer> nothing = bookService.getUserBook(req.basin, BookInitService.BookType.nothing);
                //出现被偷取鱼数据
                Vector<Integer> stolen = bookService.getUserBook(req.basin, BookInitService.BookType.stolen);
                boolean stolenFlag = false;
                for (FishResult result : req.fishes)
                {
                    if (nothing.contains(result.ftid))
                    {
                        AnglingBase.updateResponseCode(ERROR.NO_MATCH_ANGLING, res);
                        return res;
                    }
                    if (stolen.contains(result.ftid))
                    {
                        result.sum = 0;
                        stolenFlag = true;
                    }
                }
                if (stolenFlag)
                {
                    res.basin = bookService.getUserBasin();
                }
            }
            res.allow = service.reduce(req.endurance, req.propid);
            //鱼竿信息
            res.rod = userCache.getFishingRod(AnglingInitService.getFishingRodConfig());
            if (!res.allow)
            {
                res.code = ERROR.NO_ALLOW.getCode();
                res.msg = ERROR.NO_ALLOW.getMsg();
                return res;
            }
            //设置用户鱼数据..
            int fishSize = service.setFishDataResult(req.fishes);
            res.fishsize = fishSize;
            res.bag = BigData.getBigData(service.getUserFishData(), FishResult.class);
            //进行校验贝壳鱼
            String shellId = String.valueOf(AnglingBase.SHELL_FISH_ID);
            if (req.gamestatus.props != null && req.gamestatus.props.containsKey(shellId))
            {
                int shellCount = req.gamestatus.props.get(shellId);
                long addShell = AnglingInitService.getShellFishProp();
                userCache.incrShell(addShell * shellCount);
                LOG.debug("贝壳鱼增加贝壳数:" + (addShell * shellCount));
            }
            res.setUserValue(userCache);
            res.props = service.getCurrentProp();
            service.updateAnglingRecord(fishSize);
            //添加活跃进度
            Map<OperatorBase.ActivityType, Integer> process = new HashMap<>();
            process.put(OperatorBase.ActivityType.angling, 1);
            if (req.propid > 0)
                process.put(OperatorBase.ActivityType.fishProp, 1);
            process.put(OperatorBase.ActivityType.fishSize, fishSize);
            try (WorkService workService = new WorkService(userCache))
            {
                workService.addProcess(process);
                //漂流瓶
                res.drifter = new DrifterService(userCache,req.platform).createDrifter(workService);
            }
            //添加成就进度
            OperationType operationType = req.gamestatus.operation;
            res.operation = userCache.recordLastOperationData(operationType);
            req.gamestatus.doublehit = res.operation.get(operationType.name());
            new AchievementService(userCache).addAchievement(req);
        } finally
        {
            //初始化奖励数据..
            res.reward = rewardService.createRedBags();

        }
        return res;
    }


}
