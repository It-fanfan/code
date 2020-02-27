package com.code.protocols.operator;

import com.code.protocols.AbstractResponse;
import com.code.protocols.operator.utils.Member;

import java.util.List;
import java.util.Map;

/**
 * 运营 Base
 */
public class OperatorBase
{
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
        ERROR(999, "未知异常"),
        UNSATISFIED(700, "很抱歉，您不满足领取条件!"),
        SIGNUP_REPEAT(701, "重复签到"),
        TURNTABLE_UN_COUNT(702, "次数不足"),
        TURNTABLE_IS_NULL(703, "转盘数据为Null"),
        MESSAGE_IS_ERROR(705, "当前领取未达要求!"),
        LIMIT_REACHED(704, "您已达上限，不能继续领取奖励"),
        NOTQUALIFIED(705, "当前还未达标"),
        REPEATRECEIVE(706, "重复领取"),
        UNDEFINED(707, "已过期或者不存在成就"),
        RECEIVEFAIL(708, "成就领取失败"),
        COIN_UNENOUGH(709, "余额不足"),
        UNDEFINE_GOODS(710, "定位不到商品信息"),
        FAIL_BUY(711, "购买失败，余额不足!"),
        PLATFORM_NONSUPPORT(712, "不是微信玩家請求，不能使用！"),
        UNDEFINE_ORDER(713, "预订单不存在"),
        VIRTUAL_FAIL(714, "虚拟币验证失败"),
        ;
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

    public enum WorkType
    {
        all,//全部
        activity,//活跃
        casework,//宝箱任务
        signup,//签到
    }

    /**
     * 活跃类型
     */
    public enum ActivityType
    {
        angling,//垂钓
        rod,//鱼竿修复
        fishSize,//垂钓鱼数量
        fishProp,//垂钓鱼道具
        stealBook,//偷图鉴Success
        stealShell,//偷贝壳perfect
        order,//订单
        orderGet,//订单获取金额
        openBasin,//开水域
        openBook,//开图鉴
        share,//分享
        video,//视频
        addFriend,//添加好友
        invite,//邀请
        friendProtected,//好友保护
        shop,//商城

    }

    public enum ButtonType
    {
        none,//空
        receive,//领取
        leave,//前往
    }

    public enum LeaveType
    {
        none,//空
        book,//新图鉴
        basin,//海域
        WishingWell,//许愿池
        shop,//商店
        achievement,//成就
        daily,//日常
        activity,//活动
        order,//订单
    }

    //消息类型
    public enum MessageType
    {
        system,//系统
        friendProtected,//好友保护
        stealBook,//偷图鉴
        stealShell,//偷贝壳
        present,//赠送贝壳
    }

    //领取信息
    public static class ActivityReceive
    {
        //当前周
        public int weekflag;
        //周活跃值
        public int activity;
        //签到日期
        public List<Integer> sign;
        //箱子开启
        public List<Integer> caseopen;
        //任务领取
        public List<Integer> work;
        //任务进度
        public Map<String, Integer> workprocess;
    }

    public static class BehaviorInfo
    {
        // 行为类型
        public String type;
        // 扩展字段
        public String ext;
        // 操作时间
        public long times;
    }

    public static class Invite extends Member
    {
        //注册时间
        public long register;
        //是否领取
        public boolean receive;
        //领取的金额
        public long price;
    }

    public static class WelfareInfo
    {
        //上限
        public int limit;
        //价值
        public int price;
    }

    public static class MessageInfo
    {
        //邮件编号
        public String id;
        //时间
        public long times;
        //消息类型
        public MessageType type;
        //标题
        public String title;
        //内容
        public String context;
        //图标
        public String icon;
        //奖励
        public String reward;
        //button类型
        public ButtonType button = ButtonType.none;
        //前往类型
        public LeaveType leave = LeaveType.none;
    }

    /**
     * 玩家当前成就
     */
    public static class AchievementInfo
    {
        //成就编号
        public int id;
        //成就状态:receive|standard|no
        public String status;
        //当前进度
        public int process;
    }
}
