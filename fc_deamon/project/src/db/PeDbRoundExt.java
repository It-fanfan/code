/**
 *
 */
package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tool.Log4j;

import javax.persistence.Entity;
import java.util.Vector;

/**
 * @author feng
 */
@Entity(name = "round_ext")
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

    public JSONObject reward = null;


    //
    // 全部比赛日的列表
    //
    public static Vector<PeDbObject> allRounds = new Vector<>();

    public static Vector<PeDbObject> getAllRounds()
    {
        return allRounds;
    }

    /**
     * 同步比赛列表
     */
    public static void syncRounds()
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
     * 获取比赛列表
     */
    public static Vector<PeDbObject> getRounds()
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
