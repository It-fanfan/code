package com.code.protocols.login;

import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigNotice;
import com.code.protocols.AbstractResponse;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.social.SocialBase;

import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class Init
{
    /**
     * 初始弹框
     *
     * @return init notice
     */
    public static BigData getInitNotices()
    {
        Vector<ConfigNotice> data = null;
        Vector<ConfigNotice> configs = FishInfoDb.instance().getCacheListByClass(ConfigNotice.class);
        if (configs != null)
        {
            data = configs.stream().filter(ConfigNotice::getState).collect(Collectors.toCollection(Vector::new));
        }
        if (data == null)
            data = new Vector<>();
        return BigData.getBigData(data, ConfigNotice.class);
    }

    public static class RequestImpl
    {
        public String userid;
        public String serverver;
        //当前本地化
        public String localization;
    }

    public static class ResponseImpl extends AbstractResponse
    {
        // 下载地址
        public String configurl;
        // MD5
        public String md5;
        // 版本信息
        public String version;
        //当前是否为审核版本
        public boolean exmain;
        //垂钓初始化
        public AnglingBase.AnglingInit angling;
        //分享配置信息
        public BigData shareconfig;
        //福利奖励活跃任务数值
        public Map<String, OperatorBase.WelfareInfo> welfareconfig;
        //订单数据
        public OrderInit orderinit;
        // 初始弹框
        public BigData initnotices;
        //好友配置信息
        public FriendInit friendinit;
        //数值字典
        public ValueConfig values;
    }

    public static class ValueConfig
    {
        //水域:{basinId,openCost,materials}
        public BigData basin;
        //鱼类型:{ftId,lightExpend,allowLimit}
        public BigData fishtypes;
    }

    /**
     * 好友初始化信息
     */
    public static class FriendInit
    {
        //赠送参数
        public SocialBase.PresentedConfig presented;
        //好友保护cd
        public long protectedcd;
        //好友保护上限数
        public int protectedlimit;
    }

    public static class OrderInit
    {
        public BigData boss;
        public BigData introduce;
        //刷新CD
        public AnglingBase.RefreshConfig refreshconfig;
        //完成CD
        public int finishcd;
    }
}
