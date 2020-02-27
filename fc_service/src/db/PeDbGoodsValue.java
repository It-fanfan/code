package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;

@Entity(name = "goods_value_ext")
public class PeDbGoodsValue extends PeDbObject implements Serializable
{
    /**
     * 商品编号
     */
    public int ddId;
    //状态
    public boolean ddState;
    //商品名称
    public String ddName;
    //商品描述
    public String ddDesc;
    //购买类型
    public String ddCostType;
    //购买单价
    public BigDecimal ddPrice;
    //商品类型
    public String ddGoodsType;
    //商品数值
    public int ddValue;
    //首充翻倍
    public boolean ddFrist;
    //
    // 全部商品的列表
    //
    private static Vector<PeDbGoodsValue> allGoods = new Vector<>();

    /**
     * 获取游戏信息
     */
    public static PeDbGoodsValue getGoodsFast(int code)
    {
        for (int i = 0; i < allGoods.size(); i++)
        {
            PeDbGoodsValue game = allGoods.elementAt(i);
            if (game.ddId == code)
            {
                return game;
            }
        }

        return null;
    }

    /**
     * 获取商品信息
     */
    public static Vector<PeDbGoodsValue> getGamesFast(String appId)
    {
        Vector<PeDbGoodsValue> cache = new Vector<>();
        allGoods.forEach(data ->
        {
            if (!data.ddState)
                return;
            cache.add(data);
        });
        if (appId != null)
        {
            PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
            switch (appConfig.ddProgram)
            {
                //小游戏，无金币商品，无提现商品
                case 0:
                {
                    cache.removeIf(c -> c.ddGoodsType.equals("coin") || c.ddGoodsType.equals("recharge"));
                }
                break;
            }
        }
        return cache;
    }

    /**
     * 获取商品列表
     */
    public static PeDbGoodsValue getGoods(int code)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbGoodsValue goods = null;

        try
        {
            goods = (PeDbGoodsValue) PeDbGoodsValue.queryOneObject(sqlResource, PeDbGoodsValue.class, "WHERE goodsId=" + code);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return goods;
    }

    /**
     * 获取游戏列表
     */
    private static Vector<PeDbGoodsValue> getGoods()
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbGoodsValue> data = new Vector<>();

        try
        {
            Vector<PeDbObject> objects = PeDbGoodsValue.queryObject(sqlResource, PeDbGoodsValue.class, "");
            if (objects != null)
                objects.forEach(element ->
                {
                    PeDbGoodsValue value = (PeDbGoodsValue) element;
                    data.add(value);
                });
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return data;
    }

    /**
     * 同步游戏列表
     */
    private static void syncGoods()
    {
        allGoods = getGoods();
    }

    /**
     * 初始化数据对象
     */
    public static void init()
    {
        syncGoods();
    }

    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer("PeDbGoodsValue{");
        sb.append("ddId=").append(ddId);
        sb.append(", ddState=").append(ddState);
        sb.append(", ddName='").append(ddName).append('\'');
        sb.append(", ddDesc='").append(ddDesc).append('\'');
        sb.append(", ddCostType='").append(ddCostType).append('\'');
        sb.append(", ddPrice=").append(ddPrice);
        sb.append(", ddGoodsType='").append(ddGoodsType).append('\'');
        sb.append(", ddValue=").append(ddValue);
        sb.append('}');
        return sb.toString();
    }
}
