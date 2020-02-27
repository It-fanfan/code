package com.code.protocols.operator.message;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

import java.util.Vector;

/**
 * 消息节点
 */
public class MessageProtocol
{
    //URI:/message/select
    public static class SelectRequestImpl extends AbstractRequest
    {
        //玩家未领取且客户端存在ID列表
        public Vector<String> histroyunclaimed;
    }

    public static class SelectResponseImpl extends AbstractResponse
    {
        public BigData messages;
    }

    //领取操作邮件
    public static class ReceiveRequestImpl extends AbstractRequest
    {
        public String id;
    }

    public static class ReceiveResponseImpl extends AbstractResponse
    {

    }
}
