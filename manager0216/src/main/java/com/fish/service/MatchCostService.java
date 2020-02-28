package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AllCostMapper;
import com.fish.protocols.GetParameter;
import com.fish.protocols.MatchCost;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MatchCostService implements BaseService<MatchCost>
{
    @Autowired
    AllCostMapper costMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("ddtime");
    }

    @Override
    public Class<MatchCost> getClassInfo()
    {
        return MatchCost.class;
    }

    @Override
    public boolean removeIf(MatchCost allCost, JSONObject searchData)
    {
        return false;
    }

    @Override
    public List<MatchCost> selectAll(GetParameter parameter)
    {
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("ddtime").isEmpty())
        {
            return new Vector<>();
        }
        Date[] parse = XwhTool.parseDate(search.getString("ddtime"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return findAll(format.format(parse[0]), format.format(parse[1]), search);
    }


    /**
     * 通过查询条件获取参数6
     *
     * @param searchData 查询内容
     * @return 条件
     */
    private String getPar0(JSONObject searchData)
    {
        switch (searchData.getString("operate"))
        {
            case "app":
                return "ddAppId as appId,";
            case "game":
                return "ddCostExtra as gameCode,";
            case "app-game":
                return "ddAppId as appId,ddCostExtra as gameCode,";
            default:
                return "";
        }
    }

    /**
     * 通过查询条件获取参数6
     *
     * @param searchData 查询内容
     * @return 条件
     */
    private String getPar3(JSONObject searchData)
    {
        Set<String> costType = new HashSet<>();
        costType.add("gameEnter");
        costType.add("relive");
        String costStr = searchData.getString("ddcosttype");
        if (costStr != null && !costStr.trim().isEmpty())
        {
            costType.removeIf(type -> !type.equals(costStr));
        }
        StringBuilder SQL = new StringBuilder();
        costType.forEach(type ->
        {
            if (0 != SQL.length())
            {
                SQL.append(",");
            }
            SQL.append("'").append(type).append("'");
        });
        return SQL.toString();
    }

    /**
     * 通过查询条件获取参数6
     *
     * @param searchData 查询内容
     * @return 条件
     */
    private String getPar5(JSONObject searchData)
    {
        switch (searchData.getString("operate"))
        {
            case "app":
                return ",appId";
            case "game":
                return ",gameCode";
            case "app-game":
                return ",appId,gameCode";
            default:
                return "";
        }
    }

    /**
     * 通过查询条件获取参数6
     *
     * @param searchData 查询内容
     * @return 条件
     */
    private String getPar6(JSONObject searchData)
    {
        StringBuilder SQL = new StringBuilder();
        String gameCode = searchData.getString("gameCodeSelect");
        if (gameCode != null && !gameCode.trim().isEmpty())
        {
            SQL.append(" and ddCostExtra=").append(gameCode);
        }
        String appId = searchData.getString("appSelect");
        if (appId != null && !appId.trim().isEmpty())
        {
            SQL.append(" and ddAppId='").append(appId).append("'");
        }
        switch (searchData.getString("operate"))
        {
            case "game":
            case "app-game":
            {
                SQL.append(" and ddCostExtra> 1000");
            }
            break;
        }
        return SQL.toString();
    }

    /**
     * @return 基本SQL
     */
    private String getBaseSQL()
    {
        return "select {0}DATE(a.`ddTime`)AS ddTime,COUNT(1)AS {7},COUNT(DISTINCT ddUid)AS {8} from all_cost a where DATE(a.`ddTime`) BETWEEN \"{1}\" and \"{2}\" and ddCostType in ({3}) and ddType=\"{4}\" {6} GROUP BY DATE(ddTime){5}";
    }

    /**
     * 通过参数获取SQL
     *
     * @param type       类型
     * @param start      开始时间
     * @param end        截至时间
     * @param searchData 查询条件
     * @return 查询SQL
     */
    private String getSQL(String type, String start, String end, JSONObject searchData)
    {
        Object[] pars = new Object[9];
        int index = 0;
        pars[index++] = getPar0(searchData);
        pars[index++] = start;
        pars[index++] = end;
        pars[index++] = getPar3(searchData);
        pars[index++] = type;
        pars[index++] = getPar5(searchData);
        pars[index++] = getPar6(searchData);
        switch (type)
        {
            case "coin":
            {
                pars[index++] = "coinCount";
                pars[index] = "coinTotal";
            }
            break;
            case "video":
            {
                pars[index++] = "videoCount";
                pars[index] = "videoTotal";
            }
            break;
        }
        return MessageFormat.format(getBaseSQL(), pars);
    }

    /**
     * 進行詳情查詢SQL
     *
     * @return 查詢SQL
     */
    private List<MatchCost> findAll(String start, String end, JSONObject searchData)
    {
        String coinSQL = getSQL("coin", start, end, searchData);
        String videoSQL = getSQL("video", start, end, searchData);
        System.out.println(coinSQL);
        System.out.println(videoSQL);
        List<MatchCost> cost1 = costMapper.selectBySQL(coinSQL);
        List<MatchCost> cost2 = costMapper.selectBySQL(videoSQL);
        cost2.forEach(cost ->
        {
            for (MatchCost temp : cost1)
            {
                if (temp.compare(cost))
                {
                    temp.setVideoCount(cost.getVideoCount());
                    temp.setVideoTotal(cost.getVideoTotal());
                    break;
                }
            }
        });
        return cost1;
    }
}
