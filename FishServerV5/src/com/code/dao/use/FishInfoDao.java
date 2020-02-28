package com.code.dao.use;

import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.dto.PreviewDTO;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Vector;

public class FishInfoDao
{
    private static final Logger LOG = LoggerFactory.getLogger(FishInfoDao.class);

    public static FishInfoDb instance()
    {
        return FishInfoDb.instance();
    }

    /**
     * 進行保存信息
     */
    public static <T> boolean saveOrUpdate(T v)
    {
        try
        {
            return saveOrUpdate(v, false);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }

    public static <T> boolean saveOrUpdate(T v, boolean delay) throws Exception
    {
        return instance().saveOrUpdate(v, delay) > 0;
    }

    /**
     * 进行查询数据
     *
     * @param outClass    查询class
     * @param userId      玩家信息
     * @param table       查询表
     * @param searchValue 查询节点
     * @param <T>         对象
     * @return 数组
     */
    private static <T> Vector<T> searchDate(Class<T> outClass, String table, String userId, Map<String, String> searchValue)
    {
        StringBuilder SQL = new StringBuilder(String.format("select * from %s where userId=%s", table.toLowerCase(), userId));
        if (searchValue != null)
        {
            searchValue.forEach((k, v) ->
            {
                SQL.append(" and ");
                SQL.append(k);
                SQL.append("=");
                SQL.append(String.format("'%s'", v));
            });
        }
        Vector<T> bySQL = instance().findBySQL(SQL.toString(), outClass);
        if (bySQL.isEmpty())
        {
            return null;
        }
        return bySQL;
    }


    /**
     * 查询消息 userid
     */
    public static <T> Vector<T> searchDate(Class<T> outClass, String userId, Map<String, String> searchValue)
    {
        String table = instance().getTableName(outClass);
        return searchDate(outClass, table, userId, searchValue);
    }

    /**
     * 查询消息 userid
     */
    public static <T> Vector<T> searchDate(Class<T> outClass, String userId)
    {
        String table = instance().getTableName(outClass);
        return searchDate(outClass, table, userId);
    }

    /**
     * 查询消息 userid
     */
    private static <T> Vector<T> searchDate(Class<T> outClass, String table, String userid)
    {
        return searchDate(outClass, table, userid, null);
    }

    /**
     * 随机抽取偷贝壳用户
     *
     * @param i 筛选个数
     * @return 返回随机用户
     */
    public static Vector<PreviewDTO> getFillBook(String userId, String format, int i)
    {
        String SQL = String.format("SELECT * FROM user_preview where fillBook %s 0 and userId != %s ORDER BY RAND() LIMIT %s", format, userId, i);
        return instance().findBySQL(SQL, PreviewDTO.class);
    }


    /**
     * 创建猜图鉴奖励用户
     *
     * @param limit 抽取人数
     * @return 返回 随机图鉴和用户
     */
    public static Long getRandomGuessBook(int basinId, Vector<Integer> ftId, String userId, int limit)
    {
        StringBuilder builder = new StringBuilder("select userId from user_preview where");
        if (ftId.isEmpty())
        {
            return null;
        }
        builder.append(" (");
        for (Integer book : ftId)
        {
            builder.append(" fish").append(book).append("=1 or");
        }
        builder.delete(builder.length() - 3, builder.length());
        builder.append(") and userId!=").append(userId);
        builder.append(" and basinId=").append(basinId);
        builder.append(" and fillBook>0 ORDER BY RAND() LIMIT ").append(limit);
        System.out.println(builder.toString());
        Vector<PreviewDTO> bySQL = instance().findBySQL(builder.toString(), PreviewDTO.class);
        if (bySQL == null || bySQL.isEmpty())
        {
            return null;
        } else
        {
            return bySQL.firstElement().userId;
        }
    }
}
