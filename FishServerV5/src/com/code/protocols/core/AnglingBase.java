package com.code.protocols.core;

import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.angling.DrifterConfig;
import com.code.protocols.operator.achievement.AchievementType;
import com.utils.XWHMathTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.code.protocols.basic.BasePro.RewardUser;
import static com.code.protocols.operator.achievement.AchievementType.*;

/**
 * 垂钓基本协议逻辑
 */
public class AnglingBase
{

    // 不能钓鱼
    public static final String[] UN_FISH_TIP = new String[]{"鱼脱钩了，什么也没有钓到", "钓鱼是个技术活，要多练练", "竹篮打水一场空，换个厉害的鱼钩！"};
    //贝壳鱼编号
    public static final int SHELL_FISH_ID = 2;

    /**
     * 更新下发异常码
     *
     * @param error 异常码数据
     * @param res   下发节点
     */
    public static void updateResponseCode(ERROR error, AbstractResponse res)
    {
        res.code = error.getCode();
        res.msg = error.getMsg();
    }

    public enum ERROR
    {
        SUCCESS(200, "成功"),
        UNSATISFIED(300, "很抱歉，您不满足领取条件!"),
        VIDEO_EXIST(301, "视频任务未完成"),
        VIDEO_FULL(302, "视频活动已经结束"),
        SHELL_LACKING(303, "贝壳不足"),
        NO_ALLOW(304, "当前不满足条件"),
        NO_MATCH_ANGLING(305, "你还未曾点亮过图鉴，不能被钓哦~"),
        NO_ORDER(306, "订单不存在"),
        NO_CD(307, "CD期中，不满足要求"),
        NO_BASIN(308, "当前水域未收集全"),
        NO_WATER(309, "没有水域信息"),
        ERROR(310, "系统异常请重新刷新"),
        NO_BOOK(311, "不存在图鉴"),
        NO_LIGHT(312, "图鉴无法点亮"),

        FRINED_GUARD(313, "好友守护失败"),
        CHANCE_FAIL(314, "概率失败"),
        STEAL_FAIL(315, "偷取失败"),
        BOOK_FILL(316, "被偷人图鉴已开满"),
        LACK_MATERIAL(317, "材料不足"),
        PEARL_LACKING(318, "珍珠不足"),
        TRY_AGAIN(319, "操作过于频繁，请稍后再试");


        private int code;
        private String msg;

        ERROR(int code, String msg)
        {
            this.code = code;
            this.msg = msg;
        }

        public int getCode()
        {
            return code;
        }

        public String getMsg()
        {
            return msg;
        }
    }

    /**
     * 垂钓额外类型
     */
    public enum AnglingExtraType
    {
        takeshell(30),//偷贝壳
        guessbook(30),//猜图鉴
        none(40); //无奖励

        private long value;

        AnglingExtraType(long value)
        {
            this.value = value;
        }

        public long getValue()
        {
            return this.value;
        }}

    /**
     * 消耗补充类型
     */
    public enum FishingCostType
    {
        shell,//贝壳
        video,//视频

    }

    /**
     * 偷取状态
     */
    public enum StealStatus
    {
        protect,//保护
        success,//成功
        fail//失败
    }

    public enum OperationType
    {
        perfect(1, FishingPerfectCombo),//完美
        good(2, FishingGood),//不错
        great(3, none);//真棒

        private int value;
        private AchievementType achievementType;

        OperationType(int value, AchievementType achievementType)
        {
            this.value = value;
            this.achievementType = achievementType;
        }

        public AchievementType getAchievementType()
        {
            return achievementType;
        }

        public int getValue()
        {
            return value;
        }
    }

    public interface Result
    {
    }

    public static class GameStatus
    {
        public OperationType operation;//游戏状态
        public int surplus;//剩余条数
        public int doublehit;//连击次数
        public Map<String, Integer> props;//道具鱼使用
        public Map<String, Integer> combos;//combo情况
    }

    public static class ShellResult implements Result
    {
        //奖励贝壳数
        public int shell;
        //抽取玩家贝壳数
        public String othershell;
    }

    public static class BookResult implements Result
    {
        //偷取结果
        public StealStatus status = StealStatus.fail;
        //偷取图鉴
        public int book;
        //保护玩家
        public String friend;
        //额外贝壳奖励
        public int shell;
    }

