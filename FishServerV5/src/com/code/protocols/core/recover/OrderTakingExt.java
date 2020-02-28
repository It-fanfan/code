package com.code.protocols.core.recover;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

import static com.code.protocols.core.AnglingBase.OrderInfo;

/**
 * 接单结果协议
 */
public class OrderTakingExt
{
    public static class RequestImpl extends AbstractRequest
    {
        //订单编号
        public int index;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //更新订单
        public OrderInfo order;
        //接单时间
        public String finish;
        //用户鱼数据
        public BigData userfishes;
    }
}
