package com.code.protocols.operator.work;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.goods.CostType;

import java.util.Vector;

/**
 * 大转盘列表协议 URI: /turntable/list
 */
public class TurntableListProto
{
    public static class RequestImpl extends AbstractRequest
    {

    }

    public static class ResponseImpl extends AbstractResponse
    {
        /**
         * 转盘列表
         */
        public Vector<Turntable> turntables;

        /**
         * 开启条件(不足次数)
         */
        public TurntableCondition condition;
    }

    public static class Turntable
    {
        /**
         * id
         */
        public int id;

        /**
         * 名称
         */
        public String name;

        /**
         * 数量
         */
        public int num;

        /**
         * icon
         */
        public String icon;
    }

    /**
     * 转盘条件
     */
    public static class TurntableCondition
    {
        /**
         * 消耗类型
         */
        public CostType costtype;
        /**
         * 消耗数值
         */
        public int num;
    }

    //转盘配置
    public enum TurntableType
    {
        none(-1),//空
        free(0),//免费
        ad(1),//视频
        cost(2);//消耗

        TurntableType(int val)
        {
            this.val = val;
        }

        private int val;

        public int getVal()
        {
            return val;
        }

        public static TurntableType getType(int val)
        {
            if (val == free.val)
                return free;
            if (val == ad.val)
                return ad;
            if (val == cost.val)
                return cost;
            return none;
        }
    }

    public static class TurntableConfig
    {
        //视频次数
        public int ad;
        //免费次数
        public int free;
        //消耗条件
        public TurntableCondition condition;
    }
}
