package com.code.service.order;

import com.code.cache.UserCache;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.protocols.basic.BasePro;
import com.code.protocols.core.AnglingBase;
import com.code.service.reward.RewardService;
import com.code.service.trade.MarketService;
import com.utils.XwhTool;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class VideoService
{

    private UserCache userCache;

    public VideoService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * @return
     */
    private static String getField()
    {
        return "order-video";
    }

    /**
     * 获取玩家视频订单数据
     *
     * @return 视频订单
     */
    private UserVideo getUserOrderVideo()
    {
        String json = userCache.getValue(getField());
        UserVideo video = null;
        if (json != null)
        {
            video = XwhTool.parseJSONByFastJSON(json, UserVideo.class);
        }
        if (video == null)
            video = new UserVideo();
        int dayFlag = XwhTool.getCurrentDateValue();
        if (video.dayFlag != dayFlag)
        {
            video.dayFlag = dayFlag;
            video.times = 0;
            video.rewards = createVideoReward(video.times);
            updateUserOrderVideo(video);
        }
        return video;
    }

    /**
     * 更新video信息
     *
     * @param userVideo 玩家video数据
     */
    private void updateUserOrderVideo(UserVideo userVideo)
    {
        userCache.hSet(getField(), XwhTool.getJSONByFastJSON(userVideo));
    }

    /**
     * 创建视频奖励
     *
     * @param times 次数
     */
    private Vector<BasePro.RewardInfo> createVideoReward(int times)
    {
        VideoConfig videoConfig = XwhTool.parseJSONString(Systemstatusinfo.getText("order_video_config"), VideoConfig.class);
        Vector<BasePro.RewardInfo> rewards = new Vector<>();
        if (videoConfig == null)
            return rewards;
        if (times <= videoConfig.times)
        {
            rewards.add(new BasePro.RewardInfo(MarketService.WealthType.pearl.name(), -1, videoConfig.pearl));
        } else
        {
            int shell = ThreadLocalRandom.current().nextInt(videoConfig.minShell, videoConfig.maxShell);
            rewards.add(new BasePro.RewardInfo(MarketService.WealthType.shell.name(), -1, shell));
        }
        return rewards;
    }

    /**
     * 视频奖励
     */
    public static class VideoConfig
    {
        //阀值
        public int times;
        //奖励珍珠
        public int pearl;
        //奖励贝壳范围
        public int minShell;
        public int maxShell;
    }

    /**
     * 获取订单视频记录
     *
     * @return 视频记录
     */
    public AnglingBase.OrderVideo getOrderVideo()
    {
        AnglingBase.OrderVideo video = new AnglingBase.OrderVideo();
        UserVideo userVideo = getUserOrderVideo();
        video.times = userVideo.times;
        video.rewards = userVideo.rewards;
        return video;
    }

    /**
     * 进行领取视频奖励
     *
     * @param times 当前次数
     * @return 异常信息
     */
    public AnglingBase.ERROR receiveVideo(int times)
    {
        UserVideo userVideo = getUserOrderVideo();
        if (userVideo.times != times)
        {
            return AnglingBase.ERROR.NO_ORDER;
        }
        RewardService service = new RewardService(userCache);
        service.receiveReward(userVideo.rewards, "order-video");
        userVideo.times++;
        userVideo.rewards = createVideoReward(userVideo.times);
        updateUserOrderVideo(userVideo);
        return AnglingBase.ERROR.SUCCESS;

    }

    public static class UserVideo
    {
        //当前时间戳
        public int dayFlag;
        //当前次数
        public int times;
        //当前奖励
        public Vector<BasePro.RewardInfo> rewards;
    }
}
