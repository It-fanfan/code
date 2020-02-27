package com.fish.utils;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.GameRoundMapper;
import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.mapper.RoundsMapper;
import com.fish.dao.primary.model.Ranking;
import com.fish.dao.primary.model.Rounds;
import com.fish.dao.second.mapper.GroupMatchMapper;
import com.fish.dao.second.model.GroupMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Component
public class RedisData
{
    //    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RoundsMapper roundsMapper;
    @Autowired
    RankingMapper rankingMapper;
    @Autowired
    GroupMatchMapper groupMatchMapper;
    @Autowired
    GameRoundMapper gameRoundMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //TODO:隔天整理未处理数据

    static String getStartSuffix()
    {
        return "rankingGroup";
    }

    //群比赛 头信息
    static String getGroupStartSuffix()
    {
        return "rankingGroup";
    }

    //常規賽
    public static String getDayStartSuffix()
    {
        return "ranking-";
    }

    /**
     * 查找所有用户信息
     */

    public void searchAllUser()
    {
        //群比赛排行榜
        searchGroupRanking();
        //常规赛处理
        // searchRanking();
    }

    public void searchRanking()
    {
        Set<String> keys = redisUtil.keys(getDayStartSuffix() + "*");
        for (String key : keys)
        {
            if (!key.equals("ranking-user-collect"))
            {
                System.out.println("排行榜:" + key);
                //解析排行榜key,获取matchId,等相关数据(包括游戏编号，赛场编号，赛场时间，赛场索引)
                //TODO:进行判断赛场是否过期，过期才处理
                DayMatchKey matchKey = DayMatchKey.parse(key);
                //后续处理逻辑
                //TODO:过期时间通过matchKey.day&index 和 赛场`persie_deamon`.`rounds`.ddHour[matchKey.index]来进行判断

                int gameCode = matchKey.gameCode;
                String matchCode = matchKey.matchCode;
                int day = matchKey.day;
                int gameIndex = matchKey.index;

                LocalDate nows = LocalDate.now();
                LocalDate localDates = nows.minusDays(1);
                LocalDate ThreeDates = nows.minusDays(2);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String localDate = formatter.format(localDates);
                String ThreeDate = formatter.format(ThreeDates);
                //&& day >= Integer.valueOf(ThreeDate)
                if (day <= Integer.parseInt(localDate))
                {
                    Integer ddhour = 0;
                    Rounds rounds = roundsMapper.selectByDdCodeS(matchCode);
                    switch (gameIndex)
                    {
                        case 0:
                            ddhour = rounds.getDdhour0();
                            break;
                        case 1:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1();
                            break;
                        case 2:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2();
                            break;
                        case 3:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2() + rounds.getDdhour3();
                            break;
                        case 4:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2() + rounds.getDdhour3() + rounds.getDdhour4();
                            break;
                        case 5:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2() + rounds.getDdhour3() + rounds.getDdhour4() + rounds.getDdhour5();
                            break;
                        case 6:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2() + rounds.getDdhour3() + rounds.getDdhour4() + rounds.getDdhour5() + rounds.getDdhour6();
                            break;
                        case 7:
                            ddhour = rounds.getDdhour0() + rounds.getDdhour1() + rounds.getDdhour2() + rounds.getDdhour3() + rounds.getDdhour4() + rounds.getDdhour5() + rounds.getDdhour6() + rounds.getDdhour7();
                            break;
                    }
                    if (ddhour <= 24)
                    {
                        //TODO:通过matchKey获取ranking相关需要数据缓存信息，包括不限于游戏表，产品表，赛场表，赛制表
                        Set element = redisUtil.zrevrangeWithScores(key, 0, -1);
                        int ranking = 0;
                        ArrayList<Ranking> rankings = new ArrayList<>();
                        for (Object data : element)
                        {
                            Ranking ranks = new Ranking();
                            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
                            //分数score ,UID:value
                            System.out.println("数据:" + jsonObject);
                            //排名
                            int index = ++ranking;
                            //TODO:奖金，通过赛场信息 和 排名进行获取
                            //
                            Rounds round = roundsMapper.selectByDdCodeS(matchCode);
                            String award = "";
                            String userAward = "";
                            switch (gameIndex)
                            {
                                case 0:
                                    award = round.getDdaward0512a();
                                    break;
                                case 1:
                                    award = round.getDdaward1512a();
                                    break;
                                case 2:
                                    award = round.getDdaward2512a();
                                    break;
                                case 3:
                                    award = round.getDdaward3512a();
                                    break;
                                case 4:
                                    award = round.getDdaward4512a();
                                    break;
                                case 5:
                                    award = round.getDdaward5512a();
                                    break;
                                case 6:
                                    award = round.getDdaward6512a();
                                    break;
                                case 7:
                                    award = round.getDdaward7512a();
                                    break;
                            }
                            String[] split = award.split("#");
                            if (index == 1)
                            {
                                userAward = split[0];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("rmb");
                            } else if (index == 2)
                            {
                                userAward = split[1];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("rmb");
                            } else if (index == 3)
                            {
                                userAward = split[2];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("rmb");
                            } else if (index > 3 && index <= 9)
                            {
                                userAward = split[3];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("coin");
                            } else if (index > 9 && index <= 39)
                            {
                                userAward = split[4];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("coin");
                            } else if (index > 39 && index <= 100)
                            {
                                userAward = split[5];
                                ranks.setAwardtotal(Integer.valueOf(userAward));
                                ranks.setAwardtype("coin");
                            }
                            ranks.setRanking((long) index);
                            ranks.setMatchid(matchCode);
                            ranks.setUid(jsonObject.getString("value"));
                            ranks.setMark(new BigDecimal(jsonObject.getString("score")).longValue());
                            ranks.setMatchindex(gameIndex);

//                            Date now = new Date();
//                            LocalDate currDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                            currDate = currDate.minusDays(1);
//                            Date beforeDate = java.sql.Date.valueOf(currDate);
                            String gameDay = day + "";
                            DateFormat format = new SimpleDateFormat("yyyyMMdd");
                            Date date = null;
                            try
                            {
                                date = format.parse(gameDay);
                            } catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                            ranks.setMatchdate(date);
                            ranks.setGamecode(gameCode);
                            ranks.setDdgroup(false);

                            ranks.setInserttime(new Timestamp(new Date().getTime()));
                            rankings.add(ranks);
                            //TODO:***批量***操作保存到数据库ranking内
                        }
                        //rankingMapper.insertBatch(rankings);
                    }
                }

            }
        }
    }

