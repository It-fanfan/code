package com.code.service.reward;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.record.RecordReward;
import com.code.protocols.basic.BasePro;
import com.code.protocols.core.AnglingBase;
import com.code.service.angling.AnglingInitService;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Vector;

/**
 * 奖励服务
 */
public class RewardService
{
    //日志打印
    private static final Logger LOG = LoggerFactory.getLogger(RewardService.class);
    private UserCache userCache;

    public RewardService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 获取奖励数据
     */
    public static Vector<BasePro.RewardInfo> getRewardData(String json)
    {
        if (json == null || json.isEmpty())
            return new Vector<>();
        return XwhTool.parseJSONByFastJSON(json, new TypeToken<Vector<BasePro.RewardInfo>>()
        {
        }.getType());
    }

    public static void main(String[] args)
    {
        Vector<BasePro.RewardInfo> data = new Vector<>();
        data.add(new BasePro.RewardInfo("shell", -1, 10000));
        data.add(new BasePro.RewardInfo("hook", 1, 2));
        System.out.println(XwhTool.getJSONByFastJSON(data));
    }

    /**
     * 領取獎勵信息
     *
     * @param rewardInfos 獎勵數據
     */
    public void receiveReward(Vector<BasePro.RewardInfo> rewardInfos, String source)
    {
        try
        {
            AnglingInitService propService = new AnglingInitService(userCache);
            Vector<BasePro.RewardInfo> run = new Vector<>();
            for (BasePro.RewardInfo rewardInfo : rewardInfos)
            {
                switch (rewardInfo.type)
                {
                    case shell:
                    {
                        userCache.incrShell(rewardInfo.total);
                        run.add(rewardInfo);
                    }
                    break;
                    case pearl:
                    {
                        userCache.incrPearl(rewardInfo.total);
                        run.add(rewardInfo);
                    }
                    break;
                    case props:
                    {
                        // 道具
                        propService.addProps(rewardInfo.id, rewardInfo.total);
                        run.add(rewardInfo);
                    }
                    break;
                    case material:
                    {
                        // 材料
                        AnglingBase.MaterialResult material = new AnglingBase.MaterialResult();
                        material.id = rewardInfo.id;
                        material.count = rewardInfo.total;
                        userCache.updateMaterial(material);
                        run.add(rewardInfo);
                    }
                    break;
                    case none:
                        break;
                    default:
                        break;
                }
            }
            if (!run.isEmpty())
            {
                RecordReward record = new RecordReward();
                record.setUserId(userCache.userId());
                record.setSource(source);
                record.setInsertTime(new Timestamp(System.currentTimeMillis()));
                String json = XwhTool.getJSONByFastJSON(run);
                record.setRewardData(json);
                record.setRewardTotal(run.size());
                FishInfoDb.instance().saveOrUpdate(record, true);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 領取獎勵數據
     *
     * @param rewardInfo 奖励信息
     */
    public void receiveReward(BasePro.RewardInfo rewardInfo, String source)
    {
        Vector<BasePro.RewardInfo> data = new Vector<>();
        data.add(rewardInfo);
        receiveReward(data, source);
    }
}
