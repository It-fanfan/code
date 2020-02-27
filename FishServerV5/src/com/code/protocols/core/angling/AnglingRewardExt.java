package com.code.protocols.core.angling;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

import java.util.Map;

import static com.code.protocols.core.AnglingBase.AnglingExtraType;
import static com.code.protocols.core.AnglingBase.Result;

/**
 */
public class AnglingRewardExt
{
    public static class RequestImpl extends AbstractRequest
    {
        //上报奖励数据
        //奖励类型
        public AnglingExtraType type;
        //奖励索引
        public int index;
        //抽取玩家信息
        public String otherid;
        //使用道具
        public int propid;
    }

    public static class ResponseV2Impl extends AbstractResponse
    {
        //奖励类型
        public AnglingExtraType type;
        //奖励节点
        public Result result;
        //道具数量更新,仅使用后再处理
        public Map<String, Integer> props;
    }
}
