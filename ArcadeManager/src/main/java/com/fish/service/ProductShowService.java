package com.fish.service;

import com.fish.dao.primary.mapper.ProductEarnMapper;
import com.fish.dao.primary.mapper.ProductRemainMapper;
import com.fish.dao.primary.model.ProductEarn;
import com.fish.dao.primary.model.ProductRemain;
import com.fish.dao.primary.model.ProductShow;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductShowService implements BaseService<ProductShow> {

    @Autowired
    ProductEarnMapper earnMapper;
    @Autowired
    ProductRemainMapper remainMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getSort() != null)
            return;
        parameter.setSort("appId");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ProductShow> getClassInfo() {
        return ProductShow.class;
    }

    @Override
    public boolean removeIf(ProductShow productShow, Map<String, String> searchData) {
        String productName = searchData.get("searProductName");
        String platform = searchData.get("searPlatForm");
        System.out.println(productName + "," + platform + "," + productShow.getAppId());
        String times = searchData.get("times");
        if (existTimeFalse(productShow.getNewDay(), times)) {
            return true;
        }
        if (existValueFalse(platform, productShow.getAppId())) {
            return true;
        }
        return existValueFalse(productName, productShow.getAppId());
    }

    private String getKey(Date newDate, String appId) {
        return new SimpleDateFormat("yyyyMMdd").format(newDate) + "-" + appId;
    }

    @Override
    public List<ProductShow> selectAll(GetParameter parameter) {
        List<ProductRemain> remains = remainMapper.selectAll();
        List<ProductEarn> earns = earnMapper.selectAll();
        //HashSet<String> acList = remainMapper.getAcUserInfo(date, appId);
        Map<String, ProductShow> cache = new HashMap<>();
        if (remains != null)
            remains.forEach(element -> {
                ProductShow show = new ProductShow();
                show.setAppId(element.getAppId());
                show.setNewDay(element.getNewDay());
                show.setNewUserCount(element.getNewUserCount());
                show.setAcUserCount(element.getAcUserCount());
                show.setRemain1(element.getRemain1());
                show.setRemain3(element.getRemain3());
                String key = getKey(show.getNewDay(), show.getAppId());
                cache.put(key, show);
            });
        if (earns != null) {
            earns.forEach(element -> {
                String key = getKey(element.getNewDay(), element.getAppId());
                ProductShow show = cache.get(key);
                if (show == null) {
                    show = new ProductShow();
                    show.setAppId(element.getAppId());
                    show.setNewDay(element.getNewDay());
                }
                show.setAdRevenue(element.getAdRevenue());
                if (element.getInRevenue() != null) {
                    BigDecimal decimal = element.getInRevenue();
                    show.setInRevenue(decimal.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
                }
                show.setEnterCount(element.getEnterCount());
                show.setInArpu(element.getInArpu());
//                Integer acUserCount = cache.get(key).getAcUserCount();
//                Integer onlineInt = Integer.parseInt(element.getOnlineAvg());
//                Integer onlineAvg =onlineInt/acUserCount;
//                show.setOnlineAvg(onlineAvg.toString());
                show.setOnlineAvg(element.getOnlineAvg());
                show.setPayUserCount(element.getPayUserCount());

                cache.put(key, show);
            });
        }
        return new ArrayList<>(cache.values());
    }
}
