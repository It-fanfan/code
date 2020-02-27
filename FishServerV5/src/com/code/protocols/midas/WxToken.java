package com.code.protocols.midas;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;

/**
 * 獲取微信token
 */
public class WxToken
{
    public static class RequestImpl extends AbstractRequest
    {
        public String appId;
        public String appSecret;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        public String wxToken;
    }
}
