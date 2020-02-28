package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.PublicCentreMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.PublicCentre;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicCentreService implements BaseService<PublicCentre> {
    Logger LOGGER = LoggerFactory.getLogger(PublicCentreService.class);
    @Autowired
    PublicCentreMapper publicCentreMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    CacheService cacheService;

    @Override
    //查询展示所有游戏信息
    public List<PublicCentre> selectAll(GetParameter parameter) {
        List<PublicCentre> publicCentre = new ArrayList<>();
        List<PublicCentre> banners ;
        List<PublicCentre> recommends ;
        List<PublicCentre> games ;
        banners = publicCentreMapper.selectAllBanner();
        publicCentre.addAll(banners);
        recommends = publicCentreMapper.selectAllRecommend();
        publicCentre.addAll(recommends);
        games = publicCentreMapper.selectAllGame();
        publicCentre.addAll(games);
        return publicCentre;
    }

    public void selectAllForJson(GetParameter parameter) {

        List<Object> banners = new ArrayList<>();
        List<Object> recommends = new ArrayList<>();
        List<Object> games = new ArrayList<>();

        List<PublicCentre> publicBanners = publicCentreMapper.selectAllBanner();
        List<PublicCentre> publicRecommends = publicCentreMapper.selectAllRecommend();
        List<PublicCentre> publicGames = publicCentreMapper.selectAllGame();
        for (PublicCentre publicBanner : publicBanners)
        {
            String img = publicBanner.getResourceName();
            String detailImg = publicBanner.getDetailName();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("img", "images/banner/" + img);
            jsonObject.put("detailimg", "images/banner/" + detailImg);
            banners.add(jsonObject);
        }

        for (PublicCentre publicRecommend : publicRecommends)
        {
            String img = publicRecommend.getResourceName();
            Integer skipSet = publicRecommend.getSkipSet();
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(skipSet);
            String content = arcadeGameSet.getDdcontent512a();
            String[] split = content.split("#");
            List<Integer> gameyList = new ArrayList<>();
            for (String gameId : split)
            {
                Integer integer = Integer.valueOf(gameId);
                gameyList.add(integer);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameid", gameyList);
            jsonObject.put("icon", "images/recommend/" + img);
            jsonObject.put("name", "111");
            recommends.add(jsonObject);
        }
        for (PublicCentre publicGame : publicGames)
        {
            String flag;
            Integer skipSet = publicGame.getSkipSet();
            String img = publicGame.getResourceName();
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(skipSet);
            String content = arcadeGameSet.getDdcontent512a();
            String name = arcadeGameSet.getDdname();
            ArcadeGames game = cacheService.getGames(Integer.valueOf(content));
            Integer isPk = game.getDdispk();
            if (isPk == 1)
            {
                flag = "pk";
            } else
            {
                flag = "team";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameid", Integer.valueOf(content));
            jsonObject.put("icon", "images/game/" + img);
            jsonObject.put("name", name);
            jsonObject.put("desc", name);
            jsonObject.put("flag", flag);
            jsonObject.put("checkVersion", "");
            games.add(jsonObject);
        }
        // 读取原始json文件并进行操作和输出
        try
        {
            // 读取原始json文件
            BufferedReader br = new BufferedReader(new FileReader(PublicCentreService.class.getResource("/").getPath() + "config.json"));
            // 输出新的json文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    "/data/tomcat8/apache-public/webapps/public/mui_wxoa_debug/config.json"));
            String ws = null;
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            jsonObject.put("banners", banners);
            jsonObject.put("recommends", recommends);
            jsonObject.put("games", games);
            ws = jsonObject.toString();
            bw.write(ws);
            // bw.newLine();
            bw.flush();
            br.close();
            bw.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //新增展示游戏信息
    public int insert(PublicCentre record) {

        Byte skipType = record.getSkipType();
        if (skipType == 0)
        {
            record.setSkipSet(0);
        }
        return publicCentreMapper.insert(record);
    }

    //更新游戏信息
    public int updateByPrimaryKeySelective(PublicCentre record) {
        return publicCentreMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
        {
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("ddcode");
    }

    @Override
    public Class<PublicCentre> getClassInfo() {
        return PublicCentre.class;
    }

    @Override
    public boolean removeIf(PublicCentre centre, JSONObject searchData) {

        if (existValueFalse(searchData.getString("gameId"), centre.getId()))
        {
            return true;
        }
        return existValueFalse(searchData.getString("gameName"), centre.getDetailName());

    }

    public int deleteSelective(PublicCentre productInfo) {
        return publicCentreMapper.deleteByPrimaryKey(productInfo.getId());
    }
}
