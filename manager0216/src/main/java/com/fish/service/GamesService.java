package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.protocols.GetParameter;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamesService implements BaseService<ArcadeGames>
{

    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    ArcadeGames arcadeGames;
    @Autowired
    BaseConfig baseConfig;

    @Override
    //查询展示所有游戏信息
    public List<ArcadeGames> selectAll(GetParameter parameter)
    {
        List<ArcadeGames> arcadeGames = new ArrayList<>();

        arcadeGames = arcadeGamesMapper.selectAll();

        return arcadeGames;
    }

    //新增展示游戏信息
    public int insert(ArcadeGames record)
    {

        return arcadeGamesMapper.insert(record);
    }

    //更新游戏信息
    public int updateByPrimaryKeySelective(ArcadeGames record)
    {
        return arcadeGamesMapper.updateByPrimaryKeySelective(record);
    }

    //更新游戏信息
    public int updateGameBySelective(ArcadeGames record)
    {

        return arcadeGamesMapper.updateGameBySelective(record);
    }

    //新增游戏信息
    public int insertGameInfo(ArcadeGames record)
    {

        return arcadeGamesMapper.insertGameInfo(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddcode");
    }

    @Override
    public Class<ArcadeGames> getClassInfo()
    {
        return ArcadeGames.class;
    }

    @Override
    public boolean removeIf(ArcadeGames arcadeGames, JSONObject searchData)
    {

        if (existValueFalse(searchData.getString("gameId"), arcadeGames.getDdcode()))
        {
            return true;
        }
        return existValueFalse(searchData.getString("gameName"), arcadeGames.getDdname());

    }

    public int flushGamesResources(JSONObject parameter)
    {
        int updateGames = 0;
        System.out.println(parameter);
//        String url = "http://192.168.1.55:8980/persieDeamon/match/"+"{"+ddcode+"}/"+"{"+ddcode+"}.json";
        String shareUrl = "";
        String resHost = baseConfig.getResHost();
        StringBuilder url = new StringBuilder(resHost + "g");
        JSONArray array = parameter.getJSONArray("gameList");
        for (int i = 0; i < array.size(); i++)
        {
            int gameCode = array.getInteger(i);
            ArcadeGames game = arcadeGamesMapper.selectByPrimaryKey(gameCode);

            try {
                if (game != null)
                {
                    String resPath = baseConfig.getReadRes();
                    String share = XwhTool.readFileString(resPath.concat("g" + gameCode).concat("/share/readme.json"));
                    if (share != null)
                    {
                        game.setDdshareres(share);
                        updateGames += arcadeGamesMapper.updateByPrimaryKeySelective(game);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updateGames;
    }

}
