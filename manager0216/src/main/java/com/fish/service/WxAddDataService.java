package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author
 * @pragram: manger
 * @description: 微信广告数据明细service
 * @create:
 */
@Service
public class WxAddDataService implements BaseService<ProductData> {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Autowired
    CacheService cacheService;
    @Autowired
    ProductDataMapper productDataMapper;

    @Override
    public List<ProductData> selectAll(GetParameter parameter) {
        List<ProductData> productDatas = new ArrayList<>();
        String sql = "select * from minitj_wx where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || (StringUtils.isBlank(search.getString("ddappid"))&&StringUtils.isBlank(search.getString("times")))) {
            for (WxConfig wxConfig : wxConfigs) {
                ProductData productData = new ProductData();
                productData.setProductName(wxConfig.getProductName());
                String ddAppId = wxConfig.getDdappid();
                LocalDate now = LocalDate.now();
                LocalDate beforeDate = now.minusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String localDate = formatter.format(beforeDate);
                try {
                    MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, localDate);
                    if (minitjWx != null) {
                        productData.setMinitjWx(minitjWx);
                        productData.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
                        BigDecimal adRevenue = productData.getAdRevenue();
                        Integer wxActive = productData.getMinitjWx().getWxActive();
                        productData.setWxActive(wxActive);
                        BeanUtils.copyProperties(productData.getMinitjWx(), productData);

                        Integer wxVideoShow = minitjWx.getWxVideoShow();
                        BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
                        productData.setWxVideoShow(wxVideoShow);
                        productData.setWxVideoClickrate(minitjWx.getWxVideoClickrate());
                        productData.setWxVideoIncome(wxVideoIncome);

                        if (wxVideoShow != 0) {
                            productData.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                        } else {
                            productData.setVideoECPM(new BigDecimal(0));
                        }
                        Integer wxBannerShow = minitjWx.getWxBannerShow();
                        BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
                        productData.setWxBannerShow(wxBannerShow);
                        productData.setWxBannerClickrate(minitjWx.getWxBannerClickrate());
                        productData.setWxBannerIncome(wxBannerIncome);
                        if (wxBannerShow != 0) {
                            productData.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                        } else {
                            productData.setBannerECPM(new BigDecimal(0));
                        }
                        productData.setRevenueCount(adRevenue);
                        if (!checkObjFieldIsNull(productData.getMinitjWx())) {
                            productDatas.add(productData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            String ddAppId = search.getString("ddappid");
            String times = search.getString("times");
            if (StringUtils.isNotBlank(ddAppId)) {
                SQL.append(" and wx_appid =" + "'").append(ddAppId).append("'");
            }
            if (StringUtils.isNotBlank(times)) {
                String[] split = times.split("-");
                SQL.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                SQL.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
            }
            List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
            for (MinitjWx wxData : WxDatas) {

                ProductData productData = new ProductData();
                String appId = wxData.getWxAppid();
                WxConfig wxConfig = cacheService.getWxConfig(appId);
                if (wxConfig != null) {
                    productData.setProgramType(wxConfig.getProgramType());
                    String ddName = wxConfig.getProductName();
                    productData.setProductName(ddName);
                    productData.setMinitjWx(wxData);
                    productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
                    BigDecimal adRevenue = productData.getAdRevenue();
                    Integer wxActive = productData.getMinitjWx().getWxActive();
                    productData.setWxActive(wxActive);
                    BeanUtils.copyProperties(productData.getMinitjWx(), productData);

                    Integer wxVideoShow = wxData.getWxVideoShow();
                    BigDecimal wxVideoIncome = wxData.getWxVideoIncome();
                    productData.setWxVideoShow(wxVideoShow);
                    productData.setWxVideoClickrate(wxData.getWxVideoClickrate());
                    productData.setWxVideoIncome(wxVideoIncome);

                    if (wxVideoShow != 0) {
                        productData.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                    } else {
                        productData.setVideoECPM(new BigDecimal(0));
                    }
                    Integer wxBannerShow = wxData.getWxBannerShow();
                    BigDecimal wxBannerIncome = wxData.getWxBannerIncome();
                    productData.setWxBannerShow(wxBannerShow);
                    productData.setWxBannerClickrate(wxData.getWxBannerClickrate());
                    productData.setWxBannerIncome(wxBannerIncome);
                    if (wxBannerShow != 0) {
                        productData.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
                    } else {
                        productData.setBannerECPM(new BigDecimal(0));
                    }
                    productData.setRevenueCount(adRevenue);
                    BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                }
                try {
                    if (!checkObjFieldIsNull(productData.getMinitjWx())) {
                        productDatas.add(productData);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return productDatas;
    }

    //新增内容
    public int insert(ProductData productData) {
        int insertProgramData;
        productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
        insertProgramData = productDataMapper.insert(productData);
        return insertProgramData;
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(ProductData productData) {
        int update;
        productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
        update = productDataMapper.updateByPrimaryKey(productData);
        return update;
    }

    /**
     * java反射机制判断对象所有属性是否全部为空
     *
     * @param obj 对象参数
     * @return 返回属性名称
     */
    private boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {

        boolean flag = false;
        if (obj == null) {
            return true;
        } else {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) == null) {
                    flag = true;
                    return flag;
                }
            }
            return flag;
        }
    }

    public int insert(MinitjWx record) {

        return minitjWxMapper.insert(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("wxDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ProductData> getClassInfo() {
        return ProductData.class;
    }

    @Override
    public boolean removeIf(ProductData productData, JSONObject searchData) {
        return false;
    }


    public int deleteSelective(ProductData productInfo) {
        Date wxDate = productInfo.getWxDate();
        int count = productDataMapper.deleteByPrimaryKey(productInfo.getWxAppid(), wxDate);
        return count;
    }
}
