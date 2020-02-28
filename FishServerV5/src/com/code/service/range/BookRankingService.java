package com.code.service.range;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.fish.userinfo.UserInfo;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.ranking.RankingGlobalProtocol;
import com.code.service.book.BookInitService;
import com.utils.db.RedisUtils;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 开启图鉴排行版信息
 */
public class BookRankingService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BookRankingService.class);
    private static BookRankingService instance = null;

    private BookRankingService()
    {

    }

    public static BookRankingService getInstance()
    {
        if (instance == null)
            instance = new BookRankingService();
        return instance;
    }

    /**
     * 进行添加元素组合
     */
    public Double zincrby(String member, int increment)
    {
        String key = globalRankingKey();
        return RedisUtils.zincrby(key, member, increment);
    }

    /**
     * 获取用户图鉴和排行
     *
     * @param userCache 用户缓存
     * @return 排行信息
     */
    public Tuple getUserRankingAndBooks(UserCache userCache)
    {
        String key = globalRankingKey();
        return RedisUtils.zSetTuple(key, userCache.getUserId());
    }

    /**
     * 获取自己排行记录
     *
     * @param userCache 用户信息
     * @return 排行元素
     */
    public RankingGlobalProtocol.RankingUser getMyRanking(UserCache userCache)
    {
        RankingGlobalProtocol.RankingUser rankingUser = new RankingGlobalProtocol.RankingUser();
        rankingUser.nickname = userCache.getNickName();
        rankingUser.icon = userCache.getIcon();
        rankingUser.userid = userCache.getUserId();
        Tuple tuple = getUserRankingAndBooks(userCache);
        if (tuple != null)
        {
            rankingUser.rank = Integer.valueOf(tuple.getElement());
            rankingUser.booknum = (int) tuple.getScore();
        }
        return rankingUser;
    }

    /**
     * 查询全球榜前100名
     */
    public Vector<RankingGlobalProtocol.RankingUser> globalRanking()
    {
        Vector<RankingGlobalProtocol.RankingUser> globals = new Vector<>();
        try
        {
            // 排行限制
            int globalLimit = Systemstatusinfo.getInt("global_limit", "100");
            // 排行榜
            Set<Tuple> scores = RedisUtils.zrevrangeWithScores(globalRankingKey(), 0, globalLimit);
            if (scores == null)
                return globals;
            String[] field = new String[scores.size()];
            AtomicInteger atomic = new AtomicInteger();
            scores.forEach(tuple ->
            {
                String key = tuple.getElement();
                int index = atomic.getAndIncrement();
                field[index] = key;
                RankingGlobalProtocol.RankingUser element = new RankingGlobalProtocol.RankingUser();
                element.userid = key;
                element.booknum = (int) tuple.getScore();
                element.rank = index + 1;
                globals.add(element);
            });
            //获取用户详情
            List<String> infos = UserCache.getUserCollects(field);
            if (infos != null)
            {
                for (int i = 0; i < infos.size(); i++)
                {
                    JSONObject jsonObject = JSONObject.parseObject(infos.get(i));
                    RankingGlobalProtocol.RankingUser element = globals.elementAt(i);
                    if (element != null && jsonObject != null)
                    {
                        element.icon = jsonObject.getString("icon");
                        element.nickname = jsonObject.getString("nickname");
                    }
                }
            }
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return globals;
    }

    /**
     * 重置排行数据
     */
    public static int resetRanking()
    {
        //先进行移除缓存
        String key = globalRankingKey();
        RedisUtils.del(key);
        //查询用户数据
        String SQL = "select * from user_info";
        Vector<UserInfo> list = FishInfoDao.instance().findBySQL(SQL, UserInfo.class);
        list.forEach(userInfo ->
        {
            UserCache.setRankingUser(userInfo.getUserId(), userInfo.getIcon(), userInfo.getNickName());
            UserCache userCache = UserCache.getUserCache(userInfo.getUserId());
            if (userCache == null)
                return;
            BookInitService service = new BookInitService(userCache);
            int light = service.getUserBook(BookInitService.BookType.open).size();
            int stolen = service.getUserBook(BookInitService.BookType.stolen).size();
            int book = light + stolen;
            BookRankingService.getInstance().zincrby(userCache.getUserId(), book);
            JSONObject hash = new JSONObject();
            hash.put("book", book);
            userCache.updateUserData(hash);
        });
        return list.size();
    }

    /**
     * 获取全球排名key
     */
    private static String globalRankingKey()
    {
        return "global-ranking-ext";
    }
}
