package com.code.cache;

import com.alibaba.fastjson.JSONObject;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.userinfo.UserData;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.core.AnglingBase;
import com.code.service.trade.MarketService;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.db.BatchSQL;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


/**
 * 玩家缓存数据
 */
public class UserCache
{
    //日志打印
    private static Logger LOG = LoggerFactory.getLogger(UserCache.class);
    //是否注册
    public boolean isRegister;
    private String userId;//编号
    private String shell;//贝壳
    private String pearl;//珍珠
    private String pearlTotal;//珍珠总值
    private String shellTotal;//总贝壳
    private String loginCode;//登陆态
    //缓存对象
    private Map<String, String> hash = new HashMap<>();
    private boolean infoFlag = false;

    private UserCache(long userId)
    {
        this.userId = String.valueOf(userId);
    }

    private UserCache(String userId)
    {
        this.userId = userId;
    }

    /**
     * 玩家缓存数据
     *
     * @param userId 玩家编号
     */
    public static UserCache getUserCache(long userId)
    {
        if (userId == 0)
            return null;
        return getUserCache(String.valueOf(userId));
    }

    /**
     * @param userId 玩家编号
     * @return 检测缓存是否存在
     */
    public static boolean exist(String userId)
    {
        String key = getUserKey(userId);
        return RedisUtils.exists(key);
    }

