package com.fish.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ExportResult;
import com.fish.utils.RedisUtil;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RankingService implements BaseService<ShowRanking>
{

    private RankingMapper rankingMapper;
    private RankingRecordMapper rankingRecordMapper;
    private ArcadeGamesMapper arcadeGamesMapper;

    private RedisUtil redisUtil;
    private UserInfoMapper userInfoMapper;
    private RoundExtMapper roundExtMapper;
    private RoundRecordMapper roundRecordMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    BaseConfig baseConfig;

    @Override
    //查询排名信息
    public List<ShowRanking> selectAll(GetParameter parameter)
    {
        long current = System.currentTimeMillis();
        List<RoundRecord> roundRecords;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty())
        {
            roundRecords = roundRecordMapper.selectSRank();
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            roundRecords = roundRecordMapper.selectSRankTime(format.format(parse[0]), format.format(parse[1]));
        }
        ArrayList<ShowRanking> shows = new ArrayList<>();

        System.out.println("查询,耗时:" + (System.currentTimeMillis() - current) + "ms");

        Date roundDate;
        ShowRanking showRanking;
        if (roundRecords.size() > 0)
        {
            for (RoundRecord roundRecord : roundRecords)
            {
                roundDate = roundRecord.getDdsubmit();
                showRanking = new ShowRanking();
                showRanking.setDdCode(roundRecord.getDdcode());
                showRanking.setDdGroup(roundRecord.getDdgroup());
                showRanking.setDdIndex(roundRecord.getDdindex());
                showRanking.setGamesCode(roundRecord.getDdgame());
                showRanking.setDdNumber(roundRecord.getDdresult());
                showRanking.setMatchdate(roundDate);
                Integer ddCode = roundRecord.getDdgame();
                //ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(ddCode);
                ArcadeGames arcadeGames = cacheService.getArcadeGames(ddCode);
                if (arcadeGames != null)
                {
                    String gameName = arcadeGames.getDdname();
                    showRanking.setGamesName(gameName);
                }
                String ddRound = roundRecord.getDdround();
                RoundExt roundExt = cacheService.getRoundExt(ddRound);
                //RoundExt roundExt = roundExtMapper.selectByddCodeS(ddRound);
                if (roundExt != null)
                {
                    showRanking.setRoundCode(ddRound);
                    String roundName = roundExt.getDdname();
                    showRanking.setRoundName(roundName);
                    String tip = roundExt.getTip();
                    showRanking.setRoundLength(tip);
                }
                shows.add(showRanking);
            }
        }
        return shows;
    }

    public List<ExportResult> selectResult(ShowRanking productInfo)
    {

        List<ExportResult> exportResults = new ArrayList<>();

        Integer ddCode = productInfo.getDdCode();
        Boolean ddGroup = productInfo.getDdGroup();
        Integer ddNumber = productInfo.getDdNumber();
        Date matchdate = productInfo.getMatchdate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(matchdate);
        String roundCode = productInfo.getRoundCode();
        String roundName = productInfo.getRoundName();
        String roundLength = productInfo.getRoundLength();
        int group = 0;
        int num = 0;
        if (ddGroup)
        {
            group = 1;
        } else
        {
            group = 0;
        }
        if (ddNumber != null)
        {
            int numbers = ddNumber;
            if (numbers <= 100)
            {
                num = 0;
            } else
            {
                num = numbers / 100;
            }
        }
        Integer ddIndex = productInfo.getDdIndex();
        //String ddIndex = parse.getString("ddIndex");
        String matchRes = baseConfig.getMatchRes();
        JSONArray allResult = new JSONArray();
        for (int i = 0; i <= num; i++)
        {
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);

        }
        for (Object object : allResult)
        {
            ExportResult exportResult = new ExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());

            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object value = jsonObject.get("value");
            Object type = jsonObject.get("type");
            Object mark = jsonObject.get("mark");
            int reward = Integer.parseInt(value.toString());
            if (type.toString().equals("rmb"))
            {
                reward = reward / 100;
            }
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setIndex(Integer.parseInt(index.toString()));
            exportResult.setName(name.toString());
            exportResult.setUid(uid.toString());
            exportResult.setValue(reward);
            exportResult.setType(type.toString());
            exportResult.setMark(Integer.parseInt(mark.toString()));
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setMatchdate(format);

            exportResults.add(exportResult);
        }

        return exportResults;
    }

    public int insert(Ranking record)
    {
        Object gameCode = redisUtil.get("gameCode");
        Object matchCode = redisUtil.get("matchCode");
        Object day = redisUtil.get("day");
        Object index = redisUtil.get("index");
        Object uid = redisUtil.get("ddUid");
        Object ranking = redisUtil.get("ranking");
        Object mark = redisUtil.get("score");

        Ranking rank = new Ranking();
        rank.setGamecode(Integer.parseInt(gameCode.toString()));
        rank.setMark(Long.valueOf(mark.toString()));
        rank.setMatchid(matchCode.toString());
        rank.setMatchindex(Integer.parseInt(index.toString()));
        rank.setRanking(Long.valueOf(ranking.toString()));
        rank.setUid(uid.toString());
        rank.setInserttime(new Timestamp(new Date().getTime()));
        return rankingMapper.insert(record);
    }

    public int updateByPrimaryKeySelective(RankingRecord record)
    {
        record.setDdtime(new Timestamp(new Date().getTime()));
        return rankingRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("matchdate");
    }

    @Override
    public Class<ShowRanking> getClassInfo()
    {
        return ShowRanking.class;
    }

    @Override
    public boolean removeIf(ShowRanking record, JSONObject searchData)
    {

        if (existValueFalse(searchData.getString("gameName"), record.getGamesCode()))
        {
            return true;
        }
//        if (existValueFalse(searchData.getString("roundName"), record.getRoundName()))
//        {
//            return true;
//        }
//        return (existTimeFalse(record.getMatchdate(), searchData.getString("times")));
        String roundName = searchData.getString("roundName");

        if (roundName != null && roundName.contains("-"))
            roundName = roundName.split("-")[0];
        return (existValueFalse(roundName, record.getRoundCode()));
    }

    @Autowired
    public RankingService(RankingMapper rankingMapper, RankingRecordMapper rankingRecordMapper, GameRoundMapper gameRoundMapper, ArcadeGamesMapper arcadeGamesMapper, RoundsMapper roundsMapper, RedisUtil redisUtil, UserInfoMapper userInfoMapper, RoundExtMapper roundExtMapper, RoundRecordMapper roundRecordMapper, ShowRanking showRanking)
    {
        this.rankingMapper = rankingMapper;
        this.rankingRecordMapper = rankingRecordMapper;
        this.arcadeGamesMapper = arcadeGamesMapper;
        this.redisUtil = redisUtil;
        this.userInfoMapper = userInfoMapper;
        this.roundExtMapper = roundExtMapper;
        this.roundRecordMapper = roundRecordMapper;
    }

}
