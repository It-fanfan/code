package com.code.service.order;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.*;
import com.code.dao.entity.fish.userinfo.UserOrderExt;
import com.code.dao.entity.record.RecordOrder;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BasePro;
import com.code.protocols.basic.BigData;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.core.recover.OrderFish;
import com.code.protocols.login.Init;
import com.code.service.angling.FishDataService;
import com.code.service.book.BookInitService;
import com.code.service.rate.OrderHit;
import com.code.service.reward.RewardService;
import com.utils.XWHMathTool;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

import static com.code.protocols.core.AnglingBase.ERROR;
import static com.code.protocols.core.AnglingBase.FishResult;

/**
 * 订单服务
 */
public class OrderService
{
    //订单服务
    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    //订单数据信息
    private static final Logger ORDER = LoggerFactory.getLogger("orderLog");
    private UserCache userCache;
    private UserOrder userOrder = null;

    public OrderService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 设置初始配置信息
     *
     * @return
     */
    public static Init.OrderInit getInitConfig()
    {
        Init.OrderInit init = new Init.OrderInit();
        init.boss = BigData.getBigData(getBossId(), ConfigOrderBoss.class);
        init.introduce = BigData.getBigData(getIntroduce(), ConfigOrderIntroduce.class);
        init.finishcd = Systemstatusinfo.getInt("order_finish_cd");
        init.refreshconfig = refreshConfig();
        return init;
    }

    /**
     * 生成订单
     *
     * @param books 图鉴数据
     * @return 订单信息
     */
    private static Order generateOrder(Queue<Integer> books)
    {
        //设置鱼上限数 水域总容量 / ( 4 + 水域等级 / 2)
        int limit = 6;
        Order order = new Order();
        order.fishes = new HashMap<>();
        for (int i = 0; i < limit; i++)
        {
            Integer ftId = books.poll();
            if (ftId == null)
                break;
            if (order.fishes.containsKey(ftId))
                continue;
            OrderHit.OrderRate rate = (OrderHit.OrderRate) new OrderHit().getInstance().hit();
            if (rate == null)
                continue;
            int count = ThreadLocalRandom.current().nextInt(rate.min, rate.max);
            OrderFish fish = new OrderFish();
            fish.count = count;
            fish.ftid = ftId;
            order.fishes.put(ftId, fish);
            order.price += calculatePrice(ftId, rate, count);
        }
        if (Systemstatusinfo.getBoolean("sys_status"))
        {
            if (ThreadLocalRandom.current().nextBoolean())
            {
                Vector<ConfigMaterial> materials = FishInfoDao.instance().getCacheListByClass(ConfigMaterial.class);
                order.rewards = new Vector<>();
                materials.forEach(configMaterial ->
                {
                    if (ThreadLocalRandom.current().nextBoolean())
                    {
                        int total = ThreadLocalRandom.current().nextInt(1, 10);
                        order.rewards.add(new BasePro.RewardInfo(BasePro.RewardType.material.name(), configMaterial.getId(), total));
                    }
                });
                System.out.println("获取其他奖励" + XwhTool.getJSONByFastJSON(order.rewards));
            }
        }

        return order;
    }

    /**
     * 計算價格
     *
     * @param ftId  鱼类型
     * @param rate  命中公式
     * @param count 数量
     * @return 价格
     */
    private static int calculatePrice(Integer ftId, OrderHit.OrderRate rate, int count)
    {
        ConfigFish config = (ConfigFish) FishInfoDb.instance().getCacheKey(ConfigFish.class, new String[]{"ftId", ftId.toString()});
        if (config == null)
            return 0;
        Double temp = XWHMathTool.divide(count * config.getLightExpend() * rate.single, rate.denominator * config.getBasin());
        return temp.intValue();
    }

    /**
     * 生成一个包体
     *
     * @param lights 点亮图鉴
     */
    private static Queue<Integer> generateBooks(Vector<Integer> lights)
    {
        int max = 100;
        //进行填坑
        Vector<Integer> books = new Vector<>();
        setBooks(max, 1, books, lights);
        Collections.shuffle(books);
        return new ConcurrentLinkedQueue<>(books);
    }

