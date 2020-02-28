package com.code.service.book;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.userinfo.UserPreview;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BasePro.Basin;
import com.code.protocols.basic.BasePro.Book;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.achievement.AchievementType;
import com.code.service.achievement.AchievementService;
import com.code.service.friend.FriendGuardService;
import com.code.service.range.BookRankingService;
import com.code.service.work.WorkService;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.code.protocols.core.AnglingBase.ERROR;
import static com.code.protocols.core.AnglingBase.MaterialResult;

public class BookInitService
{
    private static Logger LOG = LoggerFactory.getLogger(BookInitService.class);
    private UserCache userCache;

    public BookInitService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 获取用户图鉴信息
     *
     * @return 用户图鉴信息
     */
    private static Map<Integer, Integer> getUserBookData(UserCache userCache)
    {
        String field = getUserBookRedis();
        String userJson = userCache.getValue(field);
        Map<Integer, Integer> cache = null;
        if (userJson != null)
        {
            LOG.debug("获取用户图鉴信息！！！" + userJson);
            cache = XwhTool.parseJSONByFastJSON(userJson, new TypeToken<Map<Integer, Integer>>()
            {
            }.getType());
        }
        if (cache == null)
        {
            UserPreview instance = FishInfoDao.instance().findById(UserPreview.class, new String[]{"userId", userCache.getUserId()});
            if (instance == null)
            {
                instance = new UserPreview();
                try
                {
                    instance.setUserId(userCache.userId());
                    instance.setBasinId(userCache.getBasin());
                    instance.setFillBook(userCache.getFillBook());
                    FishInfoDb.instance().saveOrUpdate(instance, true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                cache = new LinkedHashMap<>();
                Field[] fields = UserPreview.class.getDeclaredFields();
                for (Field value : fields)
                {
                    if (value.getName().startsWith("fish"))
                    {
                        value.setAccessible(true);
                        int key = Integer.valueOf(value.getName().replace("fish", ""));
                        cache.put(key, value.getInt(instance));
                    }
                }
                userJson = XwhTool.getJSONByFastJSON(cache);
                userCache.hSet(field, userJson);
            } catch (Exception e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
        }
        return cache;
    }

    /**
     * 获取用户水域等级对应的图鉴
     *
     * @param data    全部配置
     * @param basinId 用户水域等级
     * @return 用户当前水域图鉴
     */
    private static Vector<Integer> getConfigByBasinBooks(Vector<ConfigFish> data, int basinId)
    {
        Vector<Integer> list = new Vector<>();
        if (data != null)
        {
            list = data.stream().filter(en -> basinId == en.getBasin()).map(ConfigFish::getFtId).collect(Collectors.toCollection(Vector::new));
        }
        return list;
    }

    /**
     * 获取当前等级系统图鉴列表
     *
     * @return 返回当前等级系统列表
     */
    private static Vector<Integer> getLevelSystemBook(int basinId)
    {
        Vector<ConfigFish> basin = getSystemBook();
        return getConfigByBasinBooks(basin, basinId);
    }

    /**
     * 获取系统鱼信息
     *
     * @return 鱼信息
     */
    private static Vector<ConfigFish> getSystemBook()
    {
        return FishInfoDb.instance().getCacheListByClass(ConfigFish.class);
    }

    /**
     * 获取下一个图鉴数据
     *
     * @param lights 点亮图鉴数据
     * @return 图鉴信息
     */
    public static ConfigFish getNextBook(Vector<Integer> lights)
    {
        Vector<ConfigFish> surplus = new Vector<>();
        getSystemBook().forEach(configFish ->
        {
            if (!lights.contains(configFish.getFtId()))
                surplus.add(configFish);
        });
        if (surplus.isEmpty())
            return null;
        if (surplus.size() > 1)
        {
            surplus.sort(Comparator.comparingInt(ConfigFish::getBasin).thenComparingInt(ConfigFish::getLightExpend));
        }
        return surplus.firstElement();
    }

    /**
     * 用户图鉴节点
     *
     * @return redis key
     */
    private static String getUserBookRedis()
    {
        return "book-data-ext";
    }

    /**
     * 获取当前图鉴信息
     *
     * @return 用户图鉴信息
     */
    private Map<Integer, Integer> getUserBookData()
    {
        return getUserBookData(userCache);
    }

    /**
     * 获取图鉴状态
     *
     * @param bookId 图鉴编号
     * @return 图鉴状态
     */
    public int getBookStatus(int bookId)
    {
        Map<Integer, Integer> userPreview = getUserBookData();
        try
        {
            if (userPreview.containsKey(bookId))
                return userPreview.get(bookId);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return -2;
    }

    /**
     * 获取玩家[好友守护,当前水域点亮图鉴,被偷过图鉴]
     *
     * @return 返回用户图鉴信息
     */
    public Basin getUserBasin()
    {
        Basin basin = new Basin();
        Book book = new Book();
        book.gardians = FriendGuardService.getBookGuard(userCache);
        book.lightens = getThisLevelBook(BookType.open);
        book.stolen = getThisLevelBook(BookType.stolen);
        basin.basinid = userCache.getBasin();
        basin.book = book;
        //材料数据
        Vector<MaterialResult> material = new Vector<>();
        userCache.getMaterial().forEach((k, v) ->
        {
            MaterialResult result = new MaterialResult();
            result.count = v;
            result.id = k;
            material.add(result);
        });
        //图鉴，获取玩家材料数量
        basin.material = BigData.getBigData(material, MaterialResult.class);
        return basin;
    }

    /**
     * 进行升级水域
     */
    public ERROR openBasin(ConfigBasin config)
    {
        try
        {
            BookInitService bookService = new BookInitService(userCache);
            Vector<Integer> thisLevelBook = bookService.getUserBook(BookType.open);
            Vector<Integer> thisLevelSystemBook = bookService.getThisLevelSystemBook();
            if (!thisLevelBook.containsAll(thisLevelSystemBook))
            {
                return ERROR.NO_BASIN;
            }
            //进行获取材料信息
            if (config.getMaterials() != null)
            {
                System.out.println(config.getMaterials());
                Vector<MaterialResult> results = XwhTool.parseJSONByFastJSON(config.getMaterials(), new TypeToken<Vector<MaterialResult>>()
                {
                }.getType());
                Map<Integer, Integer> materials = userCache.getMaterial();
                if (results != null)
                {
                    for (MaterialResult result : results)
                    {
                        if (result.count <= 0)
                            continue;
                        Integer own = materials.get(result.id);
                        if (own == null || result.count > own)
                            return ERROR.LACK_MATERIAL;
                    }
                    //材料更新
                    results.forEach(materialResult ->
                    {
                        materialResult.count = -materialResult.count;
                        userCache.updateMaterial(materialResult);
                    });
                }
            }
            //进行开启新水域，消耗贝壳
            if (userCache.incrShell(-config.getOpenCost()))
                return ERROR.SUCCESS;
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return ERROR.ERROR;

    }

    /**
     * 获取玩家拥有图鉴数据
     *
     * @param type 对应类型
     * @return 图鉴信息
     */
    public Vector<Integer> getUserBook(BookType type)
    {
        Vector<Integer> books = new Vector<>();
        try
        {
            getUserBookData().forEach((k, v) ->
            {
                if (v == type.getStatus())
                {
                    books.add(k);
                }
            });
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return books;
    }

    /**
     * 获取用户指定水域，对应类型图鉴信息
     *
     * @param basin 水域
     * @param type  指定类型
     */
    public Vector<Integer> getUserBook(int basin, BookType type)
    {
        Vector<Integer> books = getUserBook(type);
        Vector<Integer> systemBooks = getLevelSystemBook(basin);
        return systemBooks.stream().filter(books::contains).collect(Collectors.toCollection(Vector::new));
    }

    /**
     * 获取用户当前水域图鉴  已开/被偷
     *
     * @param type 1:已开 0:被偷
     * @return 图鉴结果
     */
    public Vector<Integer> getThisLevelBook(BookType type)
    {
        return getThisLevelBook(type, true);
    }

    /**
     * 获取用户当前水域图鉴  已开/被偷
     *
     * @param type 1:已开 0:被偷
     * @return 图鉴结果
     */
    public Vector<Integer> getThisLevelBook(BookType type, boolean contains)
    {
        Vector<Integer> userRank = getThisLevelSystemBook();
        Vector<Integer> books = getUserBook(type);
        Vector<Integer> result = new Vector<>();
        for (Integer book : userRank)
            if (contains)
            {
                if (books.contains(book))
                    result.add(book);
            } else
            {
                if (books.contains(book))
                {
                    continue;
                }
                result.add(book);
            }
        return result;
    }

    /**
     * 获取当前等级系统图鉴列表
     *
     * @return 返回当前等级系统列表
     */
    public Vector<Integer> getThisLevelSystemBook()
    {
        int basinId = userCache.getBasin();
        return getLevelSystemBook(basinId);
    }

    /**
     * 获取未开水域等级图鉴数
     *
     * @return 未开水域图鉴s
     */
    public Vector<Integer> getOtherWaterLevelBook(int waterLevel)
    {
        Vector<ConfigFish> basins = getSystemBook();
        return basins.stream().filter(configFish -> configFish.getBasin() > waterLevel).map(ConfigFish::getFtId).collect(Collectors.toCollection(Vector::new));
    }

    /**
     * 更新图鉴
     *
     * @param bookId 图鉴id
     */
    public void updateUserBook(int bookId, BookType type)
    {
        Map<Integer, Integer> userAllBook = getUserBookData();
        //获取当前历史记录
        int history = getBookStatus(bookId);
        try
        {
            userAllBook.put(bookId, type.getStatus());
            String json = XwhTool.getJSONByFastJSON(userAllBook);
            userCache.hSet(getUserBookRedis(), json);
            int fillBook = userCache.subFillBook(type.getStatus());
            String SQL = String.format("update user_preview set fillBook=%d,fish%d=%d where userId=%s", fillBook, bookId, type.getStatus(), userCache.getUserId());
            FishInfoDb.instance().addQueue(SQL);
            LOG.debug("用户开图鉴更新完成！类型" + type.name() + "  " + json);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            //只有首次开启图鉴，才进行更新数值
            if (type == BookType.open && history == 0)
            {
                //进度更新
                try (WorkService workService = new WorkService(userCache))
                {
                    workService.addProcess(OperatorBase.ActivityType.openBook, 1);
                }
                //成就
                new AchievementService(userCache).addAchievement(AchievementType.Book, 1);
                // 排行榜排名
                Double book = BookRankingService.getInstance().zincrby(userCache.getUserId(), 1);
                JSONObject hash = new JSONObject();
                hash.put("book", book.intValue());
                userCache.updateUserData(hash);
            }
        }
    }


    /**
     * 图鉴状态
     */
    public enum BookType
    {
        stolen(-1),//被偷
        open(1),//点亮
        nothing(0);//未拥有
        private int status;

        BookType(int status)
        {
            this.status = status;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }}

}
