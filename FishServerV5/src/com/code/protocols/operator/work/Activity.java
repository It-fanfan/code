package com.code.protocols.operator.work;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;

import java.util.List;

/**
 * 活跃任务
 */
public class Activity
{

    //任务列表
    //URI:/work/activityList
    public static class ListRequest extends AbstractRequest
    {
        public OperatorBase.WorkType worktype;
    }

    public static class ListResponse extends AbstractResponse
    {
        //任务列表
        public BigData worklist;
        //宝箱列表
        public BigData caselist;
        //签到
        public BigData signuplist;
        //领取信息
        public OperatorBase.ActivityReceive receive;
    }

    //领取奖励
    //URI:/work/receive
    public static class ReceiveRequest extends AbstractRequest
    {
        //任务奖励
        public OperatorBase.WorkType worktype;
        //任务ID
        public int workid;
    }

    public static class ReceiveResponse extends AbstractResponse
    {
        //周活跃值
        public Integer activity;
        //更新数据
        public List<Integer> process;
    }
}
