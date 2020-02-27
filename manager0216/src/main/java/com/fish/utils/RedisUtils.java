package com.fish.utils;

import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.io.InputStream;
import java.util.*;

/**
 * Redis工具类
 *
 * @author Sky
 */
public class RedisUtils
{
    // 日志打印
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static int AVOID_REPEATABLE_COMMIT_DB = 0;

    private static JedisPool jedisPool = null;

    static
    {
        try
        {
            InputStream in = RedisUtils.class.getResourceAsStream("/redis.properties");

            if (in == null)
            {
                throw new IllegalArgumentException("[redis.properties] is not found!");
            }
            Properties bundle = new Properties();
            bundle.load(in);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.parseInt(bundle.getProperty("redis.pool.maxTotal")));
            config.setMaxIdle(Integer.parseInt(bundle.getProperty("redis.pool.maxIdle")));
            config.setMaxWaitMillis(Long.parseLong(bundle.getProperty("redis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.parseBoolean(bundle.getProperty("redis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.parseBoolean(bundle.getProperty("redis.pool.testOnReturn")));
            String host = bundle.getProperty("redis.host");
            int port = Integer.parseInt(bundle.getProperty("redis.port"));
            AVOID_REPEATABLE_COMMIT_DB = Integer.parseInt(bundle.getProperty("redis.db"));
            if (bundle.containsKey("redis.open") && Boolean.valueOf(bundle.getProperty("redis.open")))
            {
                jedisPool = new JedisPool(config, host, port, Integer.valueOf(bundle.getProperty("redis.timeout")), bundle.getProperty("redis.auth"));
            } else
                jedisPool = new JedisPool(config, host, port);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     */
    private synchronized static Jedis getJedis()
    {
        try
        {
            if (jedisPool != null)
            {
                return jedisPool.getResource();
            } else
            {
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     */
    public static void close(final Jedis jedis)
    {
        if (jedis != null)
        {
            jedis.close();
        }
    }

    /**
     * 进行增长步长
     */
    public static Long incrby(String key, long value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.incrBy(key, value);
            }
        } catch (Exception e)
        {
            logger.error("incrBy:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 进行减去步长
     */
    public static Long decrby(String key, long value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.decrBy(key, value);
            }
        } catch (Exception e)
        {
            logger.error("decrby:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 进行设置排序数据
     */
    public static void zAdd(String key, double score, String member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.zadd(key, score, member);
            }
        } catch (Exception e)
        {
            logger.error("zAdd:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 批量插入数据
     */
    public static void zadd(String key, Map<String, Double> scoreMembers)
    {

        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.zadd(key, scoreMembers);
            }
        } catch (Exception e)
        {
            logger.error("zadd:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 獲取所有對象數據
     */
    public static List<String> hvals(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hvals(key);
            }
        } catch (Exception e)
        {
            logger.error("hvals:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 设置hash对象数据
     */
    public static void hmset(String key, Map<String, String> hash)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.hmset(key, hash);
            }
        } catch (Exception e)
        {
            logger.error("hmset:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 批量获取hash对象数据
     */
    public static List<String> hmget(String key, String... fields)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hmget(key, fields);
            }
        } catch (Exception e)
        {
            logger.error("hmset:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 检测是否存在
     */
    public static boolean hexists(String key, String field)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hexists(key, field);
            }
        } catch (Exception e)
        {
            logger.error("hmset:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return false;
    }

    /**
     * 哈希表中的字段值加上指定增量值
     *
     * @return 返回目前增量值
     */
    public static Long hincrby(String key, String field, long val)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hincrBy(key, field, val);
            }
        } catch (Exception e)
        {
            logger.error("hincrby:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 哈希表中的字段值加上指定增量值
     *
     * @return 返回目前增量值
     */
    public static double hincrByFloat(String key, String field, double value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hincrByFloat(key, field, value);
            }
        } catch (Exception e)
        {
            logger.error("hincrby:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 哈希表中的字段值加上指定增量值
     *
     * @return 返回目前增量值
     */
    public static Long incr(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.incr(key);
            }
        } catch (Exception e)
        {
            logger.error("incr:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 获取hash对象
     */
    public static Map<String, String> hgetall(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hgetAll(key);
            }
        } catch (Exception e)
        {
            logger.error("hgetall:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 获取所有字段
     */
    public static Set<String> hkeys(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hkeys(key);
            }
        } catch (Exception e)
        {
            logger.error("hlen:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 进行获取hash长度
     */
    public static Long hlen(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hlen(key);
            }
        } catch (Exception e)
        {
            logger.error("hlen:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 获取hash对象
     */
    public static String hget(String key, String field)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hget(key, field);
            }
        } catch (Exception e)
        {
            logger.error("hget:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 删除hash对象
     */
    public static void hdel(String key, String... fields)
    {

        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.hdel(key, fields);
            }
        } catch (Exception e)
        {
            logger.error("hdel:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 设置对象数据信息
     */
    public static void hset(String key, String field, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.hset(key, field, value);
            }
        } catch (Exception e)
        {
            logger.error("hset:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 设置对象数据信息
     */
    public static long hsetnx(String key, String field, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.hsetnx(key, field, value);
            }
        } catch (Exception e)
        {
            logger.error("hsetnx:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 获取数据
     */
    public static String get(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.get(key);
            }
        } catch (Exception e)
        {
            logger.error("get:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;

    }

    /**
     * 删除key值
     */
    public static Long del(String... key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.del(key);
            }
        } catch (Exception e)
        {
            logger.error("get:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 檢測值是否存在
     */
    public static boolean exists(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.exists(key);
            }
        } catch (Exception e)
        {
            logger.error("get:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return false;
    }

    /**
     * 設置过期时间
     */
    public static long pexpire(String key, long milliseconds)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.pexpire(key, milliseconds);
            }
        } catch (Exception e)
        {
            logger.error("pexpire:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 过期时间获取
     */
    public static long pttl(String key)
    {
        System.out.println("---" + key);
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.pttl(key);
            }
        } catch (Exception e)
        {
            logger.error("set:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return -1;
    }

    /**
     * 设置对象数据
     */
    public static void set(String key, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.set(key, value);
            }
        } catch (Exception e)
        {
            logger.error("set:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 设置数据
     */
    public static void setnx(String key, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.setnx(key, value);
            }
        } catch (Exception e)
        {
            logger.error("setnx:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * redis工具类方法 比setnx多了个保存失效时间
     *
     * @param key   redis key
     * @param value 失效时间，单位毫秒
     * @return 当key不存在，保存成功并返回1，当key已存在不保存并返回0
     * @author wangdy
     */
    public static Long setnxAndExpire(final String key, String value, long milliseconds)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds == null)
            {
                return 0L;
            }
            jds.select(AVOID_REPEATABLE_COMMIT_DB);
            jds.set(key, value);
            return jds.pexpire(key, milliseconds);
        } catch (Exception e)
        {
            logger.error("setString:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0L;
    }

    /**
     * 添加集合
     */
    public static void sadd(String key, String... member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.sadd(key, member);
            }
        } catch (Exception e)
        {
            logger.error("setString:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 进行随机取出对象
     */
    public static String spop(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.spop(key);
            }
        } catch (Exception e)
        {
            logger.error("spop:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 获取集合内长度信息
     */
    public static Long scard(String key)
    {

        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.scard(key);
            }
        } catch (Exception e)
        {
            logger.error("spop:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;

    }

    /**
     * 进行随机移除多项对象
     */
    public static Set<String> spop(String key, long count)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.spop(key, count);
            }
        } catch (Exception e)
        {
            logger.error("spop:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 移除成功數據
     */
    public static long srem(String key, String... member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.srem(key, member);
            }
        } catch (Exception e)
        {
            logger.error("spop:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 指定成员的分数加上增量 increment
     */
    public static double zincrby(String key, String member, double score)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zincrby(key, score, member);
            }
        } catch (Exception e)
        {
            logger.error("setString:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     *
     */
    public static void zrem(String key, String... member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.zrem(key, member);
            }
        } catch (Exception e)
        {
            logger.error("zrem:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 获取有序集合的成员数
     */
    public static long zcard(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zcard(key);
            }
        } catch (Exception e)
        {
            logger.error("zrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 获取指定成员数
     */
    public static Set<String> zrange(String key, int start, int stop)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrange(key, start, stop);
            }
        } catch (Exception e)
        {
            logger.error("zrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 获取对象排名信息
     */
    public static Long zrank(String key, String member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrank(key, member);
            }
        } catch (Exception e)
        {
            logger.error("zrank:" + Log4j.getExceptionInfo(e));
        } finally
        {
            close(jds);
        }
        return null;

    }

    /**
     * 进行排重
     */
    public static Set<String> zrevrange(String key, int start, int stop)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrevrange(key, start, stop);
            }
        } catch (Exception e)
        {
            logger.error("zrevrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;

    }

    /**
     * 获取分数处理
     */
    public static Set<Tuple> zrevrangeWithScores(String key, int start, int end)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrevrangeWithScores(key, start, end);
            }
        } catch (Exception e)
        {
            logger.error("zrevrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;

    }

    public static Map<Long, Double> zrevrankOrScore(String key, String member)
    {
        Jedis jds = getJedis();
        try
        {
            Map<Long, Double> map = new HashMap<>();
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                long rank = jds.zrevrank(key, member);
                double score = jds.zscore(key, member);
                map.put(rank, score);
                return map;
            }
        } catch (Exception e)
        {
            logger.error("zrevrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    public static long zrevrank(String key, String member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrevrank(key, member);
            }
        } catch (Exception e)
        {
            logger.error("zrevrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return -1;
    }

    /**
     * 获取当前排名信息
     */
    public static double zscore(String key, String member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zscore(key, member);
            }
        } catch (Exception e)
        {
            logger.error("zscore:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return -1;
    }

    /**
     * 通过分数查询成员
     */
    public static Set<String> zrangeByScore(String key, int min, int max)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zrangeByScore(key, min, max);
            }
        } catch (Exception e)
        {
            logger.error("zrangeByScore:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 移除成员通过分数
     */
    public static long zremrangeByscore(String key, int min, int max)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zremrangeByScore(key, min, max);
            }
        } catch (Exception e)
        {
            logger.error("zremrangeByscore:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 移除成员通排名
     */
    public static long zremrangeByRank(String key, int start, int end)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.zremrangeByRank(key, start, end);
            }
        } catch (Exception e)
        {
            logger.error("zremrangeByRank:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 插入到列表的尾部(最右边)。
     */
    public static long rpush(String key, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.rpush(key, value);
            }
        } catch (Exception e)
        {
            logger.error("rpushhx:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 插入到列表的尾部(最右边)。
     */
    public static long lpush(String key, String value)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.lpush(key, value);
            }
        } catch (Exception e)
        {
            logger.error("rpushhx:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return 0;
    }

    /**
     * 移出并获取列表的第一个元素
     */
    public static String lpop(String key)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                return jds.lpop(key);
            }
        } catch (Exception e)
        {
            logger.error("rpushhx:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 清空所有库
     */
    public static void flushall()
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.flushAll();
            }
        } catch (Exception e)
        {
            logger.error("flushall:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 清空所有库
     */
    public static void flushdb()
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                jds.flushDB();
            }
        } catch (Exception e)
        {
            logger.error("zrevrange:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * zset 合并更新
     *
     * @param key
     * @param member
     * @param newMember
     */
    public static void zMerge(String key, String member, String newMember)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                // 获取原积分
                Double zscore = jds.zscore(key, member);

                if (zscore != null)
                {
                    jds.zrem(key, member);
                    jds.zadd(key, zscore, newMember);
                }
            }
        } catch (Exception e)
        {
            logger.error("zMerge:" + e.getMessage());
        } finally
        {
            close(jds);
        }
    }

    /**
     * 获取 排名 & 分数
     *
     * @param key
     * @param member
     * @return
     */
    public static Tuple zSetTuple(String key, String member)
    {
        Jedis jds = getJedis();
        try
        {
            if (jds != null)
            {
                jds.select(AVOID_REPEATABLE_COMMIT_DB);
                // 排名
                Long rank = jds.zrevrank(key, member);
                if (rank != null)
                {
                    Double score = jds.zscore(key, member);
                    return new Tuple(String.valueOf(rank + 1), score);
                }
            }
        } catch (Exception e)
        {
            logger.error("zMerge:" + e.getMessage());
        } finally
        {
            close(jds);
        }
        return null;
    }

    /**
     * 设置List集合
     *
     * @param key
     * @param list
     */
    public static void setList(String key, List<?> list)
    {
        Jedis jedis = jedisPool.getResource();
        try
        {
            if (list == null || list.size() == 0)
            {
                jedis.set(key.getBytes(), "".getBytes());
            } else
            {//如果list为空,则设置一个空
                byte[] bytes = key.getBytes();
                jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            jedis.close();
        }
    }

    /**
     * 获取List集合
     *
     * @param key
     * @return
     */
    public static List<?> getList(String key)
    {
        Jedis jedis = jedisPool.getResource();
        if (jedis == null || !jedis.exists(key))
        {
            return null;
        }
        byte[] data = jedis.get(key.getBytes());
        jedis.close();
        return SerializeUtil.unserializeList(data);
    }

}
