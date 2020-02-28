package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserApp;
import com.fish.dao.second.model.UserInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class RechargedService implements BaseService<Recharge>
{
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

    @Override
    //查询提现成功订单
    public List<Recharge> selectAll(GetParameter parameter)
    {
        List<Recharge> recharges = rechargeMapper.selectAllCharged();
        for (Recharge recharge : recharges)
        {
            //提现金额
            String dduid = recharge.getDduid();
            BigDecimal ddRmb = recharge.getDdrmb();
            //已提现金额设置
            if (recharge.getDdstatus() == 200)
            {
                recharge.setDdrmbed(ddRmb);
            }
            UserValue userValue = userValueMapper.selectByPrimaryKey(dduid);
            if (userValue != null)
            {
                Integer ddMoney = userValue.getDdmoney();
                //剩余提现金额设置
                recharge.setRemainAmount(ddMoney / 100);
            }
            UserApp userApp = userAppMapper.selectByPrimaryKey(dduid);
            if (userApp != null)
            {
                String ddoid = userApp.getDdoid();
                recharge.setDdopenid(ddoid);
            }
            String ddappid = recharge.getDdappid();
            com.fish.dao.second.model.WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddappid);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                Integer programType = wxConfig.getProgramType();
                recharge.setProductName(productName);
                recharge.setProgramType(programType);
            }

            UserInfo userInfo = userInfoMapper.selectByDdUid(dduid);
            if (userInfo != null)
            {
                String userName = userInfo.getDdname();
                recharge.setUserName(userName);
            }

        }
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
        String times = searchData.getString("times");
        Date[] parse = XwhTool.parseDate(times);
        if (times != null && times.length() != 0)
        {
            if (recharge.getDdtimes().before(parse[0]) || recharge.getDdtimes().after(parse[1]))
            {
                return true;
            }
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getProductName()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }


}
