package com.code.protocols.core.recover;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase;

import java.util.Vector;

import static com.code.protocols.core.AnglingBase.OrderIntroduced;

/**
 * 订单初始化协议  usl: /order/init
 */
public class FishOrderInitExt
{
    public static class RequestImpl extends AbstractRequest
    {

    }

    public static class ResponseImpl extends AbstractResponse
    {
        //所有订单
        public Vector<OrderIntroduced> orders;
        //订单鱼数据
        public BigData fishes;
        //时间 上次换单时间记录
        public String refresh;
        //上次接单时间记录
        public String finish;
        //视频订单
        public AnglingBase.OrderVideo ordervideo;
    }
}
