package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RechargedService implements BaseService<Recharge>
{
    //提现记录
    private static final Logger LOG = LoggerFactory.getLogger(RechargedService.class);
    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserAppMapper userAppMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserValueMapper userValueMapper;
    @Autowired
    CacheService cacheService;
    @Autowired
    AllCostMapper allCostMapper;

    @Override
    //查询提现成功订单
    public List<Recharge> selectAll(GetParameter parameter)
    {
        long current = System.currentTimeMillis();
        List<Recharge> recharges;
        JSONObject search = getSearchData(parameter.getSearchData());
        String sql ="SELECT a.ddCurrent*0.01 AS remainAmount,w.program_type AS programType,w.product_name AS productName,u.ddName AS userName,r.* FROM  \n" +
                "recharge AS r LEFT JOIN all_cost AS a ON r.ddUid = a.ddUid AND a.ddCostType= 'recharge' AND DATE_FORMAT(a.ddTime,\"%Y-%m-%d %h\")\n" +
                " = DATE_FORMAT(r.ddTimes,\"%Y-%m-%d %h\")\n" +
                " LEFT JOIN wx_config AS w ON r.ddAppId =w.ddAppId LEFT JOIN user_info AS u ON r.ddUid=u.ddUid";
        System.out.println("查询,耗时:" + (System.currentTimeMillis() - current) + "ms");
        if (search == null || search.getString("times").isEmpty())
        {
            recharges = rechargeMapper.selectAllChargeSQL(sql);
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String searchSQL="SELECT * FROM ("+sql+") t WHERE  DATE(ddTimes) BETWEEN '"+ format.format(parse[0])+"' AND '"+format.format(parse[1])+"'";
            recharges = rechargeMapper.selectAllChargeSQL(searchSQL);
        }
        System.out.println("查询提现结束,耗时:" + (System.currentTimeMillis() - current) + "ms");
        return recharges;
    }

    //新增信息
    public int insert(Recharge record)
    {
        return rechargeMapper.insert(record);
    }

    //更新信息
    public int updateByPrimaryKeySelective(Recharge record)
    {
        return rechargeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ddtimes");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Recharge> getClassInfo()
    {
        return Recharge.class;
    }

    @Override
    public boolean removeIf(Recharge recharge, JSONObject searchData)
    {
        if (existValueFalse(searchData.getString("uid"), recharge.getDduid()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getDdappid()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }


}
