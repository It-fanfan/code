package com.fish.service.cache;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.UserValueMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.*;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存查询
 */
@Service
public class CacheService {
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
    MinitjWxMapper minitjWxMapper;
    @Autowired
    RoundRecordMapper roundRecordMapper;
    @Autowired
    UserValueMapper userValueMapper;

    private Map<Integer, ArcadeGames> gamesMap = new ConcurrentHashMap<>();

    private Map<String, JSONObject> roundInfoMap = new ConcurrentHashMap<>();

    private Map<String, RoundExt> roundExtMap = new ConcurrentHashMap<>();

    private Map<String, String> userNameCache = new ConcurrentHashMap<>();

    private Map<Integer, GoodsValue> gameValueCache = new ConcurrentHashMap<>();

    private Map<String, WxConfig> wxConfigCache = new ConcurrentHashMap<>();

    private Map<String, UserValue> userValueCache = new ConcurrentHashMap<>();

    private Map<String, RoundRecord> roundRecordCache = new ConcurrentHashMap<>();

    //记录用户数值，是否进行更新用户信息
    private Map<String, Integer> userQueryRecord = new ConcurrentHashMap<>();

    private Map<String, UserAllInfo> userInfoCache = new ConcurrentHashMap<>();

    /**
     * 从数据库更新全部游戏参数
     */
    public void updateAllArcadeGames() {
        if (this.gamesMap.isEmpty()) {
            List<ArcadeGames> list = this.arcadeGamesMapper.selectAll();
            for (ArcadeGames game : list) {
                this.gamesMap.put(game.getDdcode(), game);
            }
        }
    }

    /**
     * 通过游戏编号获取游戏参数
     *
     * @param gameCode 游戏编号
     * @return 游戏信息
     */
    public ArcadeGames getArcadeGames(int gameCode) {
        ArcadeGames arcadeGames = this.gamesMap.get(gameCode);
        if (arcadeGames == null) {
            arcadeGames = this.arcadeGamesMapper.selectByPrimaryKey(gameCode);
            if (arcadeGames != null) {
                this.gamesMap.put(gameCode, arcadeGames);
            }
        }
        return arcadeGames;
    }

    /**
     * 更新某个游戏信息，在更新游戏数据时调用，减少数据库查询
     *
     * @param arcadeGames
     */
    public void updateArcadeGames(ArcadeGames arcadeGames) {
        this.gamesMap.put(arcadeGames.getDdcode(), arcadeGames);
    }

    /**
     * 从数据库更新全部赛场信息
     */
    public void updateAllRoundExt() {
        if (this.roundExtMap.isEmpty()) {
            List<RoundExt> list = this.roundExtMapper.selectAll();
            for (RoundExt roundExt : list) {
                this.roundExtMap.put(roundExt.getDdcode(), roundExt);
            }
        }
    }

    /**
     * 获取赛场信息
     *
     * @param round
     * @return
     */
    public RoundExt getRoundExt(String round) {
        RoundExt roundExt = this.roundExtMap.get(round);
        if (roundExt == null) {
            roundExt = this.roundExtMapper.selectByddCode(round);
            if (roundExt != null) {
                this.roundExtMap.put(round, roundExt);
            }
        }
        return roundExt;
    }

    /**
     * 更新某个赛场信息，在更新游戏数据时调用，减少数据库查询
     *
     * @param roundExt
     */
    public void updateRoundExt(RoundExt roundExt) {
        this.roundExtMap.put(roundExt.getDdcode(), roundExt);
    }

    /**
     * 从数据库更新全部游戏参数
     */
    public void updateAllWxConfig() {
        if (this.wxConfigCache.isEmpty()) {
            List<WxConfig> list = this.wxConfigMapper.selectAll();
            for (WxConfig wxConfig : list) {
                this.wxConfigCache.put(wxConfig.getDdappid(), wxConfig);
            }
        }
    }

    /**
     * 通过游戏编号获取游戏参数
     *
     * @param appId 游戏编号
     * @return 游戏信息
     */
    public WxConfig getWxConfig(String appId) {
        WxConfig wxConfig = this.wxConfigCache.get(appId);
        if (wxConfig == null) {
            wxConfig = this.wxConfigMapper.selectByPrimaryKey(appId);
            if (wxConfig != null) {
                this.wxConfigCache.put(wxConfig.getDdappid(), wxConfig);
            }
        }
        return wxConfig;
    }

    /**
     * 更新某个游戏信息，在更新游戏数据时调用，减少数据库查询
     *
     * @param wxConfig
     */
    public void updateWxConfig(WxConfig wxConfig) {
        this.wxConfigCache.put(wxConfig.getDdappid(), wxConfig);
    }

