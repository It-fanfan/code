package com.code.protocols.core.recover;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

import static com.code.protocols.core.AnglingBase.OrderInfo;

/**
 * 刷新订单协议  usl: /order/refresh
 */
public class RefreshExt
{
    public static class RequestImpl extends AbstractRequest
    {
        //订单索引
        public int index;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //更新订单
        public OrderInfo order;
    }
}
