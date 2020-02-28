package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import service.UserService;
import tool.CmTool;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Vector;

@Entity(name = "notice_reward")
@ManagedData(name = "persie_deamon")
public class PeDbNoticeReward extends PeDbObject
{
    //补偿奖励编号
    public int ddId;
    //补偿用户
    public String ddUid;
    //是否已经补偿
    public boolean ddState;
    //公告编号
    public long ddNotice;
    //公告参数
    public String ddValue;
    //时间范围
    public Timestamp ddStart;
    //截止时间
    public Timestamp ddEnd;

    /**
     * 获取游戏列表
     */
    private static Vector<PeDbNoticeReward> initNotice()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbNoticeReward> notice = new Vector<>();

        try
        {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Vector<PeDbObject> objects = PeDbNoticeReward.queryObject(sqlResource, PeDbNoticeReward.class, "");
            objects.forEach(element ->
            {
                PeDbNoticeReward data = (PeDbNoticeReward) element;
                if (data.ddEnd.before(now) || data.ddStart.after(now))
                    return;
                if (data.ddState)
                    notice.add(data);
            });
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return notice;
    }

    /**
     * 同步服务器
     */
    private static void sync()
    {
        Vector<PeDbNoticeReward> notices = initNotice();
        String field = getField();
        //初始加载
        for (PeDbNoticeReward reward : notices)
        {
            String uid = reward.ddUid;
            String history = UserService.getStr(uid, field);
            JSONObject data = null;
            if (history != null)
            {
                data = JSONObject.parseObject(history);
            }
            if (data == null)
                data = new JSONObject();
            if (data.containsKey(String.valueOf(reward.ddId)))
                continue;
            JSONObject notice = new JSONObject();
            notice.put("id", reward.ddId);
            notice.put("noticeId", reward.ddNotice);
            notice.put("start", reward.ddStart);
            notice.put("end", reward.ddEnd);
            notice.put("state", reward.ddState);
            notice.put("value", JSONObject.parseArray(reward.ddValue));
            data.put(String.valueOf(reward.ddId), notice);
            UserService.setCache(uid, field, data.toJSONString());
        }
    }

    /**
     * 获取补偿公告
     *
     * @param uid 用户编号
     */
    public static JSONArray getRewardNotice(String uid, String appId)
    {
        String field = getField();
        String history = UserService.getStr(uid, field);
        if (history == null)
            return null;
        JSONObject data = JSONObject.parseObject(history);
        if (data == null)
            return null;
        //可操作
        JSONArray array = new JSONArray();
        for (Map.Entry<String, Object> entry : data.entrySet())
        {
            String id = entry.getKey();
            JSONObject notice = (JSONObject) entry.getValue();
            //已经领取
            if (!notice.getBoolean("state"))
                continue;
            //操作领取
            notice.put("state", false);
            data.put(id, notice);
            array.add(notice);
        }
        if (array.size() <= 0)
            return null;
        JSONArray result = new JSONArray();
        //提前存储到缓存中，进行异步更新
        UserService.setCache(uid, field, data.toJSONString());
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject notice = array.getJSONObject(i);
            PeDbNoticeSystem system = PeDbNoticeSystem.getNotice(notice.getInteger("noticeId"));
            //获取模板
            if (system != null)
            {
                JSONArray values = notice.getJSONArray("value");
                Object[] value = new Object[array.size()];
                for (int j = 0; j < array.size(); j++)
                {
                    JSONObject reward = values.getJSONObject(j);
                    String type = reward.getString("type");
                    int addVal = reward.getInteger("value");
                    JSONObject extra = new JSONObject();
                    extra.put("type", "notice");
                    //复活次数
                    extra.put("appId", appId);
                    extra.put("extra", String.valueOf(system.ddId));
                    UserService.addValue(uid, type, addVal, extra);
                    switch (type)
                    {
                        case "rmb":
                        {
                            value[j] = CmTool.div(addVal, 100, 0) + " 元";
                        }
                        break;
                        case "coin":
                        {
                            value[j] = addVal + " 金币";
                        }
                        break;
                    }
                }
                String context = MessageFormat.format(system.ddContext, value);
                result.add(context);
                long id = notice.getLong("id");
                CmDbSqlResource.instance().addQueue("update persie_deamon.notice_reward set ddState=0 where ddId=" + id);
            }
        }
        return result;
    }

    public static String getField()
    {
        return "make-up-record";
    }

    public static void init()
    {
        sync();
    }
}
