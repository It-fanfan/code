package com.code.protocols.core.angling;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.core.AnglingBase.FishResult;
import com.code.protocols.core.AnglingBase.FishingRod;
import com.code.protocols.core.AnglingBase.GameStatus;
import com.code.protocols.core.AnglingBase.SendReward;

import java.util.Map;
import java.util.Vector;

/**
 * 垂钓结果协议 url:/angling/result
 */
public class AnglingResultExt
{
    public static class RequestImpl extends AbstractRequest
    {
        //海域等级
        public int basin;
        // 垂钓结果 鱼种类=鱼数量
        public Vector<FishResult> fishes;
        //上报耐力值
        public int endurance;
        //上报当前perfect的状态
        public GameStatus gamestatus;
        //使用道具
        public int propid;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        // 是否允许垂钓
        public boolean allow;
        //垂钓结果
        public BigData bag;
        //当次实际垂钓鱼数
        public int fishsize;
        //初始化下次奖励
        public SendReward reward;
        //鱼竿情况
        public FishingRod rod;
        //获取玩家上次垂钓的状态
        public Map<String, Integer> operation;
        //道具数量更新,仅使用后再处理
        public Map<String, Integer> props;
        //图鉴数据
        public BasePro.Basin basin;
        //漂流瓶数据
        public AnglingBase.DrifterData drifter;
    }
}
