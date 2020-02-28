package com.code.service.ui;

import com.code.cache.UserCache;
import com.code.protocols.operator.achievement.AchievementType;
import com.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.code.protocols.AbstractResponse.FlushNode;

/**
 * 刷新监控数据
 */
public class FlushService
{
    private static Logger LOGGER = LoggerFactory.getLogger(FlushNode.class);
    private UserCache userCache;

    public FlushService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 刷新节点field
     *
     * @return 参数
     */
    private static String getField()
    {
        return "flush-node";
    }

    /**
     * 获取主动通知刷新节点
     */
    public FlushNode getNode()
    {
        FlushNode node = getUserFlushNode();
        if (node != null)
        {
            userCache.hDel(getField());
        }
        return node;
    }

    /**
     * 获取玩家主动通知消息
     */
    private FlushNode getUserFlushNode()
    {
        String json = userCache.getValue(getField());
        if (json != null)
        {
            return XwhTool.parseJSONByFastJSON(json, FlushNode.class);
        }
        return null;
    }

    /**
     * 添加检测
     */
    public void addFlag(FlushType flushType)
    {
        addFlag(flushType, null);
    }

    /**
     * 添加检测
     */
    public void addFlag(FlushType flushType, Map<AchievementType, Long> value)
    {
        FlushNode node = getUserFlushNode();
        if (node == null)
        {
            node = new FlushNode();
        }
        boolean update = false;
        switch (flushType)
        {
            case user:
            {
                if (!node.user)
                {
                    node.user = true;
                    update = true;
                }
            }
            break;
            case achievement:
            {
                update = true;
                if (node.achievement == null)
                    node.achievement = new HashMap<>();
                node.achievement.putAll(value);
            }
            break;
            default:
                break;
        }
        if (update)
        {
            String json = XwhTool.getJSONByFastJSON(node);
            userCache.hSet(getField(), json);
            LOGGER.debug("flush update:" + userCache.getUserId() + ":" + json);
        }
    }

    public enum FlushType
    {
        user,
        achievement,
    }
}
