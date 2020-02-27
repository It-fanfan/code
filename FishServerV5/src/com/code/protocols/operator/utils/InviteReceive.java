package com.code.protocols.operator.utils;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

/**
 * 邀请奖励  url:/add/invite
 */
public class InviteReceive
{
    public static class RequestImpl extends AbstractRequest
    {
        //领取索引
        public int index;
    }

    public static class ResponseImpl extends AbstractResponse
    {
    }
}
