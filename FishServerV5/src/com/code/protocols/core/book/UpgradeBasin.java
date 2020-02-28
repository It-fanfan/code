package com.code.protocols.core.book;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro.Basin;

/**
 * 水域升级协议 url:book/leveladd/
 */
public class UpgradeBasin
{
    public static class RequestImpl extends AbstractRequest
    {

    }

    public static class ResponseImpl extends AbstractResponse
    {
        //水域数据
        public Basin basin;
    }
}
