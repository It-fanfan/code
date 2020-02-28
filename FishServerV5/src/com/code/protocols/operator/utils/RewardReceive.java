package com.code.protocols.operator.utils;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

public class RewardReceive
{
    //URI:/add/rewardReceive
    public static class RequestImpl extends AbstractRequest
    {
        //领取索引
        public String appid;
    }

    public static class ResponseImpl extends AbstractResponse
    {
    }

    /**
     * 该协议，默认完成
     */
    //URI:/add/receiveUtils
    public static class UtilRequestImpl extends AbstractRequest
    {
        //附加類型
        public UtilsType type;
        //记录试玩AppId
        public String appid;
    }

}