    /**
     * 玩家缓存数据
     *
     * @param userId 玩家编号
     */
    public static UserCache getUserCache(String userId)
    {
        if (Objects.isNull(userId))
            return null;
        String key = getUserKey(userId);
        if (!RedisUtils.exists(key))
        {
            return null;
        }
        UserRedisInitField[] fields = UserRedisInitField.values();
        String[] redis = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
        {
            redis[i] = fields[i].name();
        }
        //进行遍历集合
        try
        {
            List<String> data = hMGet(key, redis);
            if (data == null || data.isEmpty())
                return null;
            UserCache userCache = new UserCache(userId);
            for (int i = 0; i < data.size(); i++)
            {
                String value = data.get(i);
                Field set = UserCache.class.getDeclaredField(redis[i]);
                if (set != null)
                {
                    set.setAccessible(true);
                    set.set(userCache, value);
                }
            }
            return userCache;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 進行設置數據參數
     *
     * @param hash 玩家数据
     * @return 緩存
     */
    public static UserCache putUserInfo(Map<String, Object> hash)
    {
        Long userId = (Long) hash.get("userId");
        UserCache userCache = getUserCache(userId);
        if (userCache == null)
            userCache = new UserCache(userId);
        try
        {
            hash.forEach(userCache::setHashValue);
            userCache.infoFlag = true;
            userCache.save(userCache.hash);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return userCache;
    }

    /**
     * 获取用户缓存数据key
     *
     * @param userId 用户编号
     * @return 值
     */
    public static String getUserKey(String userId)
    {
        return String.format("user-%s", userId);
    }

    /**
     * 获取批量数据
     *
     * @param key   key值
     * @param field 参数
     */
    private static List<String> hMGet(String key, String... field)
    {
        return RedisUtils.hmget(key, field);
    }

    /**
     * 设置玩家水域相关数据
     *
     * @param basin    水域等级
     * @param fillBook 未填充图鉴数
     */
    public void setBasinData(int basin, int fillBook)
    {
        Map<String, String> hash = new HashMap<>();
        hash.put("basin", String.valueOf(basin));
        hash.put("fillBook", String.valueOf(fillBook));
        setHashValue(hash);
        save(hash);
        updateBasinData(basin, fillBook);
    }

    /**
     * 进行移除图鉴数
     *
     * @param sub 减法数据
     */
    public int subFillBook(long sub)
    {
        String field = "fillBook";
        int fillBook = getHashValue(field);
        if (sub == 0)
            return fillBook;
        long fill = hincrBy(field, -sub);
        setHashValue(field, fill);
        return (int) fill;
    }

    /**
     * 获取hash对象数据
     *
     * @param key key值
     */
    private int getHashValue(String key)
    {
        String value = hashValue(key, "0");
        return Integer.valueOf(value);
    }

    /**
     * 获取值
     *
     * @param key         key
     * @param nullDefault 默认值
     */
    private String hashValue(String key, String nullDefault)
    {
        if (hash.containsKey(key))
            return Objects.toString(hash.get(key), nullDefault);
        String value = getValue(key);
        if (value != null)
            hash.put(key, value);
        return Objects.toString(value, nullDefault);
    }

    /**
     * 进行获取玩家基本信息
     */
    private void initUserInfo()
    {
        Vector<String> fields = new Vector<>();
        Arrays.stream(UserRedisInitField.values()).forEach(v -> fields.add(v.name()));
        Arrays.stream(UserRedisField.values()).forEach(v -> fields.add(v.name()));
        //进行遍历集合
        try
        {
            String[] redis = new String[fields.size()];
            redis = fields.toArray(redis);
            String key = getUserKey(getUserId());
            List<String> data = hMGet(key, redis);
            if (data == null || data.isEmpty())
                return;
            for (int i = 0; i < data.size(); i++)
            {
                String value = data.get(i);
                if (value == null)
                    continue;
                setHashValue(redis[i], value);
            }
            infoFlag = true;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 设置hash参数
     *
     * @param key   参数key
     * @param value 参数结果
     */
    private void setHashValue(String key, Object value)
    {
        if (value == null)
            return;
        if (value instanceof Timestamp)
        {
            hash.put(key, String.valueOf(((Timestamp) value).getTime()));
        } else
        {

            hash.put(key, value.toString());
        }
    }

    /**
     * 设置hash值
     *
     * @param hash 对象
     */
    public void setHashValue(Map<String, String> hash)
    {
        hash.forEach((k, v) -> this.hash.put(k, v));
    }

    /**
     * 进行销毁
     */
    public void save(Map<String, String> hash)
    {
        hmset(hash);
    }

    /**
     * 获取批量数据
     *
     * @param field fields
     * @return data
     */
    public List<String> hMGet(String... field)
    {
        return hMGet(getUserKey(getUserId()), field);
    }

    /**
     * 設置數據信息
     *
     * @param map 映射数据
     */
    private void hmset(Map<String, String> map)
    {
        String key = getUserKey(getUserId());
        RedisUtils.hmset(key, map);
    }

    public boolean hexist(String field)
    {
        String key = getUserKey(getUserId());
        return RedisUtils.hexists(key, field);
    }


    /**
     * 增量数据
     *
     * @param field 键值
     * @param incr  增量
     */
    public long hincrBy(String field, long incr)
    {
        String key = getUserKey(getUserId());
        return RedisUtils.hincrby(key, field, incr);
    }

    public double hincrByFloat(String field, double incr)
    {
        String key = getUserKey(getUserId());
        return RedisUtils.hincrByFloat(key, field, incr);
    }

    /**
     * 获取值信息
     *
     * @param field 组信息
     * @return 值
     */
    public String getValue(String field)
    {
        String key = getUserKey(getUserId());
        return RedisUtils.hget(key, field);
    }

    /**
     * 设置缓存数据
     *
     * @param field 键值
     * @param value 值
     */
    public void hSet(String field, String value)
    {
        RedisUtils.hset(getUserKey(getUserId()), field, value);
    }

    /**
     * 进行删除键值
     *
     * @param field 键值
     */
    public void hDel(String... field)
    {
        RedisUtils.hdel(getUserKey(getUserId()), field);
    }


    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public long userId()
    {
        String value = Objects.toString(userId, "0");
        return Long.valueOf(value);
    }

    public String getShell()
    {
        return shell;
    }

    public void setShell(String shell)
    {
        this.shell = shell;
    }

    public long shell()
    {
        String value = Objects.toString(shell, "0");
        return Long.valueOf(value);
    }

    public String getPearl()
    {
        return pearl;
    }

    public void setPearl(String pearl)
    {
        this.pearl = pearl;
    }

    public long pearl()
    {
        String value = Objects.toString(pearl, "0");
        return Long.valueOf(value);
    }

    private long pearlTotal()
    {
        String value = Objects.toString(pearlTotal, "0");
        return Long.valueOf(value);
    }

    private long shellTotal()
    {
        String value = Objects.toString(shellTotal, "0");
        return Long.valueOf(value);
    }

    private String getPearlTotal()
    {
        return pearlTotal;
    }

    private void setPearlTotal(String pearlTotal)
    {
        this.pearlTotal = pearlTotal;
    }

    private String getShellTotal()
    {
        return shellTotal;
    }

    private void setShellTotal(String shellTotal)
    {
        this.shellTotal = shellTotal;
    }

    public String getLoginCode()
    {
        return loginCode;
    }

    public void setLoginCode(String loginCode)
    {
        this.loginCode = loginCode;
        //进行设置登录态数据
        hSet(UserRedisInitField.loginCode.name(), loginCode);
    }

    /**
     * 進行給玩家增量珍珠数
     *
     * @param pearl 珍珠信息
     * @return 当前最新珍珠数
     */
    public boolean incrPearl(long pearl)
    {
        if (pearl == 0)
        {
            return false;
        }
        if (pearl + pearl() < 0)
            return true;
        if (pearl > 0)
        {
            long shellTotal = hincrBy(UserRedisInitField.pearlTotal.name(), pearl);
            this.setPearlTotal(String.valueOf(shellTotal));
        }
        long value = hincrBy(UserRedisInitField.pearl.name(), pearl);
        if (value < 0)
        {
            hincrBy(UserRedisInitField.pearl.name(), -pearl);
            return true;
        }
        setPearl(String.valueOf(value));
        updateValueData();
        //当前市场操作信息
        MarketService.addTradeRecord(this, pearl, MarketService.WealthType.pearl);
        return false;
    }

    /**
     * 進行給玩家增量贝壳数
     *
     * @param shell 贝壳信息
     * @return 当前最新贝壳数
     */
    public boolean incrShell(long shell)
    {
        if (shell == 0)
        {
            return true;
        }
        if (shell + shell() < 0)
            return false;
        if (shell > 0)
        {
            long shellTotal = hincrBy(UserRedisInitField.shellTotal.name(), shell);
            this.setShellTotal(String.valueOf(shellTotal));
        }
        long value = this.hincrBy(UserRedisInitField.shell.name(), shell);
        if (value < 0)
        {
            this.hincrBy(UserRedisInitField.shell.name(), -shell);
            return false;
        }
        this.setShell(String.valueOf(value));
        updateValueData();
        //当前市场操作信息
        MarketService.addTradeRecord(this, shell);
        return true;
    }

    /**
     * 更新玩家数据
     */
    private void updateValueData()
    {
        try
        {
            UserData userData = new UserData();
            userData.setUserId(userId());
            userData.setPearl(pearl());
            if (pearlTotal == null)
                pearlTotal = pearl;
            userData.setPearlTotal(pearlTotal());
            userData.setShell(shell());
            if (shellTotal == null)
                shellTotal = shell;
            userData.setShellTotal(shellTotal());
            userData.setModiftyTime(new Timestamp(System.currentTimeMillis()));
            FishInfoDb.instance().saveOrUpdate(userData, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 更新水域信息
     *
     * @param basin    当前海域
     * @param fillBook 剩余填充图鉴数
     */
    private void updateBasinData(int basin, int fillBook)
    {
        BatchSQL batchSQL = new BatchSQL()
        {
            @Override
            public String getSQL()
            {
                return "INSERT INTO`user_preview`(userId,basinId,fillBook)VALUE(?,?,?) ON DUPLICATE KEY UPDATE basinId=VALUES(basinId),fillBook=VALUES(fillBook)";
            }

            @Override
            public int getLength()
            {
                return 1;
            }

            @Override
            public void addBatch(PreparedStatement prest, int index) throws SQLException
            {
                int parameterIndex = 1;
                prest.setLong(parameterIndex++, userId());
                prest.setInt(parameterIndex++, basin);
                prest.setInt(parameterIndex, fillBook);
            }
        };
        FishInfoDb.instance().execBatchSQL(batchSQL);
    }


    /**
     * 记录上一次操作数据
     *
     * @param operationType 操作记录
     */
    public Map<String, Integer> recordLastOperationData(AnglingBase.OperationType operationType)
    {
        String field = "fishing-operation";
        Map<String, Integer> cache = getLastOperationData();
        String key = operationType.name();
        if (cache.containsKey(key))
        {
            cache.put(key, cache.get(key) + 1);
        } else
        {
            cache.clear();
            cache.put(key, 1);
        }
        hSet(field, XwhTool.getJSONByFastJSON(cache));
        return cache;
    }

    /**
     * 获取上一次操作类型数据
     *
     * @return 操作记录
     */
    public Map<String, Integer> getLastOperationData()
    {
        //获取上一次记录最新操作记录
        String field = "fishing-operation";
        Map<String, Integer> map = XwhTool.parseJSONByFastJSON(getValue(field), new TypeToken<Map<String, Integer>>()
        {
        }.getType());
        if (map == null)
            map = new HashMap<>();
        return map;
    }

    /**
     * 获取鱼竿数据
     *
     * @return 鱼竿信息
     */
    public AnglingBase.FishingRod getFishingRod(AnglingBase.FishingRodConfig config)
    {
        String field = UserRedisField.fishingRod.name();
        String json = getValue(field);
        AnglingBase.FishingRod rod = null;
        if (json != null)
        {
            rod = XwhTool.parseJSONByFastJSON(json, AnglingBase.FishingRod.class);
        }
        int dayFlag = XwhTool.getCurrentDateValue();
        if (rod == null)
        {
            rod = new AnglingBase.FishingRod();
            rod.video = config.video;
            rod.endurance = config.endurance;
            rod.dayflag = dayFlag;
            hSet(field, XwhTool.getJSONByFastJSON(rod));
        } else
        {
            boolean update = false;
            if (rod.dayflag != dayFlag)
            {
                rod.dayflag = dayFlag;
                rod.video = config.video;
                update = true;
            }
            if (rod.endurance != config.endurance)
            {
                long _elementSingle = 1000 * 60;
                long sub = (System.currentTimeMillis() - rod.uploadtime) / (_elementSingle * config.frequency);
                if (sub > 0)
                {
                    long single = (System.currentTimeMillis() - rod.uploadtime) % (_elementSingle * config.frequency);
                    rod.uploadtime = System.currentTimeMillis() - single;
                    rod.endurance += sub;
                    if (rod.endurance >= config.endurance)
                    {
                        rod.endurance = config.endurance;
                        rod.uploadtime = 0;
                    }
                    update = true;
                }
            }
            if (update)
                hSet(field, XwhTool.getJSONByFastJSON(rod));
        }
        return rod;
    }

    /**
     * @param rod    鱼钩信息
     * @param config 鱼钩配置
     */
    public void restoreFishingRod(AnglingBase.FishingRod rod, AnglingBase.FishingRodConfig config)
    {
        rod.endurance = config.endurance;
        rod.uploadtime = 0;
        String field = UserRedisField.fishingRod.name();
        hSet(field, XwhTool.getJSONByFastJSON(rod));
    }

    /**
     * 消耗鱼竿耐力值
     */
    public void costFishingRod(AnglingBase.FishingRodConfig config)
    {
        AnglingBase.FishingRod rod = getFishingRod(config);
        String field = UserRedisField.fishingRod.name();
        if (rod.endurance > 0)
        {
            rod.endurance--;
            rod.uploadtime = System.currentTimeMillis();
            hSet(field, XwhTool.getJSONByFastJSON(rod));
        }
    }

    /**
     * 更新材料数据
     *
     * @param material 材料
     */
    public void updateMaterial(AnglingBase.MaterialResult material)
    {
        String field = UserRedisField.material.name();
        Map<Integer, Integer> cache = getMaterial();
        Integer total = cache.get(material.id);
        if (total == null)
            total = 0;
        total += material.count;
        cache.put(material.id, total);
        String json = XwhTool.getJSONByFastJSON(cache);
        hSet(field, json);
        //更新数据库
        String SQL = String.format("update user_fish_ext set material='%s' where userId=%s", json, getUserId());
        FishInfoDao.instance().addQueue(SQL);
    }

    /**
     * 更新用户数值
     *
     * @param update 更新列
     */
    public void updateUserData(JSONObject update)
    {
        StringBuilder SQL = new StringBuilder();
        SQL.append("update user_data set ");
        int len = SQL.length();
        for (String key : update.keySet())
        {
            if (len != SQL.length())
                SQL.append(",");
            SQL.append(key).append("=").append(update.get(key));
        }
        SQL.append(" where userId=").append(getUserId());
        FishInfoDao.instance().addQueue(SQL.toString());
    }

    /**
     * 获取玩家材料数据
     *
     * @return 材料信息
     */
    public Map<Integer, Integer> getMaterial()
    {
        String field = UserRedisField.material.name();
        String json = getValue(field);
        return XwhTool.parseJsonMap(json);
    }

    /**
     * 海域信息
     */
    public int getBasin()
    {
        String field = UserRedisField.basin.name();
        return getHashValue(field);
    }

    /**
     * 当前海域未开启图鉴数
     */
    public int getFillBook()
    {
        String field = UserRedisField.fillBook.name();
        return getHashValue(field);
    }

    /**
     * 获取昵称
     */
    public String getNickName()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.nickName.name());
    }

    public String getIcon()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.icon.name());
    }

    public String getOpenid()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.openId.name());
    }

    public String getSex()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.sex.name());
    }

    public String getRegisterTime()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.registerTime.name());
    }

    public String getUnionId()
    {
        if (!infoFlag)
            initUserInfo();
        return hash.get(UserRedisField.unionId.name());
    }

    /**
     * 设置用户简单集合
     *
     * @param userId   用户ID
     * @param icon     ICON
     * @param nickName 昵称
     */
    public static void setRankingUser(long userId, String icon, String nickName)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", userId);
        jsonObject.put("icon", icon);
        jsonObject.put("nickname", nickName);
        String key = getUserCollectsKey();
        RedisUtils.hset(key, String.valueOf(userId), jsonObject.toJSONString());
    }

    /**
     * 获取用户集合信息
     *
     * @param userId 用户编号
     * @return 集合
     */
    public static List<String> getUserCollects(String... userId)
    {
        String key = getUserCollectsKey();
        return RedisUtils.hmget(key, userId);
    }

    /**
     * 用户集合信息
     *
     * @return 用户集合key
     */
    private static String getUserCollectsKey()
    {
        return "ranking-user-collect";
    }


}
