package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BuyPayService implements BaseService<BuyPay> {

    @Autowired
    BuyPayMapper buyPayMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;

    @Override
    //查询常所有买量信息
    public List<BuyPay> selectAll(GetParameter parameter) {
        List<BuyPay> buyPayList;
        JSONObject search = getSearchData(parameter.getSearchData());
        String sql = "select * from buy_pay where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (search == null)
        {
            buyPayList = buyPayMapper.selectAll();
        } else
        {
            String ddAppId = search.getString("ddappid");
            String times = search.getString("times");
            if (StringUtils.isNotBlank(ddAppId))
            {
                SQL.append(" and buy_app_id =" + "'").append(ddAppId).append("'");
                if (StringUtils.isNotBlank(times))
                {
                    Date[] parse = XwhTool.parseDate(times);
                    SQL.append(" and DATE(buy_date) between '").append(format.format(parse[0])).append("' and '").append(format.format(parse[1])).append("'");
                }
                buyPayList = buyPayMapper.selectSearch(SQL.toString());
            } else
            {
                if (StringUtils.isNotBlank(times))
                {
                    Date[] parse = XwhTool.parseDate(times);
                    SQL.append(" and DATE(buy_date) between '").append(format.format(parse[0])).append("' and '").append(format.format(parse[1])).append("'");
                    buyPayList = buyPayMapper.selectSearch(SQL.toString());
                } else
                {
                    buyPayList = buyPayMapper.selectAll();
                }
            }
        }
        return buyPayList;
    }

    public int insertExcel(JSONObject record) {
        String context = record.getString("context");
        System.out.println("context :" + context);
        context = context.substring(1, context.length() - 1);
        try
        {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            List<BuyPay> lists = new ArrayList<>();
            for (int i = 0; i < param.size(); i++)
            {
                String singleData = param.get(i).toString();

                String singleString = singleData.substring(1, singleData.length() - 1);
                System.out.println("我是singleString :" + singleString);
                String[] split = singleString.split("], ");

                for (int j = 0; j < split.length; j++)
                {

                    if (j != 0 && j < split.length)
                    {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        BuyPay buyPay = new BuyPay();
                        for (int x = 0; x < singleSplit.length; x++)
                        {
                            //System.out.println("我是单条数据 "+x +" :"+singleSplit[x] );
                            if (x == 0)
                            {
                                mapSingle.put("buyDate", singleSplit[x].trim());
                            }
                            if (x == 1)
                            {
                                mapSingle.put("buyProductName", singleSplit[x].trim());
                            }
                            if (x == 2)
                            {
                                mapSingle.put("buyCost", singleSplit[x].trim());
                            }
                            if (x == 3)
                            {
                                mapSingle.put("buyClickNumber", singleSplit[x].trim());
                            }
                            if (x == 4)
                            {
                                mapSingle.put("buyClickPrice", singleSplit[x].trim());
                            }
                            if (x == 5)
                            {
                                mapSingle.put("appId", singleSplit[x].trim());
                            }
                        }
                        String buyDate = mapSingle.get("buyDate");
                        String productName = mapSingle.get("buyProductName");
                        String buyCost = mapSingle.get("buyCost");
                        String buyClickNumber = mapSingle.get("buyClickNumber");
                        String buyClickPrice = mapSingle.get("buyClickPrice");
                        String appId = mapSingle.get("appId");
                        WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
                        if (wxConfig != null)
                        {
                            String ddName = wxConfig.getProductName();
                            if (productName.equals(ddName))
                            {
                                buyPay.setBuyProductName(productName);
                            } else
                            {
                                buyPay.setBuyProductName(ddName);
                            }
                        }
                        buyPay.setBuyDate(buyDate);
                        buyPay.setBuyAppId(appId);
                        buyPay.setBuyCost(new BigDecimal(buyCost));
                        buyPay.setBuyClickNumber(Integer.parseInt(buyClickNumber));
                        buyPay.setBuyClickPrice(new BigDecimal(buyClickPrice));
                        buyPay.setInsertTime(new Timestamp(new Date().getTime()));
                        lists.add(buyPay);
                    }
                }
                buyPayMapper.insertBatch(lists);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    public int insert(BuyPay record) {

        int insert = buyPayMapper.insert(record);
        // int insert = 1;
        return insert;
    }


    public int updateByPrimaryKeySelective(BuyPay record) {
        String buyProductName = record.getBuyProductName();
        WxConfig wxConfig = wxConfigMapper.selectByProductName(buyProductName);
        if (wxConfig != null)
        {
            record.setBuyAppId(wxConfig.getDdappid());
        }
        record.setInsertTime(new Timestamp(new Date().getTime()));
        return buyPayMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("insertTime");
    }

    @Override
    public Class<BuyPay> getClassInfo() {
        return BuyPay.class;
    }

    @Override
    public boolean removeIf(BuyPay record, JSONObject searchData) {

//        if (existTimeFalse(record.getInsertTime(), searchData.getString("times"))) {
//            return true;
//        }
        return false;
    }

    public GetResult searchData(String beginDate, String endDate, String productName, GetParameter parameter) {
        String sql = "select * from buy_pay where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(productName))
        {
            List<BuyPay> buyPayList = buyPayMapper.selectAll();
            filterData(buyPayList, parameter);
            setDefaultSort(parameter);
            return template(buyPayList, parameter);
        }
        if (StringUtils.isNotBlank(beginDate))
        {
            SQL.append(" and buy_date >=" + "'").append(beginDate).append("'");
        }
        if (StringUtils.isNotBlank(endDate))
        {
            SQL.append(" and buy_date <=" + "'").append(endDate).append("'");
        }
        if (StringUtils.isNotBlank(productName))
        {
            WxConfig wxConfig = wxConfigMapper.selectByProductName(productName);
            if (wxConfig != null)
            {
                String ddAppId = wxConfig.getDdappid();
                if (StringUtils.isNotEmpty(ddAppId))
                {
                    SQL.append(" and buy_app_id =" + "'").append(ddAppId).append("'");
                }
            }
        }
        // System.out.println("拼接sql,耗时:" + (System.currentTimeMillis() - current) + "ms");
        List<BuyPay> buyPayList = buyPayMapper.selectSearch(SQL.toString());
        filterData(buyPayList, parameter);
        setDefaultSort(parameter);
        return template(buyPayList, parameter);

    }

    public int deleteSelective(BuyPay productInfo) {
        int count = buyPayMapper.deleteByPrimaryKey(productInfo.getBuyDate(), productInfo.getBuyAppId());
        return count;
    }
}
