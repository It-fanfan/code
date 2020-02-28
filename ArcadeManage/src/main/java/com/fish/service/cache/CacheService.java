package com.fish.service.cache;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.UserValueMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.dao.second.model.UserInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.dao.second.model.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存查询
 */
@Service
public class CacheService
{
    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;

    @Autowired
    RoundsMapper roundsMapper;
    @Autowired
    RoundGameMapper roundGameMapper;
    @Autowired
    RoundGroupMapper roundGroupMapper;
    @Autowired
    RoundExtMapper roundExtMapper;

    @Autowired
    RoundMatchMapper roundMatchMapper;

    @Autowired
    GoodsValueMapper goodsValueMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;


    @Autowired
    RoundRecordMapper roundRecordMapper;

    @Autowired
    UserValueMapper userValueMapper;
    private Map<Integer, ArcadeGames> gamesMap = new ConcurrentHashMap<>();

    private Map<String, JSONObject> roundInfoMap = new ConcurrentHashMap<>();

    private Map<String, RoundExt> roundExtMap = new ConcurrentHashMap<>();

    private Map<String, String> userNameCache = new HashMap<>();

    private Map<Integer, GoodsValue> gameValueCache = new ConcurrentHashMap<>();

    private Map<String, WxConfig> wxConfigCache = new ConcurrentHashMap<>();

    private Map<String, UserValue> userValueCache = new ConcurrentHashMap<>();

    private Map<String, RoundRecord> roundRecordCache = new ConcurrentHashMap<>();

    //记录用户数值，是否进行更新用户信息
    private Map<String, Integer> userQueryRecord = new ConcurrentHashMap<>();

    public List<RoundRecord> getAllRoundRecord()
    {
        List<RoundRecord> roundRecords = roundRecordMapper.selectAllRank();
        roundRecords.forEach(roundRecord -> roundRecordCache.put(String.valueOf(roundRecord.getDdindex()), roundRecord));
        return roundRecords;
    }

    public GoodsValue getGoodsValue(int gid)
    {
        if (gameValueCache.containsKey(gid))
            return gameValueCache.get(gid);
        GoodsValue goodsValue = goodsValueMapper.selectByPrimaryKey(gid);
        gameValueCache.put(gid, goodsValue);
        return goodsValue;
    }



    public UserValue getUserValue(String uid)
    {
        if (userValueCache.isEmpty())
        {
            getAllUserValue();
        }
        return userValueCache.get(uid);
    }

    public List<UserValue> getAllUserValue()
    {
        List<UserValue> userValues = userValueMapper.selectAll();
        userValues.forEach(userValue -> userValueCache.put(userValue.getDduid(), userValue));
        return userValues;
    }



    public WxConfig getWxConfig(String appId)
    {
        if (wxConfigCache.isEmpty())
        {
            getAllWxConfig();
        }
        return wxConfigCache.get(appId);
    }

    public List<WxConfig> getAllWxConfig()
    {
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        wxConfigs.forEach(wxConfig -> wxConfigCache.put(wxConfig.getDdappid(), wxConfig));
        return wxConfigs;
    }

    /**
     * 通过游戏编号获取游戏参数
     *
     * @param gameCode 游戏编号
     * @return 游戏信息
     */
    public ArcadeGames getArcadeGames(int gameCode)
    {
        if (gamesMap.containsKey(gameCode))
        {
            return gamesMap.get(gameCode);
        }
        ArcadeGames games = arcadeGamesMapper.selectByPrimaryKey(gameCode);
        if (games != null)
            gamesMap.put(gameCode, games);
        return games;
    }

    /**
     * 通过游戏编号获取游戏参数
     *
     * @param gameCode 游戏编号
     * @return 游戏信息
     */
    public ArcadeGames getGames(int gameCode)
    {
        ArcadeGames games = getArcadeGames(gameCode);
        if (games != null)
        {
            return games ;
        }
        return null;
    }

    public RoundExt getRoundExt(String round)
    {
        RoundExt roundExt;
        if (roundExtMap.containsKey(round))
        {
            roundExt = roundExtMap.get(round);
        } else
        {
            roundExt = roundExtMapper.selectByddCode(round);
            roundExtMap.put(round, roundExt);
        }
        return roundExt;
    }

    /**
     * 获取赛制信息
     *
     * @param isGroup 是否群编号
     * @param ddmCode 赛制编号
     * @return 赛制信息
     */
    public JSONObject getRoundInfo(boolean isGroup, int ddmCode)
    {
        String key = MessageFormat.format("match-{0}-g{1}", isGroup, ddmCode);
        if (roundInfoMap.containsKey(key))
            return roundInfoMap.get(key);
        //System.out.println("赛场信息" + key);
        JSONObject data = new JSONObject();
        String ddRound = null;
        if (isGroup)
        {
            RoundMatch roundGroup = roundMatchMapper.selectByPrimaryKey(ddmCode);
            if (roundGroup != null)
            {
                data.put("name", roundGroup.getDdname());
                ddRound = roundGroup.getDdround();
            }
        } else
        {
            RoundGame roundGame = roundGameMapper.selectByPrimaryKey(ddmCode);
            if (roundGame != null)
            {
                data.put("name", roundGame.getDdname());
                ddRound = roundGame.getDdround();
            }

        }
        if (ddRound != null)
        {
            RoundExt roundExt = getRoundExt(ddRound);
            if (roundExt != null)
            {
                data.put("time", roundExt.getTip());
            }
        }
        roundInfoMap.put(key, data);
        return data;
    }

    public String getUserName(String uid)
    {
        return Objects.toString(userNameCache.get(uid), "未知");
    }

    /**
     * 进行加载缓存
     */
    public void synUserName(Set<String> users)
    {
        users.removeIf(uid -> userNameCache.containsKey(uid));
        if (!users.isEmpty())
        {
            StringBuilder userStr = new StringBuilder();
            for (String temp : users)
            {
                if (userStr.length() != 0)
                    userStr.append(",");
                userStr.append("\"").append(temp).append("\"");
            }
            List<UserInfo> data = userInfoMapper.selectUserInfo(userStr.toString());
            if (data != null)
            {
                for (UserInfo info : data)
                {
                    String _uid = info.getDduid();
                    String _name = info.getDdname();
                    userNameCache.put(_uid, _name);
                }
            }
        }
    }

    /**
     * 通過用戶編號,獲取用戶昵稱
     *
     * @param uid 用戶編號
     * @return 昵稱
     */
    public String getUserName(String className, Set<String> users, String uid)
    {
        //用户是否已经缓存过
        if (userQueryRecord.containsKey(className) && userQueryRecord.get(className) == users.size())
        {
            return Objects.toString(userNameCache.get(uid), "未知");
        }
        userQueryRecord.put(className, users.size());
        long current = System.currentTimeMillis();
        synUserName(users);
        System.out.println("查询用户数据,耗时:" + (System.currentTimeMillis() - current) + "ms");
        return getUserName(className, users, uid);
    }

    /**
     * 通过昵称获取用户编号
     *
     * @param nickName 昵称信息
     * @return 用户uid
     */
    public Set<String> searchUserUid(String nickName)
    {
        Set<String> set = new HashSet<>();
        List<UserInfo> users = userInfoMapper.selectByDdName(nickName);
        if (users != null)
            users.forEach(info -> set.add(info.getDduid()));
        return set;
    }
}
