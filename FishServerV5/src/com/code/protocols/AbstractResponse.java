package com.code.protocols;

import com.code.cache.UserCache;
import com.code.protocols.operator.achievement.AchievementType;

import java.util.Map;

/**
 * 发送回调
 *
 * @author Sky
 */
public class AbstractResponse
{
    // 链接狀態,只有正常访问该值恒true
    public boolean status = true;
    // 异常码，定义正常编码200
    public int code = 200;
    // 异常提示
    public String msg;
    //刷新节点参数
    public FlushNode flush;
    //数值数据
    public UserValue value;

    /**
     * 设置玩家值
     *
     * @param userCache 用户信息
     */
    public void setUserValue(UserCache userCache)
    {
        value = new UserValue();
        value.pearl = userCache.getPearl();
        value.shell = userCache.getShell();
    }

    public static class UserValue
    {
        //贝壳
        public String shell;
        //珍珠
        public String pearl;
    }

    public static class FlushNode implements Cloneable
    {
        //用户节点
        public boolean user;
        //成就进度列表
        public Map<AchievementType, Long> achievement;
    }
}
