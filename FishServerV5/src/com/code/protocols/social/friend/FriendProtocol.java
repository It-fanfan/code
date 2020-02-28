package com.code.protocols.social.friend;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;

/**
 * 主协议
 */
public class FriendProtocol
{
    //申请(打招呼)
    //URI:/friend/apply
    public static class ApplyRequest extends AbstractRequest
    {
        //好友编号
        public String applyid;
    }

    public static class ApplyResponse extends AbstractResponse
    {
        //更新推荐列表:FriendInfo
        public BigData recommend;
    }

    //添加
    //URI:/friend/add
    public static class AddRequest extends AbstractRequest
    {
        //申请id
        public String applyid;
        //确认是否添加
        public boolean result;
    }

    public static class AddResponse extends AbstractResponse
    {
        //当前好友列表
        public BigData firendlist;
    }

    //查询
    //URI:/friend/select
    public static class SelectRequest extends AbstractRequest
    {
        //申请码，未空，则为推荐列表
        public String applycode;
    }

    public static class SelectResponse extends AbstractResponse
    {
        //查询好友，无申请码则为空
        public FriendInfo friend;
        //推荐好友
        public BigData recommend;
    }

    //赠送
    public static class PresentedRequest extends AbstractRequest
    {
        //赠送好友
        public String applyid;
    }

    public static class PresentedResponse extends AbstractResponse
    {
        public FriendInfo friendinfo;
    }
}
