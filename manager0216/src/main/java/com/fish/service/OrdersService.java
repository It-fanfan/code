package com.fish.service;

import cn.hutool.Hutool;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowOrders;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.XwhTool;
import com.fish.utils.log4j.Log4j;
import com.fish.utils.tool.CmTool;
import com.fish.utils.tool.SignatureAlgorithm;
import com.fish.utils.tool.XMLHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.fish.utils.tool.CmTool.createNonceStr;

@Service
public class OrdersService implements BaseService<ShowOrders>
{
    // 签名类型
    private static final String SIGN_TYPE = "MD5";
    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    CacheService cacheService;
    @Autowired
    BaseConfig baseConfig;

    @Override
    //查询展示所有订单信息
    public List<ShowOrders> selectAll(GetParameter parameter)
    {
        ArrayList<ShowOrders> shows = new ArrayList<>();
        List<Orders> orders;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty())
        {
            orders = ordersMapper.selectAll();
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format0 = format.format(parse[0]);
            String format1 = format.format(parse[1]);
            orders = ordersMapper.selectByTimes(format0, format1);
        }
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

    public int singleOrder(String appId, String uid, String orderId)
    {
        //查询订单产品信息
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        WxConfig wxConfig = cacheService.getWxConfig(appId);
        String ddMchId = wxConfig.getDdmchid();
        Map<String, String> stringStringMap = searchPayOrder(appId, ddMchId, orderId);
        boolean status = orderIsSuccess(stringStringMap);
        LOGGER.info("补单状态status :"+status+"-appId :"+appId+"-orderId :"+orderId);
        if (status)
        {
            order.setDdtrans(null);
            ordersMapper.updateByPrimaryKey(order);
            String supplementUrl = baseConfig.getSupplementUrl();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version","2.2.2");
            jsonObject.put("appId",appId);
            jsonObject.put("uid",uid);
            jsonObject.put("orderid",orderId);
            //System.out.println(jsonObject.toJSONString());
            String result = HttpUtil.post(supplementUrl, jsonObject.toString());
            JSONObject jsonResult = JSONObject.parseObject(result);
            if("success".equals(jsonResult.getString("result"))){
                String ddOrder = stringStringMap.get("transaction_id");
                order.setDdstate(1);
                order.setDdorder(ddOrder);
                ordersMapper.updateByPrimaryKeySelective(order);
                return 200;
            }
        }
        return  404;
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
        if (existValueFalse(searchData.getString("productName"), orders.getOrders().getDdappid()))
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

    /**
     * 查询微信支付订单
     *
     * @param orderId 订单号
     */
    private Map<String, String> searchPayOrder(String ddAppId, String ddMchId, String orderId)
    {

        // 查询订单在数据库中是否存在
        // 封装查询订单参数
        Map<String, String> searchOrder_map = new HashMap<>();
        searchOrder_map.put("appid", ddAppId);
        searchOrder_map.put("mch_id", ddMchId);
        searchOrder_map.put("out_trade_no", orderId);
        searchOrder_map.put("nonce_str", createNonceStr());
        searchOrder_map.put("sign_type", SIGN_TYPE);
        WxConfig wxConfig = cacheService.getWxConfig(ddAppId);
        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(wxConfig.getDdkey(), searchOrder_map);
        String searchOrderXml = signatureAlgorithm.getSignXml();
        try
        {
            // 从微信平台里查询支付订单
            String searchResultXml = CmTool.sendHttps(searchOrderXml, baseConfig.getWxQueryUrl(), OrdersService.class.getResource("/").getPath() + "static/"+wxConfig.getDdp12(), wxConfig.getDdp12password());
            XMLHandler parse = XMLHandler.parse(searchResultXml);
            return parse.getXmlMap();
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 订单是否成功
     *
     * @param orderMap 订单回调内容
     * @return 结果
     */
    private boolean orderIsSuccess(Map<String, String> orderMap)
    {
        return existResult(orderMap, "result_code") && existResult(orderMap, "return_code") && existResult(orderMap, "trade_state");
    }

    /**
     * 检测是否匹配
     *
     * @param map 内容
     * @param key 查询参数
     * @return 是否匹配
     */
    private static boolean existResult(Map<String, String> map, String key)
    {
        String resultCode = map.get(key);
        return "success".equalsIgnoreCase(resultCode);
    }
}
