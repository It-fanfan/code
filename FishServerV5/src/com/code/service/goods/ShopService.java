package com.code.service.goods;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.goods.GoodsDaily;
import com.code.dao.entity.goods.GoodsValue;
import com.code.dao.entity.record.RecordBuy;
import com.code.dao.entity.record.RecordVirtual;
import com.code.protocols.LoginCode;
import com.code.protocols.basic.BasePro;
import com.code.protocols.goods.CostType;
import com.code.protocols.goods.Shop;
import com.code.protocols.midas.GetBalance;
import com.code.protocols.operator.OperatorBase;
import com.code.service.mq.RabbitMQ;
import com.code.service.reward.RewardService;
import com.code.service.trade.MarketService;
import com.utils.XWHMathTool;
import com.utils.XwhTool;
import com.utils.http.XwhHttp;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import static com.code.protocols.operator.OperatorBase.ERROR.*;

/**
 * 商城邏輯
 */
public class ShopService
{
    //日志
    private static final Logger LOG = LoggerFactory.getLogger(ShopService.class);
    private static long orderIndex = 0;
    private UserCache userCache;

    public ShopService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 商品列表
     *
     * @param goodsId 商品编号
     * @return 商品属性
     */
    public static GoodsValue getGoodsValue(int goodsId)
    {
        return (GoodsValue) FishInfoDb.instance().getCacheKey(GoodsValue.class, new String[]{"id", String.valueOf(goodsId)});
    }

    /**
     * 商品-道具 缓存
     *
     * @param goodsId 商品编号
     * @return 商品属性
     */
    private static GoodsDaily getGoodsDailyCache(int goodsId)
    {
        return (GoodsDaily) FishInfoDb.instance().getCacheKey(GoodsDaily.class, new String[]{"id", String.valueOf(goodsId)});
    }

    /**
     * 計費參數
     *
     * @return redis
     */
    private static String getBuyRedis()
    {
        return "payCount";
    }

    /**
     * 計費參數
     *
     * @return redis
     */
    private static String getBuyCostRedis()
    {
        return "payCost";
    }

    /**
     * 购买商品
     *
     * @param goodsType 商品类型
     * @param goodsId   商品编号
     * @param total     购买份数
     * @return 购买是否成功
     */
    public boolean buyGoods(BasePro.RewardType goodsType, int goodsId, int total)
    {

        GoodsDaily daily = getGoodsDailyCache(goodsId);
        if (daily == null)
            return false;
        CostType costType = CostType.valueOf(daily.getCostType());
        long price = daily.getPrice() * total;
        //支付不够扣
        if (costGoods(price, costType))
        {
            return false;
        }
        //商品添加统一使用奖励接口
        BasePro.RewardInfo rewardInfo = new BasePro.RewardInfo(daily.getGoodsType(), daily.getProductId(), daily.getProductNum() * total);
        // 获取商品
        RewardService rewardService = new RewardService(userCache);
        rewardService.receiveReward(rewardInfo, "shop");
        return addRecordBuy(goodsType, goodsId, total, costType, price);
    }

    /**
     * 支付是否失败
     *
     * @param price 上传价格
     * @return 失败
     */
    public boolean costGoods(long price, CostType costType)
    {
        switch (costType)
        {
            case share://分享
            case video://视频
            case none://空
                return false;
            case pearl: // 消耗珍珠
            {
                long pearl = userCache.pearl();
                if (pearl < price)
                    return true;
                return userCache.incrPearl(-price);
            }
            case shell:// 消耗贝壳
            {
                long shell = userCache.shell();
                if (shell < price)
                    return true;
                return !userCache.incrShell(-price);
            }
            //人民币和虚拟币不支持
            case rmb:
            case virtual:
            default:
                return true;
        }
    }

