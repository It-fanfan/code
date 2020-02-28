package com.code.protocols.sdk;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.login.LoginRequest;

public class Vivo
{

    public static class PayRequest extends AbstractRequest
    {
        public int goodsid;
        public int total;
    }

    public static class PaySignResponse extends AbstractResponse
    {
        public String param;
    }

    /**
     * 支付回调
     */
    public static class PayRequestCallback extends AbstractRequest
    {
        public String requestid;
        public boolean success;
        //vivo订单编号
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
        //用户的id
        public String id;
        //用户在开放平台上的唯一标示符
        public String unionid;
        //用户的昵称
        public String nickname;
        //图片地址
        public String avatar;
    }
}
