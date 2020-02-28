package com.code.protocols.operator.achievement;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

/**
 * 成就list
 */
public class AchievementList
{
    public static class RequestImpl extends AbstractRequest
    {

    }

    public static class ResponseImpl extends AbstractResponse
    {
        //成就系统
        public BigData achievement;
        //成就类型配置
        public BigData config;
        //成就数据:OperatorBase.AchievementInfo
        public BigData userachievement;
    }
}