    /**
     * 设置图鉴数据
     *
     * @param max   最大值
     * @param rate  概率值
     * @param books 图鉴
     * @param fill  填充值
     */
    private static void setBooks(int max, double rate, Vector<Integer> books, Vector<Integer> fill)
    {
        if (!fill.isEmpty())
        {
            int limit = (int) (max * rate);
            while (limit > 0)
            {
                books.addAll(fill);
                limit -= fill.size();
            }
        }
    }

    /**
     * 进行创建一笔订单信息
     *
     * @param refresh 是否为刷新订单
     * @return 订单数据
     */
    private static void createOrder(int index, UserCache userCache, UserOrder userOrder, boolean refresh)
    {
        int basinId = Integer.valueOf(userCache.getBasin());
        Queue<Integer> packet;
        if (userOrder.packet.size() < 6 || userOrder.basinId != basinId || !refresh)
        {
            userOrder.basinId = basinId;
            packet = generateBooks(userCache);
        } else
        {
            packet = new ConcurrentLinkedQueue<>(userOrder.packet);
        }
        Vector<ConfigOrderBoss> _boss = getBossId();
        userOrder.orders.forEach(order -> _boss.stream().filter(instance -> instance.getId() == order.bossId).findFirst().ifPresent(_boss::removeElement));
        Order order = generateOrder(packet);
        if (order != null)
        {
            order.introduce = getIntroduce().firstElement().getId();
            if (!_boss.isEmpty())
            {
                order.bossId = _boss.firstElement().getId();
            }
            //刷新订单，进行记录
            if (refresh)
            {
                order.lastRefresh = System.currentTimeMillis();
            }
            userOrder.orders.add(index, order);
            userOrder.packet = new Vector<>(packet);
            putUserOrder(userCache, userOrder);
        }

    }

    /**
     * 获取bossId
     *
     * @return bossId list
     */
    private static Vector<ConfigOrderBoss> getBossId()
    {
        Vector<ConfigOrderBoss> bosses = FishInfoDb.instance().getCacheListByClass(ConfigOrderBoss.class);
        Collections.shuffle(bosses);
        return bosses;
    }

    /**
     * 描述
     *
     * @return 数据
     */
    private static Vector<ConfigOrderIntroduce> getIntroduce()
    {
        Vector<ConfigOrderIntroduce> introduces = FishInfoDb.instance().getCacheListByClass(ConfigOrderIntroduce.class);
        Collections.shuffle(introduces);
        return introduces;
    }

    /**
     * 构造初始订单数据
     *
     * @param userCache 玩家信息
     * @return 订单数据
     */
    private static UserOrder generateUserOrder(UserCache userCache)
    {
        UserOrder userOrder = new UserOrder();
        userOrder.orders = new Vector<>();
        userOrder.packet = new Vector<>();
        Queue<Integer> packet = generateBooks(userCache);
        //订单数量
        int limit = 5;
        Queue<ConfigOrderBoss> bossId = new ConcurrentLinkedQueue<>(getBossId());
        for (int i = 0; i < limit; i++)
        {
            Order order = generateOrder(packet);
            if (order != null)
            {
                ConfigOrderBoss boss = bossId.poll();
                if (boss != null)
                    order.bossId = boss.getId();
                order.introduce = getIntroduce().firstElement().getId();
                userOrder.orders.add(order);
            }
        }
        userOrder.packet = new Vector<>(packet);
        putUserOrder(userCache, userOrder);
        return userOrder;
    }

    /**
     * 生成訂單图鉴信息
     *
     * @return 图鉴数据
     */
    private static Queue<Integer> generateBooks(UserCache userCache)
    {
        long shell = userCache.shell();
        BookInitService bookService = new BookInitService(userCache);
        Vector<Integer> lights = bookService.getUserBook(BookInitService.BookType.open);
        //下一个点亮图鉴参数
        ConfigFish configFish = BookInitService.getNextBook(lights);
        if (configFish != null && shell > configFish.getLightExpend())
        {
            lights.add(configFish.getFtId());
        }
        return generateBooks(lights);
    }

