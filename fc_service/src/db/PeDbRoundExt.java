/**
 *
 */
package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.glassfish.gmbal.ManagedData;
import tool.Log4j;

import javax.persistence.Entity;
import java.util.Vector;

/**
 * @author feng
 */
@Entity(name = "round_ext")
@ManagedData(name = "persie_deamon")
public class PeDbRoundExt extends PeDbObject
{
    //赛制详情
    public String ddCode;
    //群标签
    public boolean ddGroup;
    //赛制名称
    public String ddName;
    //赛制优先级
    public int ddPriority;
    //赛制状态
    public boolean ddState;
    //赛制循环时间
    public long ddTime;
    //赛制奖励类型
    public String ddReward;
    //赛场描述
    public String ddDesc;

    private JSONObject reward = null;

    private JSONObject message = null;


    //
    // 全部比赛日的列表
    //
    private static Vector<PeDbObject> allRounds = new Vector<>();

    public static Vector<PeDbObject> getAllRounds()
    {
        return allRounds;
    }

    /**
     * 同步比赛列表
     */
    private static void syncRounds()
    {
        allRounds = getRounds();
    }

    /**
     * 获取比赛信息
     */
    public static PeDbRoundExt getRoundFast(String code)
    {
        for (int i = 0; i < allRounds.size(); i++)
        {
            PeDbRoundExt round = (PeDbRoundExt) allRounds.elementAt(i);
            if (round.ddCode.equals(code))
            {
                return round;
            }
        }
        if (allRounds.size() > 0)
        {
            return (PeDbRoundExt) allRounds.firstElement();
        }

        return null;
    }

    /**
     * 获取奖励列表
     *
     * @return 奖励内容
     */
    public JSONObject getRoundReward()
    {
        if (reward == null)
        {
            reward = new JSONObject();
            JSONArray jsonArray = JSONObject.parseArray(ddReward);
            for (int i = 0; i < jsonArray.size(); i++)
            {
                //"1#1#rmb#200"
                String[] data = jsonArray.getString(i).split("#");
                int start = Integer.valueOf(data[0]);
                int end = Integer.valueOf(data[1]);
                String type = data[2];
                int value = Integer.valueOf(data[3]);
                if ("rmb".equals(type))
                    value *= 100;
                for (int j = start; j <= end; j++)
                {
                    JSONObject info = new JSONObject();
                    info.put("type", type);
                    info.put("value", value);
                    info.put("index", j);
                    reward.put(String.valueOf(j), info);
                }
            }
        }
        return reward;
    }

    /**
     * 获取下发参数
     *
     * @return 赛制信息
     */
    public JSONObject getMessage()
    {
        if (message != null)
            return message;
        JSONObject data = new JSONObject();
        data.put("round", ddCode);
        data.put("times", ddTime);
        JSONArray reward = new JSONArray();
        JSONArray jsonArray = JSONObject.parseArray(ddReward);
        for (int i = 0; i < jsonArray.size(); i++)
        {
            String[] info = jsonArray.getString(i).split("#");
            int start = Integer.valueOf(info[0]);
            int end = Integer.valueOf(info[1]);
            String type = info[2];
                   int value = Integer.valueOf(info[3]);
            if ("rmb".equals(type))
                value *= 100;
            JSONObject context = new JSONObject();
            context.put("s", start);
            context.put("e", end);
            context.put("t", type);
            context.put("v", value);
            reward.add(context);
        }
        data.put("isGroup", ddGroup);
        data.put("reward", reward);
        return message = data;
    }

    /**
     * 获取比赛列表
     */
    private static Vector<PeDbObject> getRounds()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbRoundExt.queryObject(sqlResource, PeDbRoundExt.class, "");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return objects;
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbRoundExt()
    {
        super();
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncRounds();
    }


}
