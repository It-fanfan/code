package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GamesSetService implements BaseService<ArcadeGameSet> {
    @Autowired
    com.fish.dao.primary.mapper.ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;

    @Override
    //查询展示所有产品信息
    public List<ArcadeGameSet> selectAll(GetParameter parameter) {
        List<ArcadeGameSet> arcadeGameSets = arcadeGameSetMapper.selectAll();
        for (ArcadeGameSet arcadeGameSet : arcadeGameSets) {
            String name = arcadeGameSet.getDdname128u();
            String desc = arcadeGameSet.getDddesc512u();
            String content = arcadeGameSet.getDdcontent512a();
            String ddContents = "";
            String[] split = content.split("#");
            for (int i = 0; i < split.length; i++) {
                String ddCode = split[i];
                ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(ddCode));

                String ddName128u = HexToStringUtil.getStringFromHex(arcadeGames.getDdname128u());
                if (ddContents.length() > 0) {
                    ddContents = ddContents + ",";
                }
                ddContents = ddContents + ddName128u;
            }
            arcadeGameSet.setDdname128u(HexToStringUtil.getStringFromHex(name));
            if (desc != null) {
                arcadeGameSet.setDddesc512u(HexToStringUtil.getStringFromHex(desc));
            }
            arcadeGameSet.setDdcontent512a(ddContents);
        }

        return arcadeGameSets;
    }

    //新增展示所有产品信息
    public int insert(ArcadeGameSet record) {
        List<String> gameBox = record.getGameBox();
        String ddContents = "";
        String ddName = "";
        if (gameBox.size() > 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                    String ddName128u = HexToStringUtil.getStringFromHex(arcadeGames.getDdname128u());
                    if (ddName.length() > 0) {
                        ddName = ddName + ",";
                    }
                    ddName = ddName + ddName128u;
                    if (ddContents.length() > 0) {
                        ddContents = ddContents + "#";
                    }
                    ddContents = ddContents + box;
                }
            }
        }
        record.setDdcontent512a(ddContents);
        record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname128u()));
        if (ddName != null) {
            record.setDddesc512u(HexToStringUtil.getStringToHex(ddName));
        }

        return arcadeGameSetMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ArcadeGameSet record) {
        List<String> gameBox = record.getGameBox();
        String ddContents = "";
        String ddName = "";
        if (gameBox.size() > 1) {
            for (String box : gameBox) {
                if (!"".equals(box)) {
                    ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(Integer.valueOf(box));
                    String ddName128u = HexToStringUtil.getStringFromHex(arcadeGames.getDdname128u());
                    if (ddName.length() > 0) {
                        ddName = ddName + ",";
                    }
                    ddName = ddName + ddName128u;
                    if (ddContents.length() > 0) {
                        ddContents = ddContents + "#";
                    }
                    ddContents = ddContents + box;
                }
            }
        }
        System.out.println("我是修改ddContents :" + ddContents);
        record.setDdcontent512a(ddContents);
        record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname128u()));
        if (ddName != null) {
            record.setDddesc512u(HexToStringUtil.getStringToHex(ddName));
        }
        return arcadeGameSetMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ArcadeGameSet> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(ArcadeGameSet arcadeGameSet, Map<String, String> searchData) {
        return false;
    }

}
