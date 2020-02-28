package com.code.protocols.operator.utils;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

import java.util.HashMap;
import java.util.Map;

/**
 * 邀请列表  url:/add/inviteList
 */
public class InviteList
{
    public static class RequestImpl extends AbstractRequest
    {
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //玩家邀请数据<OperatorBase.Invite>
        public BigData invites;
        //初始配置信息
        public Map<String, Integer> receives = new HashMap<>();
    }

}
