package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowPayStatistic;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class PayStatisticService implements BaseService<ShowPayStatistic>
{
    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    CacheService cacheService;


    @Override
    //查询展示所有付费信息
    public List<ShowPayStatistic> selectAll(GetParameter parameter)
    {
        List<ShowPayStatistic> payStatistics = new ArrayList<>();
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || (search.getString("start").isEmpty() && search.getString("end").isEmpty()))
        {
            long current=System.currentTimeMillis();    //当前时间毫秒数
            long zeroT=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();  //今天零点零分零秒的毫秒数
            String zero = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zeroT);
            String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(current);
            String cur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(current);
            String sql ="SELECT ddAppId FROM orders WHERE ddTrans >='"+zero+"' AND ddTrans <='"+end+"' AND ddOrder !='null' GROUP BY ddAppId";
            List<Orders> pays = ordersMapper.selectBySQL(sql);
            for (Orders orders : pays) {
                ShowPayStatistic payStatistic = new ShowPayStatistic();
                String ddappid = orders.getDdappid();
                String payMoney ="SELECT SUM(ddPrice)AS ddPrice ,COUNT(DISTINCT ddUid)AS ddGid  FROM orders WHERE ddTrans >='"+zero+"' AND ddTrans <='"+end+"' AND ddOrder !='null' AND  ddAppId ='"+ddappid+"'";
                Orders payMoneys = ordersMapper.selectResSingle(payMoney);
                BigDecimal countPrice = payMoneys.getDdprice();
                Integer ddgid = payMoneys.getDdgid();
                int up =countPrice.intValue()/ ddgid;
                payStatistic.setPayMoney(countPrice.intValue());
                payStatistic.setPayUsers(ddgid);
                payStatistic.setPayUp(up+"");
                payStatistic.setDdappid(ddappid);
                WxConfig wxConfig = cacheService.getWxConfig(ddappid);
                String productName = wxConfig.getProductName();
                Integer programType = wxConfig.getProgramType();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date=null;
                try {
                     date = simpleDateFormat.parse(cur);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                payStatistic.setProductName(productName);
                payStatistic.setProductType(programType);
                payStatistic.setDdtrans(date);
                payStatistics.add(payStatistic);
            }
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String start = search.getString("start");
            String end = search.getString("end");

//            String format0 = format.format(dateStart);
//            String format1 = format.format(dateEnd);
            String sql ="SELECT DATE(ddTrans) AS ddTrans FROM orders WHERE ddTrans >='"+start+"' AND ddTrans <='"+end+"' AND ddOrder !='null' GROUP BY DATE(ddTrans) ORDER BY ddTrans ASC";
            List<Orders> pays = ordersMapper.selectBySQL(sql);
            for (Orders pay : pays) {
                Date ddtrans = pay.getDdtrans();
                String time = format.format(ddtrans);
                String payAppId ="SELECT ddAppId FROM orders WHERE DATE(ddTrans) ='"+time+"' AND ddOrder !='null' GROUP BY ddAppId";
                List<Orders> appIds = ordersMapper.selectBySQL(payAppId);
                for (Orders appId : appIds) {
                    ShowPayStatistic payStatistic = new ShowPayStatistic();
                    String ddappid = appId.getDdappid();
                    String payMoney ="SELECT SUM(ddPrice)AS ddPrice ,COUNT(DISTINCT ddUid)AS ddGid  FROM orders WHERE DATE(ddTrans) ='"+time+"'  AND ddOrder !='null' AND  ddAppId ='"+ddappid+"'";

                    Orders payMoneys = ordersMapper.selectResSingle(payMoney);
                    BigDecimal countPrice = payMoneys.getDdprice();
                    Integer ddgid = payMoneys.getDdgid();
                    int up =countPrice.intValue()/ ddgid;
                    payStatistic.setPayMoney(countPrice.intValue());
                    payStatistic.setPayUsers(ddgid);
                    payStatistic.setPayUp(up+"");
                    payStatistic.setDdappid(ddappid);
                    WxConfig wxConfig = cacheService.getWxConfig(ddappid);
                    String productName = wxConfig.getProductName();
                    Integer programType = wxConfig.getProgramType();

                    payStatistic.setProductName(productName);
                    payStatistic.setProductType(programType);
                    payStatistic.setDdtrans(ddtrans);
                    String ddAppId = search.getString("productName");
                    String payState = search.getString("payState");
                    if(ddAppId !=null && ddAppId.length()>0 ){
                        if(payState !=null && payState.length()>0){
                            if(ddAppId.equals(ddappid) && (Integer.valueOf(payState).equals(programType))){
                                payStatistics.add(payStatistic);
                            }
                        }else {
                            if(ddAppId.equals(ddappid)){
                                payStatistics.add(payStatistic);
                            }
                        }
                    }else {
                        if(payState !=null && payState.length()>0){
                            if(Integer.valueOf(payState).equals(programType)){
                                payStatistics.add(payStatistic);
                            }
                        }else {
                            payStatistics.add(payStatistic);
                        }
                    }
                }
            }

        }

        return payStatistics;
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("productName");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ShowPayStatistic> getClassInfo()
    {
        return ShowPayStatistic.class;
    }

    @Override
    public boolean removeIf(ShowPayStatistic showPayStatistic, JSONObject searchData)
    {
//        if (existValueFalse(searchData.getString("productName"), orders.getOrders().getDdappid()))
//        {
//            return true;
//        }
        return false;
    }
}
