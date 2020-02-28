package com.code.protocols.core.video;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

import java.util.Vector;

public class Video
{

    public enum VideoType
    {
        rod,//鱼竿
        order,//订单
    }

    public static class VideInfo
    {
        //视频编号
        public int id;
        //视频奖励
        public Vector<BasePro.RewardInfo> rewards;
        //视频次数
        public int total;
    }

    public static class RequestImpl extends AbstractRequest
    {
        //视频类型
        public VideoType type;
        //编号
        public int id;
        //视频次数
        public int total;
    }

    public static class ResponseImpl extends AbstractResponse
    {

    }
}
