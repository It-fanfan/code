package com.code.service.angling;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.dto.PreviewDTO;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.ConfigEmail;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.record.RecordSteal;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BasePro.RewardUser;
import com.code.protocols.core.AnglingBase.*;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.book.BookInitService;
import com.code.service.book.BookInitService.BookType;
import com.code.service.friend.FriendGuardService;
import com.code.service.gm.RobotService;
import com.code.service.message.NoticeService;
import com.code.service.rate.AnglingRewardHit;
import com.code.service.work.WorkService;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AnglingRewardService
{
    private static Logger LOG = LoggerFactory.getLogger(AnglingRewardService.class);
    private UserCache userCache;

    public AnglingRewardService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 检测是否真實玩家
     *
     * @param userId 玩家编号
     * @return bool
     */
    private static boolean existTruestPlayer(String userId)
    {
        return !userId.startsWith(RobotService.startRobot());
    }

    /**
     * 创建奖励类型
     */
    public SendReward createRedBags()
    {
        BookInitService service = new BookInitService(userCache);
        AnglingRewardHit rewardHit = new AnglingRewardHit(userCache);
        AnglingExtraType currentType = rewardHit.hit();
        SendReward reward = new SendReward();
        reward.type = currentType;
        switch (currentType)
        {
            case takeshell:
            {
                reward.users = createTakeShell();
            }
            break;
            case guessbook:
            {
                if (service.getThisLevelBook(BookType.open).size() >= service.getThisLevelSystemBook().size())
                {
                    reward.type = AnglingExtraType.none;
                    return reward;
                } else
                {
                    reward.users = createGuessBook();
                    if (reward.users == null)
                        reward.type = AnglingExtraType.none;
                }
            }
            break;
            case none:
            default:
                break;
        }
        return reward;
    }

    /**
     * 创建偷贝壳奖励
     */
    private RewardUser createTakeShell()
    {
        RobotService robotService = new RobotService()
        {
            public Set<String> getTrustPlayer()
            {
                int trust = Systemstatusinfo.getInt("angling-tradeShell-trust");
                Set<String> userIdSet = new HashSet<>();
                Vector<PreviewDTO> userRand = FishInfoDao.getFillBook(userCache.getUserId(), "=", trust);
                if (!userRand.isEmpty())
                {
                    userIdSet = userRand.stream().map(dto -> String.valueOf(dto.userId)).collect(Collectors.toSet());
                }
                if (userIdSet.size() < trust)
                {
                    Vector<PreviewDTO> unUserRand = FishInfoDao.getFillBook(userCache.getUserId(), "<>", trust);
                    if (!unUserRand.isEmpty())
                    {
                        for (PreviewDTO dto : unUserRand)
                        {
                            userIdSet.add(String.valueOf(dto.userId));
                        }
                    }
                }
                return userIdSet;
            }

            @Override
            protected int getTotal()
            {
                return Systemstatusinfo.getInt("angling-tradeShell-limit");
            }

            @Override
            protected int getSize()
            {
                return 1;
            }
        };
        int basinId = userCache.getBasin();
        return robotService.getRewardUser(basinId).firstElement();
    }

    /**
     * 创建猜图鉴奖励
     */
    private RewardUser createGuessBook()
    {
        //筛选当前水域图鉴奖励
        BookInitService service = new BookInitService(userCache);
        Vector<Integer> thisLevelBook = service.getThisLevelBook(BookType.open, false);
        int basinId = userCache.getBasin();
        //当前水域 图鉴id
        //玩家当前水域已收集满
        if (thisLevelBook.isEmpty())
        {
            return null;
        }
        //筛选相同水域玩家 且被猜玩家当前水域图鉴未收集满
        Long guessUserId = FishInfoDao.getRandomGuessBook(basinId, thisLevelBook, userCache.getUserId(), 1);
        RewardUser book = new BasePro.RewardUser();
        if (guessUserId == null)
        {
            book = RobotService.createRobot(basinId);
            return book;
        }
        //如果有真实用户
        UserCache otherCache = UserCache.getUserCache(String.valueOf(guessUserId));
        if (otherCache != null)
        {
            if (otherCache.getBasin() != userCache.getBasin())
            {
                book = RobotService.createRobot(basinId);
                return book;
            }
            book.userid = String.valueOf(guessUserId);
            book.icon = otherCache.getIcon();
            book.nickname = otherCache.getNickName();
            book.basin = otherCache.getBasin();
        } else
        {
            book = RobotService.createRobot(basinId);
            return book;
        }
        LOG.debug("生成奖励！！" + XwhTool.getJSONByFastJSON(book));
        return book;
    }

    /**
     * 获取偷贝壳基数数据
     *
     * @param userId 用户编号
     * @return 倒叙结果
     */
    public long getTakeShellData(String userId)
    {
        if (userId == null || userId.isEmpty())
        {
            return 0;
        }
        long shell = userCache.shell();
        if (existTruestPlayer(userId))
        {
            UserCache cache = UserCache.getUserCache(userId);
            if (cache != null)
                return cache.shell();
        }
        return RobotService.createRobotShell(shell);
    }

    /**
     * 设置贝壳结果
     *
     * @param otherId ..
     * @return ..
     */
    public ERROR setTakeResult(String otherId, long shell)
    {
        userCache.incrShell(shell);
        //更新被偷人的贝壳
        if (existTruestPlayer(otherId))
        {
            boolean b = takeShell(otherId, shell);
            if (!b)
            {
                return ERROR.STEAL_FAIL;
            }
        }
        return ERROR.SUCCESS;//返回成功结果
    }

    /**
     * 增加被偷记录
     *
     * @param value 被偷的数值
     */
    public void addRecord(AnglingExtraType type, String otherId, String value, boolean success)
    {
        RecordSteal recordSteal = new RecordSteal();
        if (existTruestPlayer(otherId))
            recordSteal.setUserId(Long.valueOf(otherId));
        recordSteal.setStealUserId(userCache.userId());
        recordSteal.setSuccess(success);
        recordSteal.setValue(value);
        recordSteal.setUpdateTime(System.currentTimeMillis());
        recordSteal.setStealName(userCache.getNickName());
        switch (type)
        {
            case takeshell:
                recordSteal.setType(AnglingExtraType.takeshell.name());
                break;
            case guessbook:
            {
                recordSteal.setType(AnglingExtraType.guessbook.name());
                if (!success)
                    break;
                UserCache otherCache = UserCache.getUserCache(otherId);
                if (otherCache == null)
                    break;
                try
                {
                    StolenService.setUserStolenDetail(recordSteal);
                } catch (Exception e)
                {
                    LOG.error(Log4j.getExceptionInfo(e));
                }
            }
            break;
            default:
                recordSteal.setType(AnglingExtraType.none.name());
                break;
        }
        try
        {
            FishInfoDb.instance().saveOrUpdate(recordSteal, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 检查猜图鉴条件
     *
     * @return 猜图鉴状态
     */
    public BookResult checkGuessBook(String otherId) throws Exception
    {
        BookResult result = new BookResult();
        result.status = StealStatus.fail;
        if (otherId == null)
            return result;
        BookInitService service = new BookInitService(userCache);
        int basinId = userCache.getBasin();
        //可被点亮图鉴
        Vector<Integer> canLight = service.getThisLevelBook(BookType.open, false);
        if (canLight.isEmpty())
        {
            //当前无可点亮图鉴，默认失败,图鉴为该水域最后图鉴
            result.book = service.getThisLevelSystemBook().lastElement();
            return result;
        }
        //自然排序
        canLight.sort(Comparator.naturalOrder());
        boolean hit = ThreadLocalRandom.current().nextInt(100000) % 3 == 0;
        Integer ftId = canLight.firstElement();
        result.book = ftId;
        if (!hit)
        {
            return result;
        }
        UserCache otherCache = UserCache.getUserCache(otherId);
        //机器人，偷取成功
        if (otherCache == null)
        {
            setSuccessGuessBook(service, result.book);
            result.status = StealStatus.success;
            return result;
        }

        //检测好友保护
        FriendGuardService guardService = new FriendGuardService(otherCache);
        if ((result.friend = guardService.protectedFriend(ftId)) != null)
        {
            result.status = StealStatus.protect;
            return result;
        }
        //获取被偷玩家当前水域可被偷图鉴
        Vector<Integer> otherLight = new BookInitService(otherCache).getThisLevelBook(BookType.open);
        ConfigBasin basin = (ConfigBasin) FishInfoDb.instance().getCacheKey(ConfigBasin.class, new String[]{"basinId", String.valueOf(basinId)});
        //水域图鉴数=点亮图鉴数，禁止被偷
        if (basin.getArchiveName() == otherLight.size())
        {
            return result;
        }
        boolean success = false;
        for (int light : canLight)
        {
            if (otherLight.contains(light))
            {
                ftId = light;
                success = true;
                break;
            }
        }
        if (!success)
        {
            //偷取失败
            return result;
        }
        LOG.debug("偷取图鉴成功:" + userCache.getUserId() + ",ftId=" + ftId + ",userList="
                + XwhTool.getJSONByFastJSON(canLight)
                + ",otherList=" + XwhTool.getJSONByFastJSON(otherLight));
        result.book = ftId;
        result.status = StealStatus.success;
        //设置被偷用户数据
        setStolenData(otherCache, ftId);
        //设置偷取成功数据
        setSuccessGuessBook(service, ftId);
        return result;
    }

    /**
     * 设置偷取成功图鉴
     *
     * @param ftId 偷取图鉴编号
     */
    private void setSuccessGuessBook(BookInitService bookService, int ftId)
    {
        //更新玩家
        bookService.updateUserBook(ftId, BookType.open);
        //进度更新
        try (WorkService workService = new WorkService(userCache))
        {
            workService.addProcess(OperatorBase.ActivityType.stealBook, 1);
        }
        //添加猜图鉴
        new AchievementService(userCache).addAchievement(AchievementType.StealBookSuccess, 1);
    }

    /**
     * 设置猜图鉴结果
     *
     * @param otherCache 被猜用户
     * @param ftId       猜中图鉴
     */
    private void setStolenData(UserCache otherCache, int ftId)
    {
        if (otherCache == null)
            return;
        BookInitService service = new BookInitService(otherCache);
        //更新被猜
        service.updateUserBook(ftId, BookType.stolen);
        //添加邮件数据
        ConfigFish configFish = (ConfigFish) FishInfoDb.instance().getCacheKey(ConfigFish.class, new String[]{"ftId", String.valueOf(ftId)});
        String bookName = configFish.getTypeName();
        NoticeService noticeService = new NoticeService(otherCache);
        ConfigEmail configEmail = NoticeService.getConfigEmail(OperatorBase.MessageType.stealBook);
        String context = String.format(configEmail.getMsg(), userCache.getNickName(), bookName);
        JSONObject json = new JSONObject();
        json.put("userId", userCache.getUserId());
        noticeService.addMessageInfo(json, context, configEmail);
    }

    /**
     * 更新贝壳数据
     *
     * @param userId 用户id
     * @param shell  贝壳量
     * @return 是否成功更新
     */
    private boolean takeShell(String userId, long shell)
    {
        UserCache cache = UserCache.getUserCache(userId);
        if (cache != null)
        {
            if (cache.incrShell(-shell))
            {
                //添加邮件数据
                NoticeService service = new NoticeService(cache);
                ConfigEmail configEmail = NoticeService.getConfigEmail(OperatorBase.MessageType.stealShell);
                String context = String.format(configEmail.getMsg(), userCache.getNickName(), String.valueOf(shell));
                JSONObject json = new JSONObject();
                json.put("userId", userCache.getUserId());
                service.addMessageInfo(json, context, configEmail);
                return true;
            }
        }
        return false;
    }

    /**
     * 补偿贝壳
     *
     * @return ..
     */
    public int getSubsidy()
    {
        return Systemstatusinfo.getInt("angling_subsidy", "100");
    }


}
