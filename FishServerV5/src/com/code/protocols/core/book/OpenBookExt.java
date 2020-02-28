package com.code.protocols.core.book;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

import java.util.List;

/**
 * 开图鉴  usl: /book/open
 */
public class OpenBookExt
{
    public static class RequestImpl extends AbstractRequest
    {
        //当前图鉴id
        public int bookid;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //当前点亮图标数据
        public BasePro.Basin basin;
    }
}
