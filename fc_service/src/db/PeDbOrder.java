package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "orders")
public class PeDbOrder extends PeDbObject implements Serializable
{
    //订单号
    public String ddId;
    //用户信息
    public String ddUid;
    //商品编号
    public int ddGId;
    //支付类型
    public String ddType = "weChat";
    //支付账号
    public String ddAccount;
    //支付订单
    public String ddOrder;
    //支付异常
    public String ddError;
    //支付金额
    public BigDecimal ddPrice;
    //支付状态
    public int ddState;
    //创建时间
    public Timestamp ddTime;
    //提交时间
    public Timestamp ddTrans;
    //openId
    public String ddOId;
    //appId
    public String ddAppId;

    /**
     * 获取订单信息
     */
    public static PeDbOrder gainObject(String orderId)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbOrder gained = null;

        try
        {
            gained = (PeDbOrder) PeDbOrder.queryOneObjectWithSplitName(sqlResource, PeDbOrder.class, null, "WHERE ddId='" + orderId + "'");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return gained;
    }

    /**
     * 创建一个支付订单
     *
     * @param orderId    订单编号
     * @param ddUid      用户编号
     * @param goodsId    商品编号
     * @param payType    支付类型
     * @param payAccount 支付账号
     * @param price      支付金额
     * @return 订单
     */
    public static PeDbOrder createOrder(String orderId, String ddUid, String openId, String appId, int goodsId, String payType, String payAccount, BigDecimal price)
    {
        PeDbOrder order = new PeDbOrder();
        order.ddId = orderId;
        order.ddUid = ddUid;
        order.ddGId = goodsId;
        order.ddType = payType;
        order.ddAccount = payAccount;
        order.ddPrice = price;
        order.ddOId = openId;
        order.ddAppId = appId;
        order.ddTime = new Timestamp(System.currentTimeMillis());
        return order;
    }

    /**
     * 更新数据库表
     *
     * @throws Exception 异常
     */
    public void update() throws Exception
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        updateObject(sqlResource, "ddOrder#ddState#ddTrans#ddError", "WHERE ddId=" + ddId, false);
    }

    public static void init()
    {
    }
}