    public static class MaterialResult implements Result
    {
        public int id;//材料编号
        public int count;//数量

        public MaterialResult()
        {
        }

        private MaterialResult(int id, int count)
        {
            this.id = id;
            this.count = count;
        }
    }

    public static class RefreshConfig
    {
        //刷新CD周期
        public long cd;
        //消耗珍珠
        public int pearl;
    }

    /**
     * 漂流瓶触发
     */
    public static class DrifterData
    {
        //索引编号
        public int index;
        //触发条件
        public DrifterConfig.Trigger trigger;
        //触发奖励
        public BasePro.RewardInfo reward;
    }


    /**
     * 垂钓结果鱼信息
     */
    public static class FishResult
    {
        //色值
        //鱼类型
        public int ftid;
        //数量
        public int sum;

        public FishResult()
        {
        }

        public FishResult(int ftid, int sum)
        {
            this.ftid = ftid;
            this.sum = sum;
        }

        public int getFtid()
        {
            return ftid;
        }
    }

    public static class SendReward
    {
        //奖励类型
        public AnglingExtraType type;
        //奖励用户
        public RewardUser users;
        //材料编号
        public int id;
    }

    /**
     * 鱼竿协议
     */
    public static class FishingRod
    {
        //耐力值
        public int endurance;
        //视频消耗剩余数
        public int video;
        //上次操作时间
        public long uploadtime;
        //操作日期
        public int dayflag;
    }

    /**
     * 鱼竿配置信息
     */
    public static class FishingRodConfig
    {
        //{"endurance":5,"frequency":3,"video":3,"basin":[200,500,1000,1500,2000]}
        //耐力值
        public int endurance;
        //恢复频率(单位:分)
        public int frequency;
        //每天视频次数
        public int video;
        //消耗信息
        public Vector<Integer> basin;
    }

    public static class OrderIntroduced
    {
        public int index;
        //bossId
        public int bossid;
        //introduce
        public int introduce;
        //奖励数据
        public Vector<BasePro.RewardInfo> rewards;
        //价格
        public int price;
        //上次刷新时间
        public String refresh;
    }

    public static class OrderInfo
    {
        //更新订单
        public OrderIntroduced order;
        //订单鱼数据
        public BigData orderfishes;
    }

    //视频订单
    public static class OrderVideo
    {
        //当前次数
        public int times;
        public Vector<BasePro.RewardInfo> rewards;
    }

    /**
     * 船舵配置信息
     */
    public static class AnglingInit
    {
        //材料配置vector<MaterialResult>
        public BigData materials;
        //偷贝壳配置参数
        public Vector<StealShellConfig> stealshell;
        //道具-贝壳鱼获取数量
        public int shellfishprop;
        //垂钓道具数据
        public FishingRodConfig rodconfig;
        //大转盘配置
        public BigWheel wheel;
        //垂钓道具信息
        public BigData props;
    }

    /**
     * 用户垂钓相关数据
     */
    public static class AnglingUser
    {
        //用户船舵数据
        public Vector<Integer> rudders = new Vector<>();
        //私人数据
        public FishingRod rod = new FishingRod();
        //获取玩家上次垂钓的状态
        public Map<String, Integer> operation = new HashMap<>();
        //道具数量
        public Map<String, Integer> props = new HashMap<>();
    }

    /**
     * 偷取贝壳配置angling-tradeShell-reward
     */
    public static class StealShellConfig
    {
        //索引
        public int index;
        //获取比例
        public double rate;
        //获取贝壳数
        public int shell;
        //区域分布
        public double per;
        //偷取上限
        public int limit;

        public StealShellConfig()
        {
        }

        public StealShellConfig(int index, double rate, int shell, double per, int limit)
        {
            this.index = index;
            this.rate = rate;
            this.shell = shell;
            this.per = per;
            this.limit = limit;
        }

        public int getHit(long userShell)
        {
            if (rate <= 0)
                return this.shell;
            int shell = XWHMathTool.multiply(rate, userShell).intValue();
            if (shell > limit)
                shell = limit;
            if (shell <= 0)
                shell = this.shell;
            return shell;
        }
    }

    /**
     * 大转盘配置
     */
    public static class BigWheel
    {
        //图鉴点亮数 大于>
        public int booklimit;
        //概率
        public double rate;
    }
}
