package com.code.protocols.social;

import com.code.protocols.AbstractResponse;
import com.code.service.trade.MarketService;

public class SocialBase
{
    /**
     * 设置结果集
     *
     * @param error 异常数据
     * @param res   配置
     */
    public static void putResponse(ERROR error, AbstractResponse res)
    {
        res.code = error.getCode();
        res.msg = error.getMsg();
    }

    //异常码信息
    public enum ERROR
    {
        SUCCESS(200, "成功"),
        NOTFRIEND(700, "你们还不是好友哦，要不要认识呢~"),
        ILLEGALPRESENTED(701, "非法赠送~"),
        UNDEFINE_USER(702, "好友不存在，添加失败！"),
        ONESELF(703, "不能申请给自己申请"),
        DUPLICATE(704, "重复申请"),
        ERROR_STATUS(705, "用户不存在"),
        NO_FRIEND(706, "当前不是你的好友"),
        GUARD_REPEAT(707, "好友重复守护"),
        GUARD_LIMIT(708, "好友守护已达上限"),
        GUARD_FAIL(709, "守护失败 请重新刷新页面"),
        NO_CD(710, "CD期中，不满足要求"),
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

    public interface APPLY
    {

        String DESTINED = "遇见彼此，就是我们的缘，有兴趣认识我吗~";
        String SAME = "你们有共同好的好友哦~";
    }

    public static class PresentedConfig
    {
        public int limit;
        public int value;
        public MarketService.WealthType costtype;
    }
}