    /////////////////////以上是CC翻新锅的/////////////////////////////

    public UserAllInfo getUserInfo(String uid) {
        if (userInfoCache.isEmpty()) {
            getAllUserInfo();
        }
        return userInfoCache.get(uid);
    }

    public List<UserAllInfo> getAllUserInfo() {
        List<UserAllInfo> userAllInfo = userInfoMapper.selectAll();
        userAllInfo.forEach(userInfo -> userInfoCache.put(userInfo.getDduid(), userInfo));
        return userAllInfo;
    }

    public List<RoundRecord> getAllRoundRecord() {
        List<RoundRecord> roundRecords = roundRecordMapper.selectAllRank();
        roundRecords.forEach(
                roundRecord -> roundRecordCache.put(String.valueOf(roundRecord.getDdindex()), roundRecord));
        return roundRecords;
    }

    public GoodsValue getGoodsValue(int gid) {
        if (gameValueCache.containsKey(gid)) {
            return gameValueCache.get(gid);
        }
        GoodsValue goodsValue = goodsValueMapper.selectByPrimaryKey(gid);
        if (goodsValue != null) {
            gameValueCache.put(gid, goodsValue);
        }
        return goodsValue;
    }


    public UserValue getUserValue(String uid) {
        if (userValueCache.isEmpty()) {
            getAllUserValue();
        }
        return userValueCache.get(uid);
    }

    public List<UserValue> getAllUserValue() {
        List<UserValue> userValues = userValueMapper.selectAll();
        userValues.forEach(userValue -> userValueCache.put(userValue.getDduid(), userValue));
        return userValues;
    }


    public List<WxConfig> getAllWxConfig() {
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
    public ArcadeGames getGames(int gameCode) {
        ArcadeGames games = getArcadeGames(gameCode);
        if (games != null) {
            return games;
        }
        return null;
    }


    /**
     * 获取赛制信息
     *
     * @param isGroup 是否群编号
     * @param ddmCode 赛制编号
     * @return 赛制信息
     */
    public JSONObject getRoundInfo(boolean isGroup, int ddmCode) {
        String key = MessageFormat.format("match-{0}-g{1}", isGroup, ddmCode);
        if (roundInfoMap.containsKey(key)) {
            return roundInfoMap.get(key);
        }
        //System.out.println("赛场信息" + key);
        JSONObject data = new JSONObject();
        String ddRound = null;
        if (isGroup) {
            RoundMatch roundGroup = roundMatchMapper.selectByPrimaryKey(ddmCode);
            if (roundGroup != null) {
                data.put("name", roundGroup.getDdname());
                ddRound = roundGroup.getDdround();
            }
        } else {
            RoundGame roundGame = roundGameMapper.selectByPrimaryKey(ddmCode);
            if (roundGame != null) {
                data.put("name", roundGame.getDdname());
                ddRound = roundGame.getDdround();
            }

        }
        if (ddRound != null) {
            RoundExt roundExt = getRoundExt(ddRound);
            if (roundExt != null) {
                data.put("time", roundExt.getTip());
                data.put("code", roundExt.getDdcode());
            }
        }
        roundInfoMap.put(key, data);
        return data;
    }

    public String getUserName(String uid) {
        return Objects.toString(userNameCache.get(uid), "未知");
    }

    /**
     * 进行加载缓存
     */
    public void synUserName(Set<String> users) {
        users.removeIf(uid -> userNameCache.containsKey(uid));
        if (!users.isEmpty()) {
            StringBuilder userStr = new StringBuilder();
            for (String temp : users) {
                if (userStr.length() != 0) {
                    userStr.append(",");
                }
                userStr.append("\"").append(temp).append("\"");
            }
            List<UserInfo> data = userInfoMapper.selectUserInfo(userStr.toString());
            if (data != null) {
                for (UserInfo info : data) {
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
    public String getUserName(String className, Set<String> users, String uid) {
        //用户是否已经缓存过
        if (userQueryRecord.containsKey(className) && userQueryRecord.get(className) == users.size()) {
            return Objects.toString(userNameCache.get(uid), "未知");
        }
        userQueryRecord.put(className, users.size());
        long current = System.currentTimeMillis();
        synUserName(users);
        return getUserName(className, users, uid);
    }

    /**
     * 通过昵称获取用户编号
     *
     * @param nickName 昵称信息
     * @return 用户uid
     */
    public Set<String> searchUserUid(String nickName) {
        Set<String> set = new HashSet<>();
        List<UserInfo> users = userInfoMapper.selectByDdName(nickName);
        if (users != null) {
            users.forEach(info -> set.add(info.getDduid()));
        }
        return set;
    }
}
