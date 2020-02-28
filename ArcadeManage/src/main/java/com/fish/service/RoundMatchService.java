package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.RoundExtMapper;
import com.fish.dao.primary.mapper.RoundMatchMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundMatch;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RoundMatchService implements BaseService<RoundMatch>
{

    @Autowired
    RoundMatchMapper roundMatchMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    RoundExtMapper roundExtMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    @Override
    //查询小程序赛制配置 roundMatch
    public List<RoundMatch> selectAll(GetParameter parameter)
    {
        String url = baseConfig.getResHost();
        List<RoundMatch> roundMatchs = roundMatchMapper.selectAll();
        for (RoundMatch roundMatch : roundMatchs)
        {
            Integer ddgame = roundMatch.getDdgame();
            ArcadeGames arcadeGames = cacheService.getArcadeGames(ddgame);
            if (arcadeGames != null)
            {
                String gameName = arcadeGames.getDdname();
                roundMatch.setGameName(gameName);
                String ddShareRes = arcadeGames.getDdshareres();
                if (StringUtils.isNotEmpty(ddShareRes))
                {
                    JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                    for (int i = 0; i < shareLists.size(); i++)
                    {
                        JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                        if (jsonObject.getInteger("position") == 4)
                        {
                            String icon = WxConfigService.concatUrl(url, jsonObject.getString("url"), "g" + arcadeGames.getDdcode(), "share");
                            if (icon != null)
                                roundMatch.setJumpDirect(icon);
                        }
                    }
                }
            }
            String ddRound = roundMatch.getDdround();
            RoundExt roundExt = cacheService.getRoundExt(ddRound);
            if (roundExt != null)
            {
                String ddReward = roundExt.getDdreward();
                String roundName = roundExt.getDdname();
                String ddDesc = roundExt.getTip();
                roundMatch.setRoundName(roundName);
                roundMatch.setDdreward(ddReward);
                roundMatch.setRoundLength(ddDesc);
            }
            String ddAppId = roundMatch.getDdappid();
            WxConfig wxConfig = cacheService.getWxConfig(ddAppId);
            if (wxConfig != null)
            {
                String appName = wxConfig.getProductName();
                roundMatch.setAppName(appName);
            }
        }
        return roundMatchs;
    }

    //新增小程序赛制配置
    public int insert(RoundMatch record)
    {

        String roundSelect = record.getRoundSelect();
        String[] roundSplit;
        roundSplit = roundSelect.split("-");
        String gameCodeSelect = record.getGameCodeSelect();
        Integer gameCode = 0;
        if (gameCodeSelect != null && gameCodeSelect.length() > 0)
        {
            gameCode = Integer.parseInt(gameCodeSelect);
            record.setDdgame(gameCode);
            ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(gameCode);
            Integer isPk = arcadeGames.getDdispk();
            if (isPk == 1)
            {
                if (roundSplit.length > 0)
                {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000 + 300 * 1000);
                    record.setDdend(endDate);
                    //record.setDdend( );
                }
            } else
            {
                if (roundSplit.length > 0)
                {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000);

                    record.setDdend(endDate);
                    //record.setDdend( );
                }
            }
        }

        record.setDdtime(new Timestamp(new Date().getTime()));
        String appId = record.getAppNameSelect();

        record.setDdappid(appId);

        System.out.println(record);
        return roundMatchMapper.insert(record);
    }

    //修改小程序赛制配置
    public int updateByPrimaryKeySelective(RoundMatch record)
    {
        String roundSelect = record.getRoundSelect();
        String[] roundSplit;
        roundSplit = roundSelect.split("-");
        String gameCodeSelect = record.getGameCodeSelect();
        if (gameCodeSelect != null && gameCodeSelect.length() > 0)
        {
            int gameCode = Integer.parseInt(gameCodeSelect);
            ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(gameCode);
            Integer isPk = arcadeGames.getDdispk();
            if (isPk == 1)
            {
                //更新开始时间、结束时间
                if (roundSplit.length > 0)
                {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000 + 300 * 1000);
                    record.setDdend(endDate);
                }
            } else
            {
                if (roundSplit.length > 0)
                {
                    String ddCode = roundSplit[0];
                    record.setDdround(ddCode);
                    RoundExt roundExt = roundExtMapper.selectByddCodeG(ddCode);
                    Long ddTime = roundExt.getDdtime();
                    Date ddStart = record.getDdstart();
                    Date endDate = new Date(ddStart.getTime() + ddTime * 1000);
                    record.setDdend(endDate);
                    //record.setDdend( );
                }
            }
            record.setDdgame(Integer.parseInt(gameCodeSelect));
        }

        record.setDdtime(new Timestamp(new Date().getTime()));
        String appId = record.getAppNameSelect();
        if (appId != null && appId.length() > 0)
        {
            record.setDdappid(appId);
        }
        System.out.println(record);
        int i = roundMatchMapper.updateByPrimaryKeySelective(record);
        return i;
    }

    //默认排序
    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ddtime");
        parameter.setOrder("desc");
    }

    @Override
    public Class<RoundMatch> getClassInfo()
    {
        return RoundMatch.class;
    }

    //筛选
    @Override
    public boolean removeIf(RoundMatch recharge, JSONObject searchData)
    {
        if (existValueFalse(searchData.getString("productName"), recharge.getAppName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("gameName"), recharge.getGameName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("ddstate"), recharge.getDdstate().toString()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("roundName"), recharge.getRoundName()));
    }

    //查询所有比赛赛制
    public List<RoundExt> selectAllG()
    {
        List<RoundExt> roundExts = roundExtMapper.selectAllG();
        for (RoundExt roundExt : roundExts)
        {
            String ddCode = roundExt.getDdcode();
            String ddName = roundExt.getDdname();
            roundExt.setDdcode(ddCode + "-" + ddName);
        }
        return roundExts;
    }

    public List<RoundExt> selectAllRound()
    {
        List<RoundExt> roundExtS = roundExtMapper.selectAll();
        for (RoundExt roundExt : roundExtS)
        {
            String ddCode = roundExt.getDdcode();
            String ddName = roundExt.getDdname();
            roundExt.setDdcode(ddCode + "-" + ddName);
        }
        return roundExtS;
    }

    //获取所有产品信息
    public List<WxConfig> getAppName()
    {
        return wxConfigMapper.selectAll();
    }
}
