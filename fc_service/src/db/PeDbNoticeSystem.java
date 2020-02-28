package db;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import tool.CmTool;
import tool.Log4j;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Entity(name = "notice_system")
@ManagedData(name = "persie_deamon")
public class PeDbNoticeSystem extends PeDbObject
{
    public int ddId;
    //是否开启
    public boolean ddState;
    //公告优先级
    public int ddPriority;
    //公告频率
    public int ddFrequency;
    //公告时间间隔
    public int ddInterval;
    //公告
    //公告类型
    public String ddType;
    //公告内容
    public String ddContext;
    //时间范围
    public Timestamp ddStart;
    //截止时间
    public Timestamp ddEnd;

    public enum NoticeType
    {
        safe,//维护公告
        match,//赛事公告
        reward,//奖励公告
        game_explain,//小游戏奖励‘？’说明
        mini_explain,//小程序奖励‘？’说明
        routine,//常规
    }

    public JSONObject getMessage()
    {
        JSONObject message = new JSONObject();
        message.put("id", ddId);
        message.put("priority", ddPriority);
        message.put("frequency", ddFrequency);
        message.put("interval", ddInterval);
        message.put("type", NoticeType.valueOf(ddType));
        message.put("context", ddContext);
        return message;
    }

    //
    // 全部公告内容
    //
    public static Map<Integer, PeDbNoticeSystem> allNotice = new HashMap<>();

    public static PeDbNoticeSystem getNotice(int id)
    {
        return allNotice.get(id);
    }

    /**
     * 获取公告信息
     *
     * @return 公告通知
     */
    public static Vector<PeDbNoticeSystem> getNoticeByType(NoticeType type)
    {
        Vector<PeDbNoticeSystem> systems = new Vector<>();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        allNotice.forEach((k, v) ->
        {
            if (v.ddEnd.before(now) || v.ddStart.after(now))
                return;
            if (NoticeType.valueOf(v.ddType) == type)
                systems.add(v);
        });
        return systems;
    }

    /**
     * 获取游戏列表
     */
    private static Map<Integer, PeDbNoticeSystem> getAllNotice()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Map<Integer, PeDbNoticeSystem> notice = new HashMap<>();

        try
        {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Vector<PeDbObject> objects = PeDbNoticeSystem.queryObject(sqlResource, PeDbNoticeSystem.class, "");
            objects.forEach(element ->
            {
                PeDbNoticeSystem data = (PeDbNoticeSystem) element;
                if (data.ddEnd.before(now) || data.ddStart.after(now))
                    return;
                if (data.ddState)
                    notice.put(data.ddId, data);
            });
            LOG.debug("公告数据:" + CmTool.getJSONByFastJSON(objects));
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return notice;
    }

    public static void sync()
    {
        allNotice = getAllNotice();

    }

    public static void init()
    {
        sync();
    }
}
