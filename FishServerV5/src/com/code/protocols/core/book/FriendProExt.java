package com.code.protocols.core.book;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;

import java.util.List;

/**
 * 好友守护结果  usl: /friend/guard
 */
public class FriendProExt
{
    public static class RequestImpl extends AbstractRequest
    {
        public String applyid;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        //好友守护信息
        public List<BasePro.FriendPro> friendgardian;
    }
}
