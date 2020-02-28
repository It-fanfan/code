package com.code.protocols.goods;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

import java.util.Vector;

public class Shop
{

    public enum GoodsLabel
    {
        first,//首充(互斥商品)
        hot,//热销
        discount,//折扣
        newest,//新品
    }

    public static class ListRequestImpl extends AbstractRequest
    {

    }

    public static class ListResponseImpl extends AbstractResponse
    {
        //付费商品
        public Vector<GoodsBasic> valuegoods = new Vector<>();
        //平常商品
        public Vector<GoodsBasic> dailygoods = new Vector<>();
        //当前时间
        public long currenttime = System.currentTimeMillis();
    }

    public static class GoodsBasic
    {
        //商品编号
        public int goodsid;
        //商品名称
        public String goodsname;
        //商品ICON
        public String icon;
        //支付类型
        public CostType costtype;
        //单价
        public int price;
        //标签:first:首充(互斥商品),hot:热销,discount:折扣,newest:新品
        public String labalurl;
        //开始时间
        public long start;
        //截至时间
        public long end;
        // 折扣
        public int discount;
        //商品类型
        public BasePro.RewardType goodstype;
    }

    public static class BuyRequestImpl extends AbstractRequest
    {
        public BasePro.RewardType goodstype;//商品类型
        public int goodsid;//商品编号
        public int total;//商品份数
    }

    public static class VirtualBuyRequstImpl extends AbstractRequest
    {
        public String orderid;
        public boolean success;
    }

    public static class BuyResponseImpl extends AbstractResponse
    {
    }
}
