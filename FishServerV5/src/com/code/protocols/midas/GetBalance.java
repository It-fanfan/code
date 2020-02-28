package com.code.protocols.midas;

import com.code.protocols.AbstractResponse;

public class GetBalance
{
    public static class RequestImpl
    {
        //申请时间
        public String applyTime;
        public String openid;
        public String sessionKey;
        public String appId;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        public int errcode;//	错误码
        public String errmsg;//	错误信息
        public int balance;//	游戏币个数（包含赠送）
        public int gen_balance;//赠送游戏币数量（赠送游戏币数量）
        public boolean first_save;//	是否满足历史首次充值
        public int save_amt;//	累计充值金额的游戏币数量
        public int save_sum;//	历史总游戏币金额
        public int cost_sum;//历史总消费游戏币金额
        public int present_sum;//	历史累计收到赠送金额
    }

}
