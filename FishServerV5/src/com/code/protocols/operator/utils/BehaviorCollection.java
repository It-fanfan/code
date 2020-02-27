package com.code.protocols.operator.utils;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.operator.OperatorBase;

import java.util.Vector;

public class BehaviorCollection
{
    public static class Request extends AbstractRequest
    {
        public Vector<OperatorBase.BehaviorInfo> datas;
    }

    public static class Response extends AbstractResponse
    {

    }

}
