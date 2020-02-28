package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowOrders;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrdersService implements BaseService<ShowOrders>
{

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    CacheService cacheService;

    @Override
    //查询展示所有wxconfig信息
    public List<ShowOrders> selectAll(GetParameter parameter)
    {
        ArrayList<ShowOrders> shows = new ArrayList<>();
        List<Orders> orders = ordersMapper.selectAll();
        String className = OrdersService.class.getSimpleName().toLowerCase();
        Set<String> users = new HashSet<>();
        for (Orders order : orders)
            users.add(order.getDduid());
        for (Orders order : orders)
        {
            ShowOrders showOrders = new ShowOrders();
            String appId = order.getDdappid();
            Integer goodId = order.getDdgid();
            String dduId = order.getDduid();
            GoodsValue goodsValue = cacheService.getGoodsValue(goodId);
            if (goodsValue != null)
            {
                String goodnName = goodsValue.getDdname();
                String dddesc = goodsValue.getDddesc();
                showOrders.setGoodsName(goodnName);
                showOrders.setDdDesc(dddesc);
            }

            String userName = cacheService.getUserName(className, users, dduId);
            showOrders.setUserName(userName);
            WxConfig wxConfig = cacheService.getWxConfig(appId);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                String originName = wxConfig.getOriginName();
                Integer programType = wxConfig.getProgramType();
                if (programType != null)
                {
                    showOrders.setProductType(programType);
                }
                if (productName != null)
                {
                    showOrders.setProductName(productName);
                }
                if (originName != null)
                {
                    showOrders.setOriginName(originName);
                }
            }
            showOrders.setOrdersId(order.getDdid());
            showOrders.setOrdersUid(order.getDduid());
            showOrders.setOrdersOder(order.getDdorder());
            showOrders.setOrdersPrice(order.getDdprice());
            showOrders.setOrdersState(order.getDdstate());
            showOrders.setOrdersTime(order.getDdtime());
            showOrders.setOrders(order);
            shows.add(showOrders);
        }

        return shows;
    }

    public ShowOrders singleOrder(String record)
    {
        //查询订单产品信息
        Orders order = ordersMapper.selectByPrimaryKey(record);
        ShowOrders showOrders = new ShowOrders();
        String appId = order.getDdappid();
        Integer goodId = order.getDdgid();
        String dduId = order.getDduid();
        order.setDdstate(1);
        GoodsValue goodsValue = cacheService.getGoodsValue(goodId);
        String goodName = goodsValue.getDdname();
        String ddDesc = goodsValue.getDddesc();
        showOrders.setDdDesc(ddDesc);
        String userName = cacheService.getUserName(dduId);
        WxConfig wxConfig = cacheService.getWxConfig(appId);
        if (wxConfig != null)
        {
            String productName = wxConfig.getProductName();
            String originName = wxConfig.getOriginName();

            if (productName != null)
            {
                showOrders.setProductName(productName);
            }
            if (originName != null)
            {
                showOrders.setOriginName(originName);
            }

        }
        if (goodName != null)
        {
            showOrders.setGoodsName(goodName);

        }
        if (userName != null)
        {
            showOrders.setUserName(userName);
        }
        showOrders.setOrdersId(order.getDdid());
        showOrders.setOrdersUid(order.getDduid());
        showOrders.setOrdersOder(order.getDdorder());
        showOrders.setOrdersPrice(order.getDdprice());
        showOrders.setOrdersState(order.getDdstate());
        showOrders.setOrdersTime(order.getDdtime());
        showOrders.setOrders(order);

        //        if (showOrders != null) {
        //            return showOrders;
        //        }
        return null;
    }

    //新增展示所有产品信息
    public int insert(Orders record)
    {


        int insert1 = 0;
        //新增产品信息
        insert1 = ordersMapper.insert(record);

        return insert1;
    }


    //更新产品信息
    public int updateByPrimaryKeySelective(Orders record)
    {

        return ordersMapper.updateByPrimaryKeySelective(record);
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
    public Class<ShowOrders> getClassInfo()
    {
        return ShowOrders.class;
    }

    @Override
    public boolean removeIf(ShowOrders orders, JSONObject searchData)
    {

        String times = searchData.getString("times");
        Date[] parse = XwhTool.parseDate(times);
        if (times != "" && times.length() != 0)
        {
            if (orders.getOrders().getDdtime().before(parse[0]) || orders.getOrders().getDdtime().after(parse[1]))
            {
                return true;
            }
        }
        if (existValueFalse(searchData.getString("productName"), orders.getProductName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("tradeNumber"), orders.getOrders().getDdid()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("uid"), orders.getOrders().getDduid()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), orders.getUserName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("payState"), orders.getOrders().getDdstate()))
        {
            return true;
        }
        //        if (existValueFalse(searchData.get("clear"), wxConfig.getClearCompany())) {
        //            return true;
        //        }
        return existValueFalse(searchData.getString("openID"), orders.getOrders().getDdoid());
    }
}
