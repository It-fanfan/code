package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class UserResourceService implements BaseService<ProductData>
{

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;

    @Override
    public List<ProductData> selectAll(GetParameter parameter)
    {
        ArrayList<ProductData> productDatas = new ArrayList<>();
        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
        for (WxConfig wxConfig : wxConfigs)
        {
            if (wxConfig.getProgramType() == 0)
            {
                ProductData productData = new ProductData();
                productData.setProductName(wxConfig.getProductName());
                //productData.setActiveUp();
                //productData.setRecharge();
                String ddAppId = wxConfig.getDdappid();
                LocalDate now = LocalDate.now();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                LocalDate beforeDate = now.minusDays(2);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String localDate = formatter.format(beforeDate);
                try
                {
                    //Date parse = format.parse(localDate);
                    MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, localDate);
                    if (minitjWx != null)
                    {
                        productData.setMinitjWx(minitjWx);
                        productData.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
                        BigDecimal adRevenue = productData.getAdRevenue();
                        Integer wxActive = productData.getMinitjWx().getWxActive();
                        BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                        productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                productDatas.add(productData);
            }
        }
        return productDatas;

    }

    public int insert(MinitjWx record)
    {

        return minitjWxMapper.insert(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

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


    public List<ProductData> searchData(String beginDate, String endDate, String productName)
    {
        ArrayList<ProductData> productDatas = new ArrayList<>();
        String sql = "select * from minitj_wx ";
        StringBuilder SQL = new StringBuilder();
        if (StringUtils.isNotBlank(beginDate))
        {
            SQL.append(sql).append("where wx_date >=" + "'" + beginDate + "'");

        }
        if (StringUtils.isNotBlank(endDate))
        {
            if (StringUtils.isNotBlank(beginDate))
            {
                SQL.append(" and wx_date <=" + "'" + endDate + "'");
            } else
            {
                SQL.append(sql).append("where wx_date <=" + "'" + endDate + "'");
            }

        }
        if (StringUtils.isNotBlank(productName))
        {
            WxConfig wxConfig = wxConfigMapper.selectByProductName(productName);
            if (wxConfig != null)
            {
                String ddAppId = wxConfig.getDdappid();
                if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate))
                {
                    SQL.append(sql).append("where wx_appid =" + "'" + ddAppId + "'");
                } else
                {
                    SQL.append(" and wx_appid =" + "'" + ddAppId + "'");
                }
            }
        }
        List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
        for (MinitjWx wxData : WxDatas)
        {
            ProductData productData = new ProductData();
            String appId = wxData.getWxAppid();
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
            String ddName = wxConfig.getProductName();
            productData.setProductName(ddName);
            productData.setMinitjWx(wxData);
            productData.setAdRevenue(wxData.getWxBannerIncome().add(wxData.getWxVideoIncome()));
            BigDecimal adRevenue = productData.getAdRevenue();
            Integer wxActive = productData.getMinitjWx().getWxActive();
            if (wxActive != 0)
            {
                BigDecimal divide = adRevenue.divide(new BigDecimal(wxActive), 4, ROUND_HALF_UP);
                productData.setActiveUp(divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            productDatas.add(productData);
        }

        return productDatas;
    }
}
