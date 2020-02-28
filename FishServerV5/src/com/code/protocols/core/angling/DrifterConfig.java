package com.code.protocols.core.angling;

import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.protocols.basic.BasePro;
import com.code.protocols.goods.CostType;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.operator.OperatorBase;
import com.utils.XwhTool;

import java.util.Vector;

/**
 * 漂流瓶配置
 */
public class DrifterConfig
{
    //触发条件列表
    public Vector<Trigger> triggers;
    //奖励数值
    public Vector<DrifterReward> reward;

    /**
     * 获取漂流瓶配置信息
     *
     * @return 配置
     */
    public static DrifterConfig getInstance(LoginRequest.Platform platform)
    {
        String json = Systemstatusinfo.getText("drifter-config");
        if (platform == LoginRequest.Platform.huawei)
        {
            json = Systemstatusinfo.getText("drifter-config-huawei");
        }
        if (json != null)
        {
            return XwhTool.parseJSONByFastJSON(json, DrifterConfig.class);
        }
        return null;
    }

    public static void main(String[] arg)
    {
        String json = "{\"reward\":[{\"id\":1,\"rate\":30,\"total\":1,\"type\":\"material\"},{\"id\":2,\"rate\":30,\"total\":1,\"type\":\"material\"},{\"id\":3,\"rate\":30,\"total\":1,\"type\":\"material\"},{\"id\":-1,\"rate\":10,\"total\":5,\"type\":\"pearl\"}],\"triggers\":[{\"activitynum\":6,\"activitytype\":\"angling\",\"costnum\":1,\"costtype\":\"video\",\"showsecond\":60},{\"activitynum\":26,\"activitytype\":\"angling\",\"costnum\":500,\"costtype\":\"shell\",\"showsecond\":60},{\"activitynum\":61,\"activitytype\":\"angling\",\"costnum\":1000,\"costtype\":\"shell\",\"showsecond\":60},{\"activitynum\":121,\"activitytype\":\"angling\",\"costnum\":2000,\"costtype\":\"shell\",\"showsecond\":60}]}";
        DrifterConfig config = XwhTool.parseJSONByFastJSON(json, DrifterConfig.class);
        int[] counts = new int[]{6, 26, 61, 121};
        for (int i = 0; i < config.triggers.size(); i++)
        {
            Trigger trigger = config.triggers.elementAt(i);
            trigger.activitytype = OperatorBase.ActivityType.angling;
            trigger.activitynum = counts[i];
        }
        System.out.println(XwhTool.getJSONByFastJSON(config));
    }

    /**
     * 触发条件
     */
    public static class Trigger
    {
        //消耗类型
        public CostType costtype;
        //消耗数量
        public int costnum;
        //展示时间:秒数
        public int showsecond;
        //触发条件-绑定日常任务
        public OperatorBase.ActivityType activitytype;
        //触发条件-绑定任务次数
        public int activitynum;

        public Trigger(CostType costtype, int costNum, int showSecond, OperatorBase.ActivityType activityType, int activityNum)
        {
            this.costtype = costtype;
            this.costnum = costNum;
            this.showsecond = showSecond;
            this.activitytype = activityType;
            this.activitynum = activityNum;
        }

        public Trigger()
        {
        }
    }

    /**
     * 触发奖励信息
     */
    public static class DrifterReward extends BasePro.RewardInfo
    {
        public int rate;

        public DrifterReward()
        {
            super();
        }

        public DrifterReward(String type, int id, int total, int rate)
        {
            super(type, id, total);
            this.rate = rate;
        }
    }
}
