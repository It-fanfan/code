package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AllCostMapper;
import com.fish.dao.second.model.AllCost;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AllCostService implements BaseService<AllCost> {
    @Autowired
    AllCostMapper costMapper;
    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddtime");
    }

    @Override
    public Class<AllCost> getClassInfo() {
        return AllCost.class;
    }


    private Set<String> uid = null;

    @Override
    public boolean removeIf(AllCost allCost, JSONObject searchData) {
        String operate = searchData.getString("operate");
        if (operate != null)
        {
            if ("0".equals(operate) && allCost.getDdvalue() > 0)
                return true;
            if ("1".equals(operate) && allCost.getDdvalue() < 0)
                return true;
        }
        if (existValueFalse(searchData.getString("ddtype"), allCost.getDdtype()))
            return true;
        String gameCode = searchData.getString("gameCodeSelect");
        if (gameCode != null && !gameCode.trim().isEmpty())
        {
            if (!gameCode.equals(allCost.getDdcostextra()))
                return true;
        }
        if (existValueFalse(searchData.getString("ddcosttype"), allCost.getDdcosttype()))
            return true;
        if (existValueFalse(searchData.getString("appSelect"), allCost.getDdappid()))
            return true;
        if (existValueFalse(searchData.getString("dduid"), allCost.getDduid()))
            return true;
        if (uid != null)
        {
            return !uid.contains(allCost.getDduid());
        }
        String nickName = searchData.getString("nickName");
        if (nickName != null && !nickName.trim().isEmpty())
        {
            //通过昵称获取用户编号
            uid = cacheService.searchUserUid(nickName);
            return (uid != null && !uid.contains(allCost.getDduid()));
        }
        return false;
    }

    public void finish(GetResult<AllCost> result) {
        Set<String> users = new HashSet<>();
        result.getData().forEach(allCost -> users.add(allCost.getDduid()));
        cacheService.synUserName(users);
        result.getData().forEach(allCost -> allCost.setNickname(cacheService.getUserName(allCost.getDduid())));
    }

    @Override
    public List<AllCost> selectAll(GetParameter parameter) {
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("ddtime").isEmpty())
        {
            return new Vector<>();
        }
        uid = null;
        Date[] parse = XwhTool.parseDate(search.getString("ddtime"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<AllCost> allCosts = costMapper.selectAllCost(format.format(parse[0]), format.format(parse[1]));
        for (AllCost allCost : allCosts)
        {
            String ddtype = allCost.getDdtype();
            if ("rmb".equals(ddtype))
            {
                allCost.setDdhistory(allCost.getDdhistory() / 100);
                allCost.setDdcurrent(allCost.getDdcurrent() / 100);
                allCost.setDdvalue(allCost.getDdvalue() / 100);
            }
        }
        return allCosts;
    }
}
