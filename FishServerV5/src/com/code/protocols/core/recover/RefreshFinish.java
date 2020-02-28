package com.code.protocols.core.recover;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

/**
 * 接单结果协议
 */
public class RefreshFinish
{
    public static class RequestImpl extends AbstractRequest
    {
        //订单编号
        public int index;
        //消耗金额
        public int cost;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        public int cost;
    }
}
