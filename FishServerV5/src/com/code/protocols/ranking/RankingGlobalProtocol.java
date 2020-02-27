package com.code.protocols.ranking;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.utils.Member;

/**
 * 全球排行
 */
public class RankingGlobalProtocol
{
    public static class RequestImpl extends AbstractRequest
    {

    }

    public static class ResponseImpl extends AbstractResponse
    {
        // 全球数据
        public BigData globals;
        // 我的排名
        public RankingUser rankglobal;
    }

    /**
     * 排名用户信息
     */
    public static class RankingUser extends Member
    {
        // 排名数:默认排行:9999
        public long rank = 9999;
        // 图鉴数
        public int booknum;
    }
}
