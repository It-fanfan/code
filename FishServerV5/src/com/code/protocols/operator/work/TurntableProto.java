package com.code.protocols.operator.work;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

/**
 * 大转盘开始协议 URI: /turntable/play
 */
public class TurntableProto
{
    public static class RequestImpl extends AbstractRequest
    {
        /**
         * 类型(0: 免费  1: 视频广告 2: 珍珠)
         */
        public int type;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        /**
         * 免费次数
         */
        public Integer freecount;

        /**
         * 通过广告获取次数
         */
        public Integer adcount;

        /**
         * 奖励id
         */
        public int rewardid;
    }

}
