package com.fish.service;

import com.alibaba.fastjson.JSONObject;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class ProductDataService implements BaseService<ProductData>
{

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public List<ProductData> selectAll(GetParameter parameter)
    {
        List<ProductData> productDatas = new ArrayList<>();
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        for (WxConfig wxConfig : wxConfigs)
        {
            ProductData productData = new ProductData();
            if (wxConfig.getProgramType() == 0)
            {
                productData.setProductName(wxConfig.getProductName());
                String ddAppId = wxConfig.getDdappid();
                LocalDate now = LocalDate.now();
                LocalDate beforeDate = now.minusDays(1);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String localDate = formatter.format(beforeDate);
                try
                {
                    MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, localDate);
                    if (minitjWx != null)
                    {
                        productData.setMinitjWx(minitjWx);
                        productData.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
                        BigDecimal adRevenue = productData.getAdRevenue();
                        Integer wxActive = productData.getMinitjWx().getWxActive();
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                        productData.setActiveUp(divide);
                        String wxRegJson = minitjWx.getWxRegJson();
                        JSONObject jsonObject = JSONObject.parseObject(wxRegJson);
                        Integer other = (Integer) jsonObject.get("其他");
                        if (other != null)
                        {
                            productData.setWxRegOther(other);
                        } else
                        {
                            productData.setWxRegOther(0);
                        }
                        BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                        if (!checkObjFieldIsNull(productData.getMinitjWx()))
                        {
                            productDatas.add(productData);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return productDatas;

    }

    /**
     * java反射机制判断对象所有属性是否全部为空
     *
     * @param obj 对象参数
     * @return 返回属性名称
     */
    private boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException
    {

        boolean flag = false;
        if (obj == null)
        {
            return true;
        } else
        {
            for (Field f : obj.getClass().getDeclaredFields())
            {
                f.setAccessible(true);
                if (f.get(obj) == null)
                {
                    flag = true;
                    return flag;
                }
            }
            return flag;
        }
    }

    public int insert(MinitjWx record)
    {

        return minitjWxMapper.insert(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("wxDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ProductData> getClassInfo()
    {
        return ProductData.class;
    }

    @Override
    public boolean removeIf(ProductData productData, JSONObject searchData)
    {
        return false;
    }

    /**
     * 页面搜索查询
     *
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param productName 产品名称
     * @param parameter   参数
     * @return 返回结果
     */
    public GetResult searchProductData(String beginDate, String endDate, String productName, GetParameter parameter)
    {
        //System.out.println("开始 :" +System.currentTimeMillis());
        ArrayList<ProductData> searchDatas = new ArrayList<>();
        String sql = "select * from minitj_wx where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(productName))
        {
            List<ProductData> productData = selectAll(parameter);
            filterData(productData, parameter);
            setDefaultSort(parameter);
            return template(productData, parameter);
        }
        if (StringUtils.isNotBlank(beginDate))
        {
            SQL.append(" and wx_date >=" + "'").append(beginDate).append("'");
        }
        if (StringUtils.isNotBlank(endDate))
        {
            SQL.append(" and wx_date <=" + "'").append(endDate).append("'");
        }
        if (StringUtils.isNotBlank(productName))
        {
            WxConfig wxConfig = wxConfigMapper.selectByProductName(productName);
            if (wxConfig != null)
            {
                String ddAppId = wxConfig.getDdappid();
                if (StringUtils.isNotEmpty(ddAppId))
                {
                    SQL.append(" and wx_appid =" + "'").append(ddAppId).append("'");
                }
            }
        }
        // System.out.println("拼接sql,耗时:" + (System.currentTimeMillis() - current) + "ms");
        List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
        for (MinitjWx wxData : WxDatas)
        {
            ProductData productData = new ProductData();
            String appId = wxData.getWxAppid();
            // WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
            WxConfig wxConfig = cacheService.getWxConfig(appId);
            if (wxConfig != null)
            {
                String ddName = wxConfig.getProductName();
                productData.setProductName(ddName);
                productData.setMinitjWx(wxData);
                productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
                BigDecimal adRevenue = productData.getAdRevenue();
                Integer wxActive = productData.getMinitjWx().getWxActive();
                try
                {
                    if (!wxActive.equals(0))
                    {
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                        productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    String wxRegJson = productData.getMinitjWx().getWxRegJson();
                    JSONObject jsonObject = null;
                    jsonObject = JSONObject.parseObject(wxRegJson);
                    if (jsonObject != null)
                    {
                        Integer other = (Integer) jsonObject.get("其他");
                        if (other != null)
                        {
                            productData.setWxRegOther(other);
                        } else
                        {
                            productData.setWxRegOther(0);
                        }
                    } else
                    {
                        productData.setWxRegOther(0);
                    }
                    BeanUtils.copyProperties(productData.getMinitjWx(), productData);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                searchDatas.add(productData);
            }
        }
        filterData(searchDatas, parameter);
        setDefaultSort(parameter);
        return template(searchDatas, parameter);
    }
}
