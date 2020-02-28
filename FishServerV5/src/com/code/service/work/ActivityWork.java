package com.code.service.work;

import com.code.protocols.operator.OperatorBase;

import java.util.Map;
import java.util.Vector;

/**
 * 活跃任务
 */
public class ActivityWork
{
    //周标签
    public int weekFlag;
    //活跃标签
    public Vector<DayActivity> activities;
    //当前活跃值
    public int activityValue;
    //当前签到值
    public Vector<Integer> signup = new Vector<>();
    //领取宝箱值
    public Vector<Integer> caseIds;

    public static class DayActivity
    {
        //日期
        public int dayFlag;
        //进度
        public Map<OperatorBase.ActivityType, Integer> activityMap;
        //领取任务编号
        public Vector<Integer> workList;
        //每日领取转盘次数
        public Map<Integer, Integer> turntable;
    }

}
