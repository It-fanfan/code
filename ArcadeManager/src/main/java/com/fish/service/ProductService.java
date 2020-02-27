package com.fish.service;

import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.utils.BaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Autowired
    ProductProfileMapper productProfileMapper;
    @Autowired
    ProductRemainMapper productRemainMapper;
    @Autowired
    ProductShowMapper productShowMapper;
    @Autowired
    ProductEarnMapper productEarnMapper;
    @Autowired
    ProductRemain productRemain;
    @Autowired
    ProductShow productShow;

    //查询展示所有产品信息
    public List<ProductInfo> selectAllProduct() {
        return productInfoMapper.selectAll();
    }

    //新增产品信息
    public Integer insertProductInfo(ProductInfo productInfo) {
        int count = productInfoMapper.insert(productInfo);
        return count;
    }

    //修改产品信息
    public Integer modifyProductInfo(ProductInfo productInfo) {
        int count = productInfoMapper.updateProductInfo(productInfo);
        return count;
    }

    public int deleteProductInfo(ProductInfo productInfo) {
        int counts = productInfoMapper.deleteProductInfo(productInfo);
        return counts;
    }

    //根据时间，产品名称，平台，查询展示信息
    public List<ProductShow> showSearchProductProfile(Date searchTime, String productName, String platform) {
        List<ProductShow> lists = productShowMapper.showSearchProductProfile(searchTime, productName, platform);
        return lists;
    }

    //查询展示所有产品概况信息
    public List<ProductShow> showProductProfile() {
        List<ProductShow> lists = productShowMapper.showProductProfile();
        return lists;
    }

    //获取json信息生成product_frofile表
    public int insertProfile(ProductJson productJson) {
        int jsonCount = productProfileMapper.insertJson(productJson);
        return jsonCount;
    }

    private String getKey(Date newDate, String appId) {
        return new SimpleDateFormat("yyyyMMdd").format(newDate) + "-" + appId;
    }

    //通过Json获取产品概况信息
    public void insertRemain() {
        Map<String, ProductEarn> earnCache = new HashMap<>();
        Map<String, ProductRemain> remainCache = new HashMap<>();
        //查询所有进入次数
        List<ProductEarn> inNumbers = productEarnMapper.selectInNumbers();
        for (ProductEarn inNumber : inNumbers) {
            if (inNumber != null) {
                String inNumberKey = getKey(inNumber.getNewDay(), inNumber.getAppId());
                earnCache.put(inNumberKey, inNumber);
            }
            // productEarnMapper.insertEarnData(inNumber);//插入所有进入次数
        }

        //查询付费用户数
        List<ProductEarn> payUsers = productEarnMapper.selectPayUser();
        for (ProductEarn payUser : payUsers) {
            if (payUser != null) {
                String payUserKey = getKey(payUser.getNewDay(), payUser.getAppId());
                earnCache.put(payUserKey, payUser);
            }
            //productEarnMapper.insertEarnData(payUser);//插入付费用户数
        }

        //查询内购收益及内购Arpu
        List<ProductEarn> inCosts = productEarnMapper.selectInCost();
        inCosts.forEach(cost -> {
            BigDecimal inRevenue = cost.getInRevenue();
            Date newDay = cost.getNewDay();
            String appId = cost.getAppId();
            Integer activeUsers = productProfileMapper.searchActiveUser(newDay, appId);
            BigDecimal activeUserCount = new BigDecimal(Integer.toString(activeUsers));
            BigDecimal inArpu = inRevenue.divide(activeUserCount, 2, BigDecimal.ROUND_HALF_UP);
            cost.setInArpu(inArpu);
        });
        for (ProductEarn inCost : inCosts) {
            if (inCost != null) {
                String inCostKey = getKey(inCost.getNewDay(), inCost.getAppId());
                earnCache.put(inCostKey, inCost);
            }
            // productEarnMapper.insertEarnData(inCost);//插入内购收益
        }

        //查询人均在线时长
        List<ProductEarn> onlineAvgs = productEarnMapper.selectOnlineAvg();
        for (ProductEarn onlineAvg : onlineAvgs) {
            if (onlineAvg != null) {
                String onlineAvgKey = getKey(onlineAvg.getNewDay(), onlineAvg.getAppId());
                earnCache.put(onlineAvgKey, onlineAvg);
            }
            //插入人均在线时长
            // productEarnMapper.insertEarnData(onlineAvg);//插入所有进入次数
        }
        for (ProductEarn value : earnCache.values()) {
            productEarnMapper.insertEarnData(value);
        }
        //查询所有新增用户并插入
        List<ProductRemain> addUsers = productRemainMapper.selectAddUser();
        for (ProductRemain addUser : addUsers) {
            if (addUser != null) {
                String addUserKey = getKey(addUser.getNewDay(), addUser.getAppId());
                remainCache.put(addUserKey, addUser);
            }
            //productRemainMapper.insertData(addUser);
        }
        //查询所有活跃用户并插入
        List<ProductRemain> activeUsers = productRemainMapper.selectActiveUser();
        for (ProductRemain activeUser : activeUsers) {
            if (activeUser != null) {
                String activeUserKey = getKey(activeUser.getNewDay(), activeUser.getAppId());
                remainCache.put(activeUserKey, activeUser);
            }
            //productRemainMapper.insertData(activeUser);
        }
        //计算1日留存，3日留存并进行数据插入
        List<ProductRemain> lists = productRemainMapper.selectAllTimeAndAppId();
        lists.forEach(productRemainY -> {
            if (productRemainY != null) {
                Date searchTime = productRemainY.getNewDay();
                String appId = productRemainY.getAppId();
                //获取新增用户数
                HashSet<String> newList = productRemainMapper.getNewUserInfo(searchTime, appId);
                Integer firstRemains = remainCal(searchTime, appId, newList, 1);
                productRemainY.setRemain1(firstRemains);
                Integer thirdRemains = remainCal(searchTime, appId, newList, 3);
                productRemainY.setRemain3(thirdRemains);

                String onlineAvgKey = getKey(productRemainY.getNewDay(), productRemainY.getAppId());
                remainCache.put(onlineAvgKey, productRemainY);
            }
            // productRemainMapper.insertData(productRemainY);
        });
        for (ProductRemain remainValue : remainCache.values()) {
            productRemainMapper.insertData(remainValue);
        }

    }

    /**
     * 根据时间，appId，留存天数，
     *
     * @param searchTime 时间
     * @param appId      appId
     * @param newList    新增用户集合
     * @param remain     留存天数
     * @return 计算留存量
     */
    private Integer remainCal(Date searchTime, String appId, HashSet<String> newList, int remain) {
        LocalDate now = LocalDateTime.ofInstant(searchTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        //设置偏移时间
        now = now.plusDays(remain);
        //获取留存天活跃用户数
        Date date = Date.from(now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        HashSet<String> acList = productRemainMapper.getAcUserInfo(date, appId);
        AtomicInteger retention = new AtomicInteger();
        //进行计算留存数
        newList.forEach(userId -> {
            if (acList.contains(userId))
                retention.incrementAndGet();
        });
        return retention.intValue();
    }

}

