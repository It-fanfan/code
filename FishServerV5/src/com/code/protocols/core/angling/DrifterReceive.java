package com.code.protocols.core.angling;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

/**
 * 漂流瓶领取
 */
public class DrifterReceive
{
    public static class RequestImpl extends AbstractRequest
    {
        //领取漂流瓶索引
        public int index;
    }

    public static class ResponseImpl extends AbstractResponse
    {

    }
}
