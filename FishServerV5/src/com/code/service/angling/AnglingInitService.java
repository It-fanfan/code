package com.code.service.angling;

import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.achievement.ConfigProp;
import com.code.dao.entity.fish.config.ConfigMaterial;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.fish.userinfo.UserProps;
import com.code.dao.entity.record.RecordAngling;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BigData;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.code.protocols.core.AnglingBase.*;

public class AnglingInitService
{
    private static Logger LOG = LoggerFactory.getLogger(AnglingInitService.class);
    private UserCache userCache;
    //当前玩家道具数据
    private UserProps userProps;
    private Vector<FishResult> userFishes;


    public AnglingInitService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 更新垂钓记录信息
     *
     * @param userCache 玩家信息
     * @param record    记录信息
     * @return 是否成功
     */
    public static void updateRecordAngling(UserCache userCache, RecordAngling record)
    {
        //添加记录
        record.setInsertTime(new Timestamp(System.currentTimeMillis()));
        try
        {
            userCache.hSet(getRecordAnglingRedis(String.valueOf(record.getDayFlag())), XwhTool.getJSONByFastJSON(record));
            FishInfoDb.instance().saveOrUpdate(record, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取垂钓记录
     *
     * @param userCache 用户信息
     * @return 。。
     */
    public static RecordAngling getRecordAngling(UserCache userCache)
    {
        String date = XwhTool.getFormaterTime(System.currentTimeMillis(), "yyyyMMdd");
        String value = userCache.getValue(getRecordAnglingRedis(date));
        RecordAngling recordAngling = new RecordAngling();
        recordAngling.setDayFlag(Integer.parseInt(date));
        recordAngling.setUserId(userCache.userId());
        if (value == null)
        {
            String sql = "select * from record_angling where userId=" + userCache.getUserId() + " and dayFlag=" + date;
            Vector<RecordAngling> bySQL = FishInfoDb.instance().findBySQL(sql, RecordAngling.class);
            if (bySQL != null && !bySQL.isEmpty())
            {
                return bySQL.firstElement();
            }
        } else
            recordAngling = XwhTool.parseConfigString(value, RecordAngling.class);
        return recordAngling;
    }

    //记录垂钓
    private static String getRecordAnglingRedis(String date)
    {
        return "record-angling" + "-" + date;
    }

    /**
     * 实现基础数据初始化
     *
     * @return 初始节点
     */
    public static AnglingInit getInit()
    {
        AnglingInit init = new AnglingInit();
        Vector<ConfigMaterial> materials = FishInfoDao.instance().getCacheListByClass(ConfigMaterial.class);
        //初始获取材料
        init.materials = BigData.getBigData(materials, ConfigMaterial.class);
        //偷贝壳配置
        init.stealshell = getStealShellConfig();
        init.rodconfig = getFishingRodConfig();
        //大转盘数据
        String bigWheel = Systemstatusinfo.getText("angling-big-wheel");
        if (bigWheel != null)
        {
            init.wheel = XwhTool.parseJSONByFastJSON(bigWheel, BigWheel.class);
        }
        if (init.wheel == null)
        {
            init.wheel = new BigWheel();
            init.wheel.booklimit = 2;
            init.wheel.rate = 0.1;
        }
        Vector<ConfigProp> props = FishInfoDao.instance().getCacheListByClass(ConfigProp.class);
        init.props = BigData.getBigData(props, ConfigProp.class);
        init.shellfishprop = getShellFishProp();
        return init;
    }

    public static int getShellFishProp()
    {
        return Systemstatusinfo.getInt("angling-shell-fish", "100");
    }

    /**
     * 获取玩家道具信息
     *
     * @return 道具数据
     */
    private static Map<String, Integer> getUserProp(UserCache userCache)
    {
        Map<String, Integer> props = new HashMap<>();
        //道具数量
        Map<String, String> searchValue = new HashMap<>();
        searchValue.put("local", "config_prop");
        Vector<UserProps> propList = FishInfoDao.searchDate(UserProps.class, userCache.getUserId(), searchValue);
        if (propList != null && !propList.isEmpty())
            propList.forEach(userProps -> props.put(String.valueOf(userProps.getProductId()), userProps.getTotal()));
        return props;
    }

    /**
     * 偷贝壳配置信息
     */
    public static Vector<StealShellConfig> getStealShellConfig()
    {
        String json = Systemstatusinfo.getText("angling-tradeShell-reward");
        Vector<StealShellConfig> configs = null;
        if (json != null)
        {
            configs = XwhTool.parseJSONByFastJSON(json, new TypeToken<Vector<StealShellConfig>>()
            {
            }.getType());
        }
        if (configs == null)
        {
            configs = new Vector<>();
        }
        if (configs.isEmpty())
        {
            configs.add(new StealShellConfig(3, 0, 1, 0.8, 1000));
            configs.add(new StealShellConfig(2, 0.1, 0, 0.16, 500));
            configs.add(new StealShellConfig(1, 0.3, 0, 0.04, 0));
        }
        return configs;
    }

    /**
     * 鱼竿配置信息
     *
     * @return 参数
     */
    public static FishingRodConfig getFishingRodConfig()
    {
        String json = Systemstatusinfo.getText("fishing-rod");
        FishingRodConfig config = null;
        if (json != null)
            config = XwhTool.parseJSONByFastJSON(json, FishingRodConfig.class);
        return config;
    }

    /**
     * 检测当次垂钓是否有效
     *
     * @param endurance 耐力值
     * @param propId    使用道具编号
     * @return 垂钓结果
     */
    public boolean reduce(int endurance, int propId)
    {
        //检测是否有效
        FishingRodConfig config = getFishingRodConfig();
        FishingRod rod = userCache.getFishingRod(config);
        //耐力值KO结束
        if (endurance > rod.endurance || endurance <= 0)
            return false;
        if (!existAndUpdateProp(propId))
        {
            return false;
        }
        userCache.costFishingRod(config);
        return true;
    }

    /**
     * 更新垂钓记录
     *
     * @param fishSize 垂钓鱼实际条数
     */
    public void updateAnglingRecord(int fishSize)
    {
        RecordAngling record = getRecordAngling(userCache);
        //添加记录
        record.setDayCount(record.getDayCount() + 1);
        record.setFishCount(record.getFishCount() + fishSize);
        updateRecordAngling(userCache, record);
    }

    /**
     * 設置垂釣鱼数据
     *
     * @param fishResults 垂钓鱼信息
     * @return 垂钓信息
     */
    public int setFishDataResult(Vector<FishResult> fishResults)
    {
        try (FishDataService service = new FishDataService(userCache))
        {
            int size = service.getFishData(fishResults);
            userFishes = service.getUserFishes();
            return size;
        }
    }

    /**
     * 获取玩家当前所拥有的鱼
     */
    public Vector<FishResult> getUserFishData()
    {
        return userFishes;
    }

    /**
     * 获取用户船舵信息
     */
    private Vector<Integer> getUserRudders()
    {
        //TODO：船舵数据未定义 表数据未建
        Map<String, String> searchValue = new HashMap<>();
        searchValue.put("local", "product_rudder");
        Vector<UserProps> instance = FishInfoDao.searchDate(UserProps.class, userCache.getUserId(), searchValue);
        Vector<Integer> result = new Vector<>();
        if (instance == null)
        {
            return result;
        }
        return instance.stream().map(UserProps::getProductId).collect(Collectors.toCollection(Vector::new));
    }

    /**
     * 实现垂钓玩家初始化数据
     *
     * @return 节点
     */
    public AnglingUser getUser()
    {
        AnglingUser user = new AnglingUser();
        //船舵
        user.rudders = getUserRudders();
        user.rod = userCache.getFishingRod(getFishingRodConfig());
        user.operation = userCache.getLastOperationData();
        user.props = getUserProp(userCache);
        return user;
    }

    /**
     * 获取道具数据
     */
    public Map<String, Integer> getCurrentProp()
    {
        if (userProps != null)
        {
            Map<String, Integer> hash = new HashMap<>();
            hash.put(String.valueOf(userProps.getProductId()), userProps.getTotal());
            return hash;
        }
        return null;
    }

    /**
     * 进行检测使用道具
     */
    public boolean existAndUpdateProp(int propId)
    {
        if (propId <= 0)
            return true;
        userProps = getUserProps(propId);
        if (userProps != null)
        {
            try
            {
                //道具不足，不进行扣除
                if (userProps.getTotal() <= 0)
                    return false;
                userProps.setTotal(userProps.getTotal() - 1);
                userProps.setInsertTime(new Timestamp(System.currentTimeMillis()));
                FishInfoDao.instance().saveOrUpdate(userProps, true);
                return true;
            } catch (Exception e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
        }
        return false;
    }

    /**
     * 进行添加道具
     */
    public void addProps(int propId, int total)
    {
        if (propId <= 0)
            return;
        UserProps userProps = getUserProps(propId);
        if (userProps == null)
            userProps = new UserProps();
        userProps.setInsertTime(new Timestamp(System.currentTimeMillis()));
        userProps.setTotal(userProps.getTotal() + total);
        userProps.setLocal("config_prop");
        userProps.setProductId(propId);
        userProps.setUserId(userCache.userId());
        try
        {
            FishInfoDao.instance().saveOrUpdate(userProps, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取道具信息
     *
     * @param propId 道具编号
     */
    private UserProps getUserProps(int propId)
    {
        Map<String, String> searchValue = new HashMap<>();
        searchValue.put("local", "config_prop");
        searchValue.put("productId", String.valueOf(propId));
        Vector<UserProps> props = FishInfoDao.searchDate(UserProps.class, userCache.getUserId(), searchValue);
        if (props != null && !props.isEmpty())
            return props.firstElement();
        return null;
    }
}
