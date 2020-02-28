package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.RoundExtMapper;
import com.fish.dao.primary.model.RoundExt;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RoundExtService implements BaseService<RoundExt>
{

    @Autowired
    RoundExtMapper roundExtMapper;


    @Override
    //查询常规游戏赛制信息
    public List<RoundExt> selectAll(GetParameter parameter)
    {

        List<RoundExt> roundExts = roundExtMapper.selectAll();

        return roundExts;
    }


    public int insert(RoundExt record)
    {
        String ddReward = record.getDdreward();
        if (ddReward == null || ddReward.length() == 0 || "0".equals(ddReward))
        {
            ddReward = "['0#0#coin#0']";
            record.setDdreward(ddReward);
        }
        String roundLength = record.getRoundLength();
        String timeValue = roundLength.substring(0, roundLength.length() - 1);
        String timeFormat = roundLength.substring(roundLength.length() - 1);
        if ("s".equalsIgnoreCase(timeFormat))
        {
            record.setDdtime(Long.parseLong(timeValue));
            record.setTip(timeValue + "秒");
        } else if ("m".equalsIgnoreCase(timeFormat))
        {
            record.setTip(timeValue + "分钟");
            record.setDdtime(Long.parseLong(timeValue) * 60);
        } else if ("h".equalsIgnoreCase(timeFormat))
        {
            record.setTip(timeValue + "小时");
            record.setDdtime(Long.parseLong(timeValue) * 60 * 60);
        } else if ("d".equalsIgnoreCase(timeFormat))
        {
            record.setTip(timeValue + "天");
            record.setDdtime(Long.parseLong(timeValue) * 60 * 60 * 24);
        }
        Boolean ddgroup = record.getDdgroup();
        if (ddgroup)
        {
            int maxId = roundExtMapper.selectGMaxId();
            record.setDdcode("G" + (maxId + 1));
            record.setDdgroup(true);

        } else
        {
            int maxId = roundExtMapper.selectSMaxId();
            record.setDdcode("S" + (maxId + 1));
            record.setDdgroup(false);
        }
        record.setDdstate(true);
        record.setInserttime(new Timestamp(new Date().getTime()));
        int insert = roundExtMapper.insert(record);
        // int insert = 1;
        return insert;
    }


    public int updateByPrimaryKeySelective(RoundExt record)
    {
        String ddReward = record.getDdreward();
        if (ddReward == null || ddReward.length() == 0 || "0".equals(ddReward))
        {
            ddReward = "['0#0#coin#0']";
            record.setDdreward(ddReward);
        }
        String roundLength = record.getRoundLength();
        if (roundLength != null && roundLength.length() > 0)
        {
            String timeValue = roundLength.substring(0, roundLength.length() - 1);
            String timeFormat = roundLength.substring(roundLength.length() - 1);
            if ("s".equalsIgnoreCase(timeFormat))
            {
                record.setDdtime(Long.parseLong(timeValue));
                record.setTip(timeValue + "秒");
            } else if ("m".equalsIgnoreCase(timeFormat))
            {
                record.setTip(timeValue + "分钟");
                record.setDdtime(Long.parseLong(timeValue) * 60);
            } else if ("h".equalsIgnoreCase(timeFormat))
            {
                record.setTip(timeValue + "小时");
                record.setDdtime(Long.parseLong(timeValue) * 60 * 60);
            } else if ("d".equalsIgnoreCase(timeFormat))
            {
                record.setTip(timeValue + "天");
                record.setDdtime(Long.parseLong(timeValue) * 60 * 60 * 24);
            }
        }
        record.setInserttime(new Timestamp(new Date().getTime()));
        return roundExtMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("inserttime");
    }

    @Override
    public Class<RoundExt> getClassInfo()
    {
        return RoundExt.class;
    }

    @Override
    public boolean removeIf(RoundExt record, JSONObject searchData)
    {

//        if (existValueFalse(searchData.get("appId"), appConfig.getDdappid())) {
//            return true;
//        }
//        if (existValueFalse(searchData.get("gameName"), appConfig.getDdname())) {
//            return true;
//        }
        return true;
    }


}
