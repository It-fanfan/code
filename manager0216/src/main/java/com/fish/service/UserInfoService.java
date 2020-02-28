package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Service
public class UserInfoService implements BaseService<UserAllInfo> {
    @Autowired
    AllCostMapper allCostMapper;
    @Autowired
    UserValueMapper uerValueMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    CacheService cacheService;

    @Autowired
    RechargeMapper rechargeMapper;

    @Override
    public List<UserAllInfo> selectAll(GetParameter parameter) {
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null)
        {
            return new Vector<>();
        }
        long current = System.currentTimeMillis();
        List<UserAllInfo> userInfos = null;
        if (search != null)
        {
            String registertime = search.getString("registertime");
            String ddname = search.getString("ddname");
            String uid = search.getString("uid");
            String ddOpenID = search.getString("ddoid");
            if (StringUtils.isNotBlank(ddname))
            {
                if (StringUtils.isNotBlank(uid))
                {
                    if (StringUtils.isNotBlank(ddOpenID))
                    {
                        if (StringUtils.isNotBlank(registertime))
                        {
                            Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "SELECT *  FROM  user_info where  ddOid  like '" + ddOpenID + "%' and ddName like '" + ddname + "%'" + " and ddUid like '%" + uid + "%' and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        } else
                        {
                            String sql = "SELECT *  FROM  user_info where ddOid like '" + ddOpenID + "%' and ddName like '" + ddname + "%'" + " and ddUid like '%" + uid + "%'";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        }
                    } else
                    {
                        if (StringUtils.isNotBlank(registertime))
                        {
                            Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "SELECT *  FROM  user_info where  ddName like '" + ddname + "%'" + " and ddUid like '%" + uid + "%' and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        } else
                        {
                            String sql = "SELECT *  FROM  user_info where ddName like '" + ddname + "%'" + " and ddUid like '%" + uid + "%'";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        }
                    }
                } else
                {
                    if (StringUtils.isNotBlank(ddOpenID))
                    {
                        if (StringUtils.isNotBlank(registertime))
                        {
                            Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "SELECT *  FROM  user_info where ddOid like '" + ddOpenID + "%' and ddName like '" + ddname + "%'" + " and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'  limit 0,10";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        } else
                        {
                            String sql = "SELECT *  FROM  user_info where  ddoid like '" + ddOpenID + "%' and ddName like '" + ddname + "%' ";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        }
                    } else
                    {
                        if (StringUtils.isNotBlank(registertime))
                        {
                            Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String sql = "SELECT *  FROM  user_info where ddName like '" + ddname + "%'" + " and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'  limit 0,10";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        } else
                        {
                            String sql = "SELECT *  FROM  user_info where ddName like '" + ddname + "%' ";
                            userInfos = userInfoMapper.selectBySQL(sql);
                        }
                    }
                }
            } else if (StringUtils.isBlank(ddname) && StringUtils.isNotBlank(uid))
            {
                if (StringUtils.isNotBlank(ddOpenID))
                {
                    if (StringUtils.isNotBlank(registertime))
                    {
                        Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String sql = "SELECT *  FROM  user_info where ddOid like '" + ddOpenID + "%' and ddUid like '%" + uid + "%' and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'  limit 0,10";
                        userInfos = userInfoMapper.selectBySQL(sql);
                    } else
                    {
                        String sql = "SELECT *  FROM  user_info where  ddOid like '" + ddOpenID + "%' and ddUid like '%" + uid + "%' ";
                        userInfos = userInfoMapper.selectBySQL(sql);
                    }
                } else
                {
                    if (StringUtils.isNotBlank(registertime))
                    {
                        Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String sql = "SELECT *  FROM  user_info where  ddUid like '%" + uid + "%' and " + " DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'  limit 0,10";
                        userInfos = userInfoMapper.selectBySQL(sql);
                    } else
                    {
                        String sql = "SELECT *  FROM  user_info where  ddUid like '%" + uid + "%' ";
                        userInfos = userInfoMapper.selectBySQL(sql);
                    }
                }
            } else if (StringUtils.isBlank(ddname) && StringUtils.isBlank(uid) && StringUtils.isNotBlank(ddOpenID))
            {
                if (StringUtils.isNotBlank(registertime))
                {
                    Date[] parse = XwhTool.parseDate(search.getString("registertime"));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String sql = "SELECT *  FROM  user_info where  ddOid like '" + ddOpenID + "%' and  DATE(ddRegisterTime) between '" + format.format(parse[0]) + "' and '" + format.format(parse[1]) + "'  limit 0,10";
                    userInfos = userInfoMapper.selectBySQL(sql);
                } else
                {
                    String sql = "SELECT *  FROM  user_info where  ddOid like '" + ddOpenID + "%' ";
                    userInfos = userInfoMapper.selectBySQL(sql);
                }
            }
        }
        LOGGER.info("查询,耗时:" + (System.currentTimeMillis() - current) + "ms");

        for (UserAllInfo userInfo : userInfos)
        {
            String ddappid = userInfo.getDdappid();
            String dduid = userInfo.getDduid();

            WxConfig wxConfig = cacheService.getWxConfig(ddappid);
            //WxConfig wxConfig =wxConfigMapper.selectByPrimaryKey(ddappid);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                userInfo.setProductName(productName);
            }
            String cashRmb = "select SUM(ddRmb) from recharge WHERE ddStatus = 200 AND ddUid ='" + dduid + "'";
            BigDecimal decimal = rechargeMapper.selectRecharged(cashRmb);
            if (decimal != null)
            {
                userInfo.setCashOut(decimal.intValue());
            } else
            {
                userInfo.setCashOut(0);
            }
            String coinSql = "select * from all_cost where ddUid ='" + dduid + "' and ddType ='coin' ORDER BY id DESC LIMIT 1";
            AllCost coinCost = allCostMapper.selectCurrentCoin(coinSql);

            String rmbSql = "select * from all_cost where ddUid ='" + dduid + "' and ddType ='rmb' ORDER BY id DESC LIMIT 1";
            AllCost rmbCost = allCostMapper.selectCurrentCoin(rmbSql);
            if (coinCost != null)
            {
                Long coinCurrent = coinCost.getDdcurrent();
                userInfo.setDdcoincount(coinCurrent.intValue());
            } else
            {
                userInfo.setDdcoincount(0);
            }
            if (rmbCost != null)
            {   //剩余可提现金额
                Long rmbCurrent = rmbCost.getDdcurrent();
                userInfo.setRemainMoney(rmbCurrent.intValue() / 100);
            } else
            {
                userInfo.setRemainMoney(0);
            }

        }
        LOGGER.info("返回,耗时:" + (System.currentTimeMillis() - current) + "ms");
        return userInfos;
    }

    //新增用户信息
    public int insert(UserAllInfo record) {
        return 1;
    }

    //更新用户信息
    public int updateByPrimaryKeySelective(UserAllInfo record) {
        return 1;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<UserAllInfo> getClassInfo() {
        return UserAllInfo.class;
    }

    @Override
    public boolean removeIf(UserAllInfo userAllInfo, JSONObject searchData) {

        return (existValueFalse(searchData.getString("appSelect"), userAllInfo.getDdappid()));
    }

}
