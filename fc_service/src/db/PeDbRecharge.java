package db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tool.Log4j;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

@Entity(name = "recharge")
public class PeDbRecharge extends PeDbObject
{
    //订单ID
    public String ddId;
    //订单人民币
    public BigDecimal ddRmb;
    //订单appId
    public String ddAppId;
    //用户编号
    public String ddUid;
    //提现描述
    public String ddTip;
    //提现状态
    public int ddStatus;
    //提现appId
    public String ddRechargeAppId;
    //提现openId
    public String ddRechargeOpenId;
    //提现时间
    public Timestamp ddTimes;
    //完成时间
    public Timestamp ddTrans;

    /**
     * 获取游戏列表
     */
    public static Vector<PeDbObject> getDbRecharge(String ddUid)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbRecharge.queryObject(sqlResource, PeDbRecharge.class, "WHERE ddUid='" + ddUid + "'");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    /**
     * 获取游戏列表
     */
    public static Vector<PeDbObject> getDbRechargeList(String ddOpenId)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try
        {
            objects = PeDbRecharge.queryObject(sqlResource, PeDbRecharge.class, "WHERE ddRechargeOpenId='" + ddOpenId + "'");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    /**
     * 獲取消息節點
     *
     * @param list 提现记录
     * @return 下发参数节点
     */
    public static JSONArray getMessage(Vector<PeDbObject> list)
    {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        JSONArray jsonArray = new JSONArray();
        list.forEach(temp ->
        {
            PeDbRecharge data = (PeDbRecharge) temp;
            JSONObject element = getJsonObject(format, data);
            jsonArray.add(element);
        });
        return jsonArray;
    }

    /**
     * 獲取一筆提现记录
     *
     * @param format 时间格式
     * @param data   提现记录
     * @return json
     */
    public static JSONObject getJsonObject(DateFormat format, PeDbRecharge data)
    {
        JSONObject element = new JSONObject();
        Timestamp time = data.ddTimes;
        String msg = "提现 " + data.ddRmb + " 元";
        String state = "发放中";
        switch (data.ddStatus)
        {
            case 200:
            {
                time = data.ddTrans;
                state = "已到账";
            }
            break;
            case 1:
            {
                state = "申请中";
            }
            break;
            case -1:
            {
                time = data.ddTrans;
                state = "审核失败";
            }
            break;
        }
        element.put("rmb", data.ddRmb);
        element.put("appId", data.ddAppId);
        element.put("orderId", data.ddId);
        element.put("applyTime", format.format(data.ddTimes));
        element.put("times", format.format(time));
        element.put("msg", msg);
        element.put("status", data.ddStatus);
        element.put("state", state);
        return element;
    }

    /**
     * 获取提现记录
     */
    public static PeDbRecharge searchRecharge(String ddId)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbRecharge element = null;

        try
        {
            element = (PeDbRecharge) PeDbGoodsValue.queryOneObject(sqlResource, PeDbRecharge.class, "where ddId like'%" + ddId + "'");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return element;
    }

    public static void init()
    {
    }

    /**
     * 更新参数
     */
    public void update(String filter)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            this.updateObject(sqlResource, filter, "where ddId='" + ddId + "'", false);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }
}
