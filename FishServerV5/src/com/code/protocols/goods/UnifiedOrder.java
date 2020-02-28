package com.code.protocols.goods;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

/**
 * 订单申请
 */
public class UnifiedOrder
{
    public static class RequestImpl extends AbstractRequest
    {
        //商品类型
        public BasePro.RewardType goodstype;
        //商品编号
        public int goodsid;
        //份数
        public int total;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //是否满足历史首次充值
        public boolean first;
        //虚拟币数值
        public int virtualvalue;
        //当前玩家虚拟币数值
        public int currentvirtual;
        //订单编号信息
        public String orderid;
    }
}
