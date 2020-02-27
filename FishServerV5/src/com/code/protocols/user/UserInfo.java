package com.code.protocols.user;

import com.code.protocols.AbstractRequest;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.utils.ReceiveReward;

import java.util.List;

import static com.code.protocols.basic.BasePro.Basin;
import static com.code.protocols.core.AnglingBase.AnglingUser;

/**
 * 玩家信息
 */
public class UserInfo
{
    public enum SelectUserType
    {
        basic,//基础（不动产）
        value,//价值
        fish,//鱼数据
        angling,//垂钓
        book,//图鉴
        shop,//商店
        friend,//好友信息
        welfare,//福利领取信息
        guide,//引导
        achievement,//成就
        turntable,//转盘数据
    }

    public static class RequestImpl extends AbstractRequest
    {
        public List<SelectUserType> usertypes;
        public String guide;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        public String userid;
        public String openid;
        public String unionid;
        public String icon;
        public String nickname;
        public String sex;
        public String registtime;
        //垂钓节点
        public AnglingUser anglinguser;
        //玩家鱼数据
        public BigData fishdata;
        //当前水域图鉴数据
        public Basin basin;
        //好友信息
        public BigData friends;
        //当前申请码
        public String applycode;
        //推荐申请列表
        public BigData applylist;
        //福利领取信息
        public ReceiveReward welfare;
        //引导信息
        public String guide;
        //成就数据
        public BigData achievement;
        //转盘数据
        public BasePro.UserTurntable turntable;
    }
}
