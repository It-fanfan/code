package com.code.protocols.sdk;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.login.LoginRequest;

public class Huawei
{
    public static class Pay extends AbstractRequest
    {
        public String merchantId;
        public String applicationID;
        public String amount;
        public String productName;
        public String productDesc;
        public String requestId;
        public int sdkChannel;
        public String urlver;
        public int goodsid;
    }

    public static class PaySignResponse extends AbstractResponse
    {
        public String sign;
    }

    /**
     * 支付回调
     */
    public static class PayRequestCallback extends AbstractRequest
    {
        public String requestid;
        public boolean success;
        //华为订单编号
        public String orderid;
        //异常信息
        public String errormsg;
    }

    /**
     * 支付回调
     */
    public static class PayResponseCallback extends AbstractResponse
    {
    }


    public static class RequestImpl extends LoginRequest
    {
        //用户的openid
        public String openid;
        //用户在开放平台上的唯一标示符
        public String unionid;
        //用户的昵称
        public String nickname;
        //图片地址
        public String avatar;
    }
}
