package com.code.service.gm;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.robot.ConfigRobot;
import com.code.protocols.basic.BasePro;
import com.utils.XWHMathTool;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 机器人服务
 */
public abstract class RobotService
{
    private static long refreshTime = 0;
    private static Vector<ConfigRobot> robots = null;

    /**
     * 获取机器人配置
     *
     * @return 查询
     */
    private static ConfigRobot getRobot()
    {
        if ((System.currentTimeMillis() - refreshTime > 1000 * 60 * 30))
        {
            refreshTime = System.currentTimeMillis();
            String SQL = "select * from config_robot";
            robots = FishInfoDb.instance().findBySQL(SQL, ConfigRobot.class);
        }
        int index = ThreadLocalRandom.current().nextInt(robots.size());
        return robots.elementAt(index);
    }

    /**
     * 机器人
     *
     * @param basinId 水域信息
     * @param extra   例外的编号
     * @return 玩家
     */
    public static BasePro.RewardUser createRobot(int basinId, Set<String> extra)
    {
        if (extra == null || extra.isEmpty())
            return createRobot(basinId);
        String[] extras = new String[extra.size()];
        extras = extra.toArray(extras);
        return createRobot(basinId, extras);
    }

    /**
     * 机器人
     *
     * @param basinId 水域信息
     * @param extra   例外的编号
     * @return 玩家
     */
    public static BasePro.RewardUser createRobot(int basinId, String... extra)
    {
        BasePro.RewardUser user = new BasePro.RewardUser();
        user.basin = basinId;
        while (true)
        {
            ConfigRobot robot = getRobot();
            user.userid = startRobot() + robot.getRobotId();
            if (extra != null)
            {
                String value = Arrays.stream(extra).filter(id -> id.equals(user.userid)).findFirst().orElse(null);
                if (value != null)
                    continue;
            }
            user.nickname = robot.getRobotName();
            user.icon = robot.getIcon();
            user.robot = true;
            return user;
        }
    }

    /**
     * 機器人标志
     *
     * @return str
     */
    public static String startRobot()
    {
        return "robot-";
    }

    /**
     * 机器人贝壳数据
     *
     * @param shell 玩家贝壳
     * @return 值
     */
    public static long createRobotShell(long shell)
    {
        double rate = ThreadLocalRandom.current().nextDouble(0.5, 1.1);
        return XWHMathTool.multiply(shell, rate).longValue();
    }

    /**
     * 获取真实玩家节点
     *
     * @return 真实玩家节点信息
     */
    protected abstract Set<String> getTrustPlayer();

    /**
     * 获取池子数量
     *
     * @return 池子总量
     */
    protected abstract int getTotal();

    /**
     * 实际需要数量
     */
    protected abstract int getSize();

    /**
     * 获取奖励数据
     *
     * @return
     */
    public Vector<BasePro.RewardUser> getRewardUser(int basinId)
    {
        Vector<String> pool = new Vector<>(getTrustPlayer());
        if (pool.size() < getTotal())
        {
            int size = pool.size();
            for (int i = 0; i < getTotal() - size; i++)
            {
                pool.add(startRobot());
            }
        }
        Collections.shuffle(pool);
        Vector<BasePro.RewardUser> users = new Vector<>();
        Set<String> robotIds = new HashSet<>();
        for (int i = 0; i < getSize(); i++)
        {
            String userId = pool.elementAt(i);
            BasePro.RewardUser take;
            if (userId.equals(startRobot()))
            {
                take = createRobot(basinId, robotIds);
                robotIds.add(take.userid);
            } else
            {
                //获取玩家个人信息
                UserCache cache = UserCache.getUserCache(userId);
                if (cache != null && cache.shell() > 0)
                {
                    take = new BasePro.RewardUser();
                    take.userid = userId;
                    take.icon = cache.getIcon();
                    take.nickname = cache.getNickName();
                    take.basin = cache.getBasin();
                } else
                {
                    take = createRobot(basinId, robotIds);
                    robotIds.add(take.userid);
                }
            }
            users.add(take);
        }
        return users;
    }
}