    private void searchGroupRanking()
    {

        RedisUtil redisUtil = new RedisUtil(redisTemplate);
        Set<String> keys = redisUtil.keys(getStartSuffix() + "*");
        if (keys != null && keys.size() > 0)
        {
            for (String key : keys)
            {
                System.out.println("排行榜:" + key);
                //解析排行榜key,获取matchId,等相关数据(包括游戏编号，赛场编号，赛场时间，赛场索引)
                //TODO:进行判断赛场是否过期，过期才处理
                GroupMatchKey matchKey = GroupMatchKey.parse(key);

                //后续处理逻辑
                int gameCode = matchKey.gameCode;
                String groupCode = matchKey.group;
                //TODO:群编号 查找 数据表:`persie`.`group_match`(主键)
                GroupMatch groupMatch = groupMatchMapper.selectByPrimaryKey(groupCode);
                if (groupMatch != null)
                {
                    DateFormat dateFmt = new SimpleDateFormat("yyyyMMddHH");//格式化一下时间
                    Date dNow = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dNow);
                    String currentTime = dateFmt.format(calendar.getTime());
                    int currentDate = Integer.parseInt(currentTime);
                    Integer ddend = groupMatch.getDdend();
                    if (ddend <= currentDate)
                    {
                        ArrayList<Ranking> rankings = new ArrayList<>();
                        //TODO:通过matchKey获取ranking相关需要数据缓存信息，包括不限于游戏表，产品表，赛场表，赛制表
                        Set element = redisUtil.zrevrangeWithScores(key, 0, -1);
                        int ranking = 0;
                        for (Object data : element)
                        {
                            Ranking rank = new Ranking();
                            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
                            //分数score ,UID:value
                            System.out.println("数据:" + jsonObject);
                            //排名
                            int index = ++ranking;
                            //TODO:奖金，通过赛场信息 和 排名进行获取
                            Rounds round = roundsMapper.selectByDdCodeQ(groupMatch.getDdmatch());
                            String userAward = "";
                            String award = round.getDdaward0512a();
                            String[] split = award.split("#");
                            if (index == 1)
                            {
                                userAward = split[0];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("rmb");
                            } else if (index == 2)
                            {
                                userAward = split[1];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("rmb");
                            } else if (index == 3)
                            {
                                userAward = split[2];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("rmb");
                            } else if (index > 3 && index <= 9)
                            {
                                userAward = split[3];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("coin");
                            } else if (index > 9 && index <= 39)
                            {
                                userAward = split[4];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("coin");
                            } else if (index > 39 && index <= 100)
                            {
                                userAward = split[5];
                                rank.setAwardtotal(Integer.valueOf(userAward));
                                rank.setAwardtype("coin");
                            }
                            rank.setMatchid(groupMatch.getDdmatch());
                            rank.setRanking(Long.valueOf(index));
                            rank.setUid(jsonObject.getString("value"));
                            rank.setMark(new BigDecimal(jsonObject.getString("score")).longValue());
                            rank.setMatchindex(0);
                            rank.setGamecode(gameCode);
                            String endTime = ddend + "";
                            String gameTime = endTime.substring(0, endTime.length() - 2);
                            rank.setDdgroup(true);
                            //查询数据是否重复
                            Ranking reRanking = rankingMapper.selectIsSameData(groupMatch.getDdmatch(), jsonObject.getString("value"), Long.valueOf(index), new BigDecimal(jsonObject.getString("score")).longValue(), 0, gameCode, endTime);
                            if (reRanking == null)
                            {
//                        Date now = new Date();
//                        LocalDate localDate=now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                        localDate = localDate.minusDays(1);
//                        Date beforeDate=java.sql.Date.valueOf(localDate);
                                Date parse = null;
                                DateFormat rankDateFmt = new SimpleDateFormat("yyyyMMdd");//格式化一下时间
                                try
                                {
                                    parse = rankDateFmt.parse(gameTime);
                                } catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                rank.setMatchdate(parse);
                                rank.setInserttime(new Timestamp(new Date().getTime()));

                                rankings.add(rank);
                            }
                        }
                        //TODO:***批量***操作保存到数据库ranking内

                        // rankingMapper.insertBatch(rankings);
                    }
                }

            }
        }
    }

    public static class MatchKey
    {
        //游戏编号
        private int gameCode;
        //赛场区域
        private String group;

        private MatchKey()
        {

        }

        MatchKey(int gameCode, String group)
        {
            this.gameCode = gameCode;
            this.group = group;
        }

        /**
         * 解析赛区编号信息
         *
         * @param key 赛区key
         * @return 赛区节点
         */
        static MatchKey parse(String key)
        {
            String[] keys = key.split("-");
            if (keys.length < 2)
                return null;
            MatchKey matchKey = new MatchKey();
            //游戏编号
            String start = keys[0];
            matchKey.gameCode = Integer.valueOf(start.substring(start.length() - 4));
            //某天比赛
            matchKey.group = keys[1];
            return matchKey;
        }

        public String getKey()
        {
            return getStartSuffix() + gameCode + "-" + group;
        }

        String getCollectKey()
        {
            return "user-" + getStartSuffix() + gameCode + "-" + group;
        }

        public int getGameCode()
        {
            return gameCode;
        }

        String getGroup()
        {
            return group;
        }

        public void setGroup(String group)
        {
            this.group = group;
        }
    }

    public static class GroupMatchKey
    {
        //游戏编号
        private int gameCode;
        //赛场区域
        private String group;

        private GroupMatchKey()
        {

        }

        GroupMatchKey(int gameCode, String group)
        {
            this.gameCode = gameCode;
            this.group = group;
        }

        /**
         * 解析赛区编号信息
         *
         * @param key 赛区key
         * @return 赛区节点
         */
        static GroupMatchKey parse(String key)
        {
            String[] keys = key.split("-");
            if (keys.length < 2)
                return null;
            GroupMatchKey matchKey = new GroupMatchKey();
            //游戏编号
            String start = keys[0];
            matchKey.gameCode = Integer.valueOf(start.substring(start.length() - 4));
            //赛场区域
            matchKey.group = keys[1];
            return matchKey;
        }

        public String getKey()
        {
            return getGroupStartSuffix() + gameCode + "-" + group;
        }

        String getCollectKey()
        {
            return "user-" + getGroupStartSuffix() + gameCode + "-" + group;
        }

        public int getGameCode()
        {
            return gameCode;
        }

        String getGroup()
        {
            return group;
        }

        public void setGroup(String group)
        {
            this.group = group;
        }
    }

    //常規賽
    public static class DayMatchKey
    {
        //游戏编号
        private int gameCode;
        //赛区编号
        private String matchCode;
        //赛场时间
        private int day;
        //赛场区域
        private int index;

        private DayMatchKey()
        {

        }

        public DayMatchKey(int gameCode, String matchCode, int day, int index)
        {
            this.gameCode = gameCode;
            this.matchCode = matchCode;
            this.day = day;
            this.index = index;
        }

        /**
         * 解析赛区编号信息
         *
         * @param key 赛区key
         * @return 赛区节点
         */
        public static DayMatchKey parse(String key)
        {
            String[] keys = key.split("-");
            if (keys.length < 5)
                return null;
            DayMatchKey matchKey = new DayMatchKey();
            //游戏编号
            matchKey.gameCode = Integer.parseInt(keys[1]);
            //赛区编号
            matchKey.matchCode = keys[2];
            //某天比赛
            matchKey.day = Integer.parseInt(keys[3]);
            //赛区索引
            matchKey.index = Integer.parseInt(keys[4]);
            return matchKey;
        }

        public String getKey()
        {
            return getDayStartSuffix() + gameCode + "-" + matchCode + "-" + day + "-" + index;
        }

        public String getCollectKey()
        {
            return "usr-" + getDayStartSuffix() + gameCode + "-" + matchCode + "-" + day + "-" + index;
        }

        public int getGameCode()
        {
            return gameCode;
        }

        public String getMatchCode()
        {
            return matchCode;
        }

        public int getDay()
        {
            return day;
        }

        public int getIndex()
        {
            return index;
        }
    }
}
