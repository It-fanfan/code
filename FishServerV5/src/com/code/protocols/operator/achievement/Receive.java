package com.code.protocols.operator.achievement;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

/**
 * 领取成就
 */
public class Receive
{

    public static class RequestImpl extends AbstractRequest
    {
        //成就编号
        public int id;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //刷新成就数据
        public BigData achievement;
    }
}
