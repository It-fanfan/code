package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.RoundReceiveMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.RoundReceive;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RoundReceiveService implements BaseService<RoundReceive>
{
    @Autowired
    RoundReceiveMapper roundReceiveMapper;

    @Autowired
    CacheService cacheService;

    @Override
    //查询排名信息
    public List<RoundReceive> selectAll(GetParameter parameter)
    {
        List<RoundReceive> roundReceives;


        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null)
        {
            roundReceives = roundReceiveMapper.selectAll();
        } else
        {
            String times = search.getString("times");
            if (StringUtils.isNotBlank(times))
            {
                Date[] parse = XwhTool.parseDate(search.getString("times"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                roundReceives = roundReceiveMapper.selectSearchTime(format.format(parse[0]), format.format(parse[1]));
            } else
            {
                roundReceives = roundReceiveMapper.selectAll();
            }
        }
        String className = RoundReceiveService.class.getSimpleName().toLowerCase();
        long current = System.currentTimeMillis();
        Vector<Long> record = new Vector<>();
        record.add(System.currentTimeMillis() - current);

        Set<String> users = new HashSet<>();
        for (RoundReceive roundReceive : roundReceives)
        {
            users.add(roundReceive.getDduid());
        }
        for (RoundReceive roundReceive : roundReceives)
        {
            String dduid = roundReceive.getDduid();
            String ddtype = roundReceive.getDdtype();
            if("rmb".equals(ddtype)){
                roundReceive.setDdtotal(roundReceive.getDdtotal()/100);
            }
            roundReceive.setUserName(cacheService.getUserName(className, users, dduid));
            ArcadeGames games = cacheService.getGames(roundReceive.getDdgcode());
            if (games != null)
            {
                roundReceive.setGameName(games.getDdname());
            } else
            {
                roundReceive.setGameName("未知");
            }
            JSONObject roundInfo = cacheService.getRoundInfo(roundReceive.getDdgroup(), roundReceive.getDdmcode());
            if (roundInfo.containsKey("name"))
            {
                roundReceive.setRoudName(roundInfo.getString("name"));
            } else
            {
                roundReceive.setRoudName("未知比赛");
            }
            if (roundInfo.containsKey("time"))
            {
                roundReceive.setRoudTime(roundInfo.getString("time"));
            } else
            {
                roundReceive.setRoudTime("未知时长");
            }
            if ((roundInfo.containsKey("code")))
            {
                roundReceive.setRoudCode(roundInfo.getString("code"));
            } else
            {
                roundReceive.setRoudCode("位置赛制");
            }
        }
        record.add(System.currentTimeMillis() - current);
        System.out.println("获取记录查询1:" + JSONObject.toJSONString(record));
        return roundReceives;
    }


    public int insert(RoundReceive record)
    {
        return roundReceiveMapper.insert(record);
    }

    public int updateByPrimaryKeySelective(RoundReceive record)
    {
        return roundReceiveMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddtime");
    }

    @Override
    public Class<RoundReceive> getClassInfo()
    {
        return RoundReceive.class;
    }

    @Override
    public boolean removeIf(RoundReceive record, JSONObject searchData)
    {

        if (existValueFalse(searchData.getString("userName"), record.getUserName()))
            return true;
        if (existValueFalse(searchData.getString("ddGroup"), record.getDdgroup().toString()))
            return true;
        if (existValueFalse(searchData.getString("gameCode"), record.getDdgcode()))
            return true;
        String roundCode = searchData.getString("roundCode");
        if (roundCode != null && roundCode.contains("-"))
            roundCode = roundCode.split("-")[0];
        return existValueFalse(roundCode, record.getRoudCode());
    }
}
