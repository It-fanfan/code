package com.code.protocols.core.angling;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.core.AnglingBase.FishingCostType;
import com.code.protocols.core.AnglingBase.FishingRod;

/**
 * 垂钓鱼竿道具协议
 */
public class AnglingRod
{

    //进行购买恢复耐力值
    public static class RequestImpl extends AbstractRequest
    {
        public FishingCostType costtype;
        //视频是否达上限
        public boolean videolimit;
    }

    //下发恢复情况
    public static class ResponseImpl extends AbstractResponse
    {
        public FishingRod rod;
    }
}
