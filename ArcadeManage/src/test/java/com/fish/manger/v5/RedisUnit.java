package com.fish.manger.v5;

import com.alibaba.fastjson.JSONObject;
import com.fish.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class RedisUnit
{
    @Resource
    private RedisUtil redisUtil;
    //TODO:隔天整理前一天数据
    /**
     * 查找所有用户信息
     */
    public void searchAllUser()
    {
        //群比赛排行榜
        {
            Set<String> keys = redisUtil.keys(getGroupStartSuffix() + "*");
            for (String key : keys)
            {
                System.out.println("排行榜:" + key);
                //解析排行榜key,获取matchId,等相关数据(包括游戏编号，赛场编号，赛场时间，赛场索引)
                //TODO:进行判断赛场是否过期，过期才处理
                GroupMatchKey matchKey = GroupMatchKey.parse(key);
                //后续处理逻辑
                //TODO:群编号 查找 数据表:`persie`.`group_match`(主键)
                //TODO:通过matchKey获取ranking相关需要数据缓存信息，包括不限于游戏表，产品表，赛场表，赛制表
                Set element = redisUtil.zrevrangeWithScores(key, 0, -1);
                int ranking = 0;
                for (Object data : element)
                {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
                    //分数score ,UID:value
                    System.out.println("数据:" + jsonObject);
                    //排名
                    int index = ++ranking;
                    //TODO:奖金，通过赛场信息 和 排名进行获取
                    //
                    //TODO:***批量***操作保存到数据库ranking内
                }
            }
        }

        //常規賽處理
        {
            Set<String> keys = redisUtil.keys(getDayStartSuffix() + "*");
            for (String key : keys)
            {
                System.out.println("排行榜:" + key);
                //解析排行榜key,获取matchId,等相关数据(包括游戏编号，赛场编号，赛场时间，赛场索引)
                //TODO:进行判断赛场是否过期，过期才处理
                DayMatchKey matchKey = DayMatchKey.parse(key);
                //后续处理逻辑
                //TODO:过期时间通过matchKey.day&index 和 赛场`persie_deamon`.`rounds`.ddHour[matchKey.index]来进行判断
                //TODO:通过matchKey获取ranking相关需要数据缓存信息，包括不限于游戏表，产品表，赛场表，赛制表
                Set element = redisUtil.zrevrangeWithScores(key, 0, -1);
                int ranking = 0;
                for (Object data : element)
                {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
                    //分数score ,UID:value
                    System.out.println("数据:" + jsonObject);
                    //排名
                    int index = ++ranking;
                    //TODO:奖金，通过赛场信息 和 排名进行获取
                    //
                    //TODO:***批量***操作保存到数据库ranking内
                }
            }
        }
    }


    static String getStartSuffix()
    {
        return "rankingGroup";
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

        public String getKey()
        {
            return getStartSuffix() + gameCode + "-" + group;
        }

        String getCollectKey()
        {
            return "user-" + getStartSuffix() + gameCode + "-" + group;
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


    //群比赛 头信息
    static String getGroupStartSuffix()
    {
        return "rankingGroup";
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

        public String getKey()
        {
            return getGroupStartSuffix() + gameCode + "-" + group;
        }

        String getCollectKey()
        {
            return "user-" + getGroupStartSuffix() + gameCode + "-" + group;
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
            //某天比赛
            matchKey.group = keys[1];
            return matchKey;
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
    public static String getDayStartSuffix()
    {
        return "ranking-";
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

        public String getKey()
        {
            return getDayStartSuffix() + gameCode + "-" + matchCode + "-" + day + "-" + index;
        }

        public String getCollectKey()
        {
            return "usr-" + getDayStartSuffix() + gameCode + "-" + matchCode + "-" + day + "-" + index;
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
            matchKey.gameCode = Integer.valueOf(keys[1]);
            //赛区编号
            matchKey.matchCode = keys[2];
            //某天比赛
            matchKey.day = Integer.valueOf(keys[3]);
            //赛区索引
            matchKey.index = Integer.valueOf(keys[4]);
            return matchKey;
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
