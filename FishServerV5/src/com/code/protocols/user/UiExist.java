package com.code.protocols.user;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

import java.util.Map;
import java.util.Vector;

/**
 * UI检测
 */
public class UiExist
{
    public enum UIType
    {
        order,//订单
        invite,//邀请
        activity,//日常
        email,//邮件
        turntable, //转盘免费
        friend,//好友
        achievement,//成就
    }

    public static class RequestImpl extends AbstractRequest
    {
        public Vector<UIType> ui;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //标志信息
        public Map<UIType, Boolean> flags;
    }
}
