package com.fish.service;

import cn.hutool.json.JSONNull;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.UserValueMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoService implements BaseService<UserAllInfo>
{

    @Autowired
    UserValueMapper uerValueMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    CacheService cacheService;

    @Override
    public List<UserAllInfo> selectAll(GetParameter parameter)
    {
        List<UserAllInfo> userInfos =new ArrayList<>();
        JSONObject search = getSearchData(parameter.getSearchData());
        if(search !=null){
            String registertime = search.getString("registertime");
            String logintime = search.getString("logintime");
            if(StringUtils.isNotBlank(registertime) && StringUtils.isBlank(logintime)){
                Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                userInfos = userInfoMapper.selectByRegister(format.format(parse[0]), format.format(parse[1]));
            }

            else if(StringUtils.isBlank(registertime) && StringUtils.isNotBlank(logintime)){
                Date[] parse = XwhTool.parseDate(search.getString("logintime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                userInfos = userInfoMapper.selectByRegister(format.format(parse[0]), format.format(parse[1]));
            }
            else if(StringUtils.isNotBlank(registertime) && StringUtils.isNotBlank(logintime)){
                Date[] register = XwhTool.parseDate(search.getString("registertime"));
                Date[] login = XwhTool.parseDate(search.getString("logintime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                userInfos = userInfoMapper.selectByTime(format.format(register[0]), format.format(register[1]),format.format(login[0]), format.format(login[1]));
            }else {
                userInfos = userInfoMapper.selectAll();

            }
        }
        else {
            userInfos = userInfoMapper.selectAll();
        }

        for (UserAllInfo userInfo : userInfos)
        {
            Integer remainMoney = 0;
            String ddappid = userInfo.getDdappid();
            String dduid = userInfo.getDduid();
            UserValue userValue = cacheService.getUserValue(dduid);
            WxConfig wxConfig = cacheService.getWxConfig(ddappid);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                userInfo.setProductName(productName);
            }
            if (userValue != null)
            {
                Integer ddcoincount = userValue.getDdcoincount();
                Integer payMoney = userValue.getDdtotalpaymoney();
                userInfo.setDdcoincount(ddcoincount);
                userInfo.setDdtotalpaymoney(payMoney);
                Integer awardMoney = userValue.getDdawardmoney();
                Integer money = userValue.getDdmoney();
                remainMoney = awardMoney - money;
                userInfo.setRemainMoney(remainMoney);
                userInfo.setDdmoney(money);
            }
        }

        return userInfos;
    }

    //新增展示所有产品信息
    public int insert(UserAllInfo record)
    {
        return 1;
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(UserAllInfo record)
    {
        return 1;
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<UserAllInfo> getClassInfo()
    {
        return UserAllInfo.class;
    }

    @Override
    public boolean removeIf(UserAllInfo userAllInfo, JSONObject searchData)
    {
        if (existValueFalse(searchData.getString("ddname"), userAllInfo.getDdname()))
            return true;

        return (existValueFalse(searchData.getString("uid"), userAllInfo.getDduid()));
    }

}