    /**
     * 添加购买记录
     *
     * @param goodsType 商品类型
     * @param goodsId   商品编号
     * @param total     份数
     * @param costType  消耗类型
     * @param cost      消耗金额
     */
    private boolean addRecordBuy(BasePro.RewardType goodsType, int goodsId, int total, CostType costType, long cost)
    {
        try
        {
            RecordBuy recordBuy = new RecordBuy();
            recordBuy.setCost(cost);
            recordBuy.setCostType(costType.name());
            recordBuy.setGoodsId(goodsId);
            recordBuy.setGoodsType(goodsType.name());
            recordBuy.setInsertTime(new Timestamp(System.currentTimeMillis()));
            recordBuy.setTotal(total);
            recordBuy.setUserId(userCache.userId());
            FishInfoDb.instance().saveOrUpdate(recordBuy, true);
            return true;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }

    /**
     * 设置价值商品列表
     */
    public Vector<Shop.GoodsBasic> getValueGoods()
    {
        Vector<Shop.GoodsBasic> values = new Vector<>();
        Vector<GoodsValue> list = FishInfoDb.instance().getCacheListByClass(GoodsValue.class);
        if (list != null)
        {
            list.forEach(goods ->
            {
                if (goods == null)
                    return;
                BasePro.RewardType goodsType = BasePro.RewardType.valueOf(goods.getWealthType());
                Shop.GoodsBasic value = setGoodsInfo(goods.getId(), goods.getState(), goods.getIcon(), goods.getGoodsName(), goods.getStartTime().getTime(), goods.getEndTime().getTime(), CostType.rmb, goodsType, goods.getLabelUrl(), goods.getRmb(), goods.getDiscount());
                if (value != null)
                {
                    values.add(value);
                }
            });
        }
        return values;
    }

    /**
     * 设置商品属性
     *
     * @param id        商品编号
     * @param state     商品状态
     * @param icon      商品ICON
     * @param name      商品名称
     * @param start     开始时间
     * @param end       截止时间
     * @param type      消费类型
     * @param goodsType 商品类型
     * @param label     标签
     * @param price     商品价格
     * @param discount  折扣
     */
    private Shop.GoodsBasic setGoodsInfo(int id, boolean state, String icon, String name, long start, long end, CostType type, BasePro.RewardType goodsType, String label, int price, int discount)
    {
        Shop.GoodsBasic basic = new Shop.GoodsBasic();
        basic.goodsid = id;
        basic.icon = icon;
        basic.goodsname = name;
        basic.start = start;
        basic.end = end;
        basic.costtype = type;
        basic.labalurl = label;
        basic.price = price;
        basic.discount = discount;
        basic.goodstype = goodsType;
        long now = System.currentTimeMillis();
        if (!state || basic.start > now || basic.end <= now)
            return null;
        if ("first".equals(label))
        {
            String field = getBuyRedis();
            String _data = userCache.getValue(field);
            if (_data == null || "0".equals(_data))
                return basic;
            return null;
        }
        return basic;
    }

    /**
     * 道具商品
     *
     * @return 商品列表
     */
    public Vector<Shop.GoodsBasic> getDailyGoods()
    {
        Vector<GoodsDaily> list = FishInfoDb.instance().getCacheListByClass(GoodsDaily.class);
        Vector<Shop.GoodsBasic> data = new Vector<>();
        if (list != null)
        {
            list.forEach(goods ->
            {
                if (goods == null)
                    return;
                Shop.GoodsBasic value = setGoodsInfo(goods.getId(), goods.getState(), goods.getIcon(), goods.getGoodsName(), goods.getStartTime().getTime(), goods.getEndTime().getTime(), CostType.valueOf(goods.getCostType()), BasePro.RewardType.valueOf(goods.getGoodsType()), goods.getLabelUrl(), goods.getPrice(), goods.getDiscount());
                if (value != null)
                {
                    data.add(value);
                }
            });
        }
        return data;
    }

    /**
     * 获取虚拟订单信息
     *
     * @param wealthType 价格类型
     * @param goodsId    商品编号
     * @param virtual    虚拟币
     * @param price      获得数值
     * @param balance    当前玩家信息
     * @return 订单信息
     */
    public String getVirtualOrder(MarketService.WealthType wealthType, int goodsId, int price, int virtual, GetBalance.ResponseImpl balance)
    {
        RecordVirtual record = new RecordVirtual();
        record.setHistoryBalance(balance.balance);
        record.setUserId(userCache.userId());
        record.setVirtualValue(virtual);
        String id = XwhTool.getCurrentDateValue() + "-" + XwhTool.getCurrentHourValue() + "-" + record.getUserId() + "-" + ThreadLocalRandom.current().nextInt(1000) + (++orderIndex);
        if (orderIndex > 10000)
        {
            orderIndex = 0;
        }
        record.setGoodsId(goodsId);
        record.setId(id);
        record.setPrice(price);
        record.setWealthType(wealthType.name());
        try
        {
            String field = "virtual_" + id;
            userCache.hSet(field, XwhTool.getJSONByFastJSON(record));
            //设置订单补救措施,顶替单
            userCache.hSet("virtual-order", field);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return id;
    }

    /**
     * 查找补单信息
     */
    public void init(LoginCode loginCode)
    {
        String field = "virtual-order";
        if (!userCache.hexist(field))
        {
            return;
        }
        //查单
        String orderId = userCache.getValue(field);
        String json = userCache.getValue(orderId);
        //删单
        userCache.hDel(field, orderId);
        if (json == null)
        {
            return;
        }
        OperatorBase.ERROR error = SUCCESS;
        try
        {
            error = saveSuccessVirtualOrder(json, loginCode);
            if (error == UNDEFINE_ORDER)
            {
                //重新回滚
                Map<String, String> hash = new HashMap<>();
                hash.put(field, orderId);
                hash.put(orderId, json);
                userCache.save(hash);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            LOG.debug("进行补单：补单号:" + orderId + ",补单结果:" + error + ",记录:" + json);
        }
    }

    /**
     * 购买虚拟币
     *
     * @param orderId 订单信息
     * @param success 购买是否成功
     * @return 狀態嗎
     */
    public OperatorBase.ERROR buyVirtual(String orderId, boolean success, LoginCode loginCode) throws Exception
    {
        String field = "virtual_" + orderId;
        //删除缓存记录
        String json = userCache.getValue(field);
        userCache.hDel(field, "virtual-order");
        if (!success)
        {
            return SUCCESS;
        }
        if (json == null)
        {
            return UNDEFINE_ORDER;
        }
        return saveSuccessVirtualOrder(json, loginCode);
    }

    /**
     * 进行查单验证
     *
     * @param json      验证单
     * @param loginCode 登录态
     * @return 验证结果
     */
    private OperatorBase.ERROR saveSuccessVirtualOrder(String json, LoginCode loginCode) throws Exception
    {
        RecordVirtual record = XwhTool.parseJSONByFastJSON(json, RecordVirtual.class);
        if (record == null)
        {
            return UNDEFINE_ORDER;
        }
        GetBalance.ResponseImpl balance = getBalance(loginCode.weChatAppId(), loginCode.weChatSessionKey());
        if (balance == null || balance.code != 200 || !balance.status || balance.errcode != 0)
        {
            return UNDEFINE_ORDER;
        }
        int current = record.getVirtualValue() + record.getHistoryBalance();
        if (balance.balance < current)
        {
            return VIRTUAL_FAIL;
        }
        record.setBalance(balance.balance);
        record.setFirstSave(balance.first_save);
        record.setSaveSum(balance.save_sum);
        record.setInsertTime(new Timestamp(System.currentTimeMillis()));
        buySuccess(record.getGoodsId());
        record.setSuccess(true);
        FishInfoDb.instance().saveOrUpdate(record, true);
        return SUCCESS;
    }

    /**
     * 购买商品成功
     *
     * @param goodsId 商品编号
     */
    public void buySuccess(int goodsId)
    {
        userCache.hincrBy(getBuyRedis(), 1);
        GoodsValue goods = getGoodsValue(goodsId);
        if (goods == null)
            return;
        //人民币 = 分
        long rmb = goods.getRmb();
        double payCost = userCache.hincrByFloat(getBuyCostRedis(), XWHMathTool.divide(rmb, 100, 2));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payCost", payCost);
        userCache.updateUserData(jsonObject);
        MarketService.WealthType wealthType = MarketService.WealthType.valueOf(goods.getWealthType());
        int value = goods.getTotal();
        switch (wealthType)
        {
            case pearl:
            {
                userCache.incrPearl(value);
            }
            break;
            case shell:
            {
                userCache.incrShell(value);
            }
            break;
            default:
                break;
        }
        RabbitMQ.addPayQueue(userCache.userId(), goodsId, rmb, System.currentTimeMillis());
    }

    /**
     * 获取余额信息
     *
     * @param appId      appId
     * @param sessionKey 获取sessionKey
     */
    public GetBalance.ResponseImpl getBalance(String appId, String sessionKey)
    {
        String host = Systemstatusinfo.getText("virtual_host");
        String url = "/midas/getBalance";
        GetBalance.RequestImpl req = new GetBalance.RequestImpl();
        req.appId = appId;
        req.openid = userCache.getOpenid();
        req.sessionKey = sessionKey;
        String requestJson = XwhTool.getJSONByFastJSON(req);
        LOG.debug("进行查询余额：" + host + url + ",data=" + requestJson);
        String json = XwhHttp.sendPost(host, url, requestJson);
        if (json != null)
        {
            LOG.debug("查詢虚拟币信息:" + requestJson + ",post=" + json);
            return XwhTool.parseJSONByFastJSON(json, GetBalance.ResponseImpl.class);
        }
        return null;
    }


}