    /**
     * 获取玩家订单数据
     *
     * @param userCache 玩家信息
     * @return 订单数据
     */
    private static UserOrder getUserOrder(UserCache userCache)
    {
        UserOrder userOrder = null;
        String json = userCache.getValue(getUserField());
        if (json != null)
            userOrder = XwhTool.parseJSONByFastJSON(json, UserOrder.class);
        if (userOrder == null)
        {
            userOrder = generateUserOrder(userCache);

        }
        return userOrder;
    }

    /**
     * 设置玩家订单信息
     *
     * @param userCache 玩家信息
     * @param userOrder 订单数据
     */
    public static void putUserOrder(UserCache userCache, UserOrder userOrder)
    {
        userCache.hSet(getUserField(), XwhTool.getJSONByFastJSON(userOrder));
        saveUserOrder(userOrder, userCache);
    }

    /**
     * 订单redis数据key
     *
     * @return field
     */
    private static String getUserField()
    {
        return "order-ext";
    }

    /**
     * 保存玩家数据信息
     *
     * @param userOrder 玩家订单
     * @param userCache 玩家缓存
     */
    private static void saveUserOrder(UserOrder userOrder, UserCache userCache)
    {
        UserOrderExt ext = new UserOrderExt();
        ext.setUserId(userCache.userId());
        ext.setInsertTime(new Timestamp(System.currentTimeMillis()));
        ext.setLastFinish(userOrder.lastFinish);
        ext.setOrderInfo(XwhTool.getJSONByFastJSON(userOrder));
        LOG.debug("玩家数据！！" + XwhTool.getJSONByFastJSON(ext));
        try
        {
            FishInfoDb.instance().saveOrUpdate(ext, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取刷新珍珠数值
     *
     * @return 珍珠
     */
    public static int getRefreshPearl(long lastRefreshTime)
    {
        AnglingBase.RefreshConfig config = refreshConfig();
        //剩余CD时间比例
        long sub = config.cd + lastRefreshTime - System.currentTimeMillis();
        //刷新时间结束
        if (sub <= 0)
            return 0;
        Double pearl = XWHMathTool.divide(config.pearl * sub, config.cd, 0);
        if (pearl.intValue() == 0)
        {
            return 1;
        }
        return pearl.intValue();
    }

    /**
     * 刷新配置信息
     *
     * @return 参数信息
     */
    private static AnglingBase.RefreshConfig refreshConfig()
    {
        String json = Systemstatusinfo.getText("order_refresh_config");
        return XwhTool.parseJSONByFastJSON(json, AnglingBase.RefreshConfig.class);
    }

    /**
     * 玩家订单
     *
     * @return 订单数据
     */
    public UserOrder getUserOrder()
    {
        if (userOrder == null)
            return userOrder = getUserOrder(userCache);
        return userOrder;
    }

    /**
     * 进行接单数据
     *
     * @param index     索引
     * @param userOrder 玩家訂單數據
     * @return 订单异常
     */
    public ERROR setOrderResult(int index, UserOrder userOrder)
    {
        try
        {
            if (index < userOrder.orders.size())
            {
                Order order = userOrder.orders.elementAt(index);
                try (FishDataService service = new FishDataService(userCache))
                {
                    Vector<OrderFish> fishes = new Vector<>(order.fishes.values());
                    Vector<FishResult> userFishData = service.getUserFishes();
                    if (!checkOrder(fishes, userFishData))
                    {
                        return ERROR.UNSATISFIED;
                    }
                    //进行删除鱼数据
                    Vector<FishResult> remove = new Vector<>();
                    fishes.forEach(orderFish -> remove.add(new FishResult(orderFish.ftid, orderFish.count)));
                    service.removeFishData(remove);
                }
                //移除订单，进行生成新订单
                if (removeOrder(userOrder, index))
                {
                    userOrder.lastFinish = System.currentTimeMillis();
                    createOrder(index, userCache, userOrder, false);
                }
                RewardService rewardService = new RewardService(userCache);
                Vector<BasePro.RewardInfo> rewards = new Vector<>();
                rewards.add(new BasePro.RewardInfo("shell", -1, order.price));
                if (order.rewards != null)
                {
                    rewards.addAll(order.rewards);
                }
                rewardService.receiveReward(rewards, "order");
                addOrderRecord(order, index);
                return ERROR.SUCCESS;
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return ERROR.NO_ORDER;
    }

    /**
     * 进行刷新订单
     *
     * @param index     索引
     * @param userOrder 订单数据
     * @return 操作码
     */
    public ERROR refreshOrder(UserOrder userOrder, int index)
    {
        if (removeOrder(userOrder, index))
        {
            createOrder(index, userCache, userOrder, true);
            return ERROR.SUCCESS;
        }
        return ERROR.NO_ORDER;
    }

    /**
     * 进行移除订单信息
     *
     * @param userOrder 玩家订单
     * @param index     索引数据
     * @return bool
     */
    private boolean removeOrder(UserOrder userOrder, int index)
    {
        try
        {
            if (index < userOrder.orders.size())
            {
                userOrder.orders.removeElementAt(index);
                return true;
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }

    /**
     * 通过索引获取订单数据
     *
     * @param userOrder
     * @param index
     */
    public Order getOrderByIndex(UserOrder userOrder, int index)
    {
        if (index < userOrder.orders.size())
            return userOrder.orders.elementAt(index);
        return null;
    }

    /**
     * 进行判断新增标志位
     */
    public boolean uiFlag()
    {
        UserOrder userOrder = getUserOrder();
        for (Order order : userOrder.orders)
        {
            //检查订单鱼数据书否满足
            Vector<OrderFish> fishes = new Vector<>(order.fishes.values());
            boolean b = checkOrderReach(fishes);
            if (b)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否满足订单
     *
     * @param fishes 订单内鱼数据
     * @return 是否满足
     */
    private boolean checkOrderReach(Vector<OrderFish> fishes)
    {

        try (FishDataService fishService = new FishDataService(userCache))
        {
            Vector<FishResult> userFishData = fishService.getUserFishes();
            int total = fishes.stream().mapToInt(fish -> fish.count).sum();
            if (fishService.getSum() < total)
                return false;
            return checkOrder(fishes, userFishData);
        }
    }

    /**
     * 检查是否满足订单
     *
     * @param fishes       订单信息
     * @param userFishData 鱼数据
     * @return bool
     */
    private boolean checkOrder(Vector<OrderFish> fishes, Vector<FishResult> userFishData)
    {
        for (OrderFish order : fishes)
        {
            FishResult _data = userFishData.stream().filter(result -> result.ftid == order.ftid && result.sum >= order.count).findFirst().orElse(null);
            if (_data == null)
                return false;
        }
        return true;
    }

    /**
     * 添加接单记录
     *
     * @param order 订单
     * @param index 订单索引
     */
    private void addOrderRecord(Order order, int index)
    {
        RecordOrder recordOrder = new RecordOrder();
        recordOrder.setFinishTime(new Timestamp(System.currentTimeMillis()));
        recordOrder.setOrderId(index);
        recordOrder.setUserId(userCache.userId());
        recordOrder.setPrice(order.price);
        recordOrder.setStatus(true);
        Map<String, Object> hash = new LinkedHashMap<>();
        hash.put("userId", recordOrder.getUserId());
        hash.put("index", index);
        hash.put("price", order.price);
        hash.put("finish", System.currentTimeMillis());
        hash.put("order", order);
        ORDER.error(XwhTool.getJSONByFastJSON(hash));
        try
        {
            FishInfoDb.instance().saveOrUpdate(recordOrder, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }
}
