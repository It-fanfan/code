package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GamesService implements BaseService<ArcadeGames> {

    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;

    @Override
    //查询展示所有产品信息
    public List<ArcadeGames> selectAll(GetParameter parameter) {
        List<ArcadeGames> arcadeGames = arcadeGamesMapper.selectAll();
        for (ArcadeGames arcadeGame : arcadeGames) {
            String name = arcadeGame.getDdname128u();
            arcadeGame.setDdname128u(HexToStringUtil.getStringFromHex(name));
            if (StringUtils.isBlank(arcadeGame.getDdtitle2048u())) {
                String ddtitle = arcadeGame.getDdtitle2048u();
                arcadeGame.setDdtitle2048u(HexToStringUtil.getStringFromHex(ddtitle));
            }
        }

        return arcadeGames;
    }

    //新增展示所有产品信息
    public int insert(ArcadeGames record) {
        String name = record.getDdname128u();
        String ddtitle = record.getDdtitle2048u();
        record.setDdname128u(HexToStringUtil.getStringToHex(name));
        record.setDdtitle2048u(HexToStringUtil.getStringToHex(ddtitle));
        return arcadeGamesMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadeGames record) {
        String name = record.getDdname128u();
        String ddtitle = record.getDdtitle2048u();
        record.setDdname128u(HexToStringUtil.getStringToHex(name));
        record.setDdtitle2048u(HexToStringUtil.getStringToHex(ddtitle));
        return arcadeGamesMapper.updateByPrimaryKeySelective(record);
    }
    //更新游戏信息
    public int updateGameBySelective(ArcadeGames record) {
        String name = record.getDdname128u();
        record.setDdname128u(HexToStringUtil.getStringToHex(name));
        return arcadeGamesMapper.updateGameBySelective(record);
    }
    //新增游戏信息
    public int insertGameInfo(ArcadeGames record) {
        String name = record.getDdname128u();
        System.out.println("name :"+name);
        record.setDdname128u(HexToStringUtil.getStringToHex(name));
        return arcadeGamesMapper.insertGameInfo(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadeGames> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeGames arcadeGames, Map<String, String> searchData) {
        return false;
    }
}
