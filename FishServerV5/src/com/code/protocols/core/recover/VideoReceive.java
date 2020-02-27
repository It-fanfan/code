package com.code.protocols.core.recover;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.core.AnglingBase;

/**
 * 视频领取
 */
public class VideoReceive
{

    public static class RequestImpl extends AbstractRequest
    {
        //当前领取次数
        public int times;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //玩家当前贝壳
        public String shell;
        //玩家当前珍珠
        public String pearl;
        //玩家当前广告订单
        public AnglingBase.OrderVideo ordervideo;
    }
}
