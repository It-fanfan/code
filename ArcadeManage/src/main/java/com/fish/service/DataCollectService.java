package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.DataCollect;
import com.fish.dao.third.model.MinitjWx;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.utils.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataCollectService implements BaseService<DataCollect>
{

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;
    @Autowired
    BuyPayMapper buyPayMapper;

    private Integer productCount = 0;//产品数量
    private Integer newCount = 0;//新增数量
    private Integer activeCount = 0;//活跃数量
    private BigDecimal revenueCount = new BigDecimal(0);//总收入
    private BigDecimal adRevenueCount = new BigDecimal(0);//广告总收入
    private BigDecimal videoIncomeCount = new BigDecimal(0); //视频总收入
    private BigDecimal bannerIncomeCount = new BigDecimal(0); //banner总收入
    private BigDecimal buyCostCount = new BigDecimal(0);//买量支出
    private Integer shareUserCount = 0; //分享人数
    private Integer shareCount = 0; //分享次数
    private BigDecimal shareRateCount = new BigDecimal(0); //分享率

    //缓存数据
    private static List<DataCollect> dataCollects;

    @Override
    public List<DataCollect> selectAll(GetParameter parameter)
    {
        // List<DataCollect> list = (List<DataCollect>) RedisUtils.getList("dataList");

//        List<DataCollect> list = (List<DataCollect>) RedisUtils.getList("dataList");
//        String start = "", end = "";
//        Date startDate = parseDate(start);
//        Date endDate = parseDate(end);
//        List<DataCollect> collect = list.stream().filter(x -> x.getWxDate().before(startDate) && x.getWxDate().after(endDate)).collect(Collectors.toList());
        if (dataCollects == null)
        {
            flushAll(parameter);
        } else
        {
            for (DataCollect dataCollect : dataCollects)
            {
                Date wxDate = dataCollect.getWxDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(wxDate);
                BigDecimal countBuyCost = buyPayMapper.selectCountBuyCost(format);
                dataCollect.setBuyPay(countBuyCost);
            }
        }
        return dataCollects;
    }

    /**
     * 解析日期
     *
     * @param str 字符串
     * @return 时间
     */
    private Date parseDate(String str)
    {
        try
        {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(str);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return new Date();
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
    public Class<DataCollect> getClassInfo()
    {
        return DataCollect.class;
    }

    @Override
    public boolean removeIf(DataCollect productData, JSONObject searchData)
    {
        return false;
    }

    public GetResult searchData(String beginDate, String endDate, GetParameter parameter)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DataCollect> lists = new ArrayList<>();
        for (DataCollect dataCollect : Objects.requireNonNull(dataCollects))
        {
            Date wxDate = dataCollect.getWxDate();
            String format = sdf.format(wxDate);
            if (StringUtils.isNotEmpty(beginDate) && StringUtils.isNotEmpty(endDate))
            {
                int start = format.compareTo(beginDate);
                int end = format.compareTo(endDate);
                if (start >= 0 && end <= 0)
                {
                    lists.add(dataCollect);
                }
            }
            if (StringUtils.isNotEmpty(beginDate) && StringUtils.isEmpty(endDate))
            {
                int start = format.compareTo(beginDate);
                if (start >= 0)
                {
                    lists.add(dataCollect);
                }
            }
            if (StringUtils.isEmpty(beginDate) && StringUtils.isNotEmpty(endDate))
            {
                int end = format.compareTo(endDate);
                if (end <= 0)
                {
                    lists.add(dataCollect);
                }
            }
            if (StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate))
            {
                lists.add(dataCollect);
            }
        }
        filterData(lists, parameter);
        setDefaultSort(parameter);
        return template(lists, parameter);
    }

    public int flushAll(GetParameter parameter)
    {
        dataCollects = new ArrayList<>();

        List<String> dates = minitjWxMapper.dateCash();
        //遍历fc数据日期
        for (String date : dates)
        {
            DataCollect dataCollect = new DataCollect();
            List<WxConfig> wxConfigs = wxConfigMapper.selectAllGames();
            productCount = wxConfigs.size();
            //遍历wx_config中AppId
            for (WxConfig wxConfig : wxConfigs)
            {
                String ddAppId = wxConfig.getDdappid();
                MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, date);
                BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(date, ddAppId);
                if (minitjWx != null)
                {
                    if (buyPay != null)
                    {
                        BigDecimal buyCost = buyPay.getBuyCost();
                        buyCostCount = buyCostCount.add(buyCost);
                    }
                    Integer wxNew = minitjWx.getWxNew();
                    newCount += wxNew;
                    Integer wxActive = minitjWx.getWxActive();
                    activeCount += wxActive;
                    BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
                    bannerIncomeCount = bannerIncomeCount.add(wxBannerIncome);
                    BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
                    videoIncomeCount = videoIncomeCount.add(wxVideoIncome);

                    Integer wxShareUser = minitjWx.getWxShareUser();
                    shareUserCount += wxShareUser;
                    Integer wxShareCount = minitjWx.getWxShareCount();
                    shareCount += wxShareCount;
                    BigDecimal wxShareRate = minitjWx.getWxShareRate();
                    shareRateCount = shareRateCount.add(wxShareRate);
                }
            }
            //数据求和后拼接赋值
            adRevenueCount = videoIncomeCount.add(bannerIncomeCount);
            dataCollect.setProductCount(productCount);
            dataCollect.setNewCount(newCount);
            dataCollect.setActiveCount(activeCount);
            dataCollect.setBuyPay(buyCostCount);
            dataCollect.setVideoIncomeCount(videoIncomeCount);
            dataCollect.setBannerIncomeCount(bannerIncomeCount);
            dataCollect.setAdRevenueCount(adRevenueCount);
            dataCollect.setShareUserCount(shareUserCount);
            dataCollect.setShareCount(shareCount);
            if (productCount != 0)
            {
                dataCollect.setShareRateCount(shareRateCount.divide(new BigDecimal(productCount), 2, RoundingMode.HALF_UP));
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date localDate = null;
            try
            {
                localDate = format.parse(date);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            dataCollect.setWxDate(localDate);
            System.out.println(date);
            dataCollects.add(dataCollect);

            productCount = 0;
            newCount = 0;
            activeCount = 0;
            revenueCount = new BigDecimal(0);
            adRevenueCount = new BigDecimal(0);
            videoIncomeCount = new BigDecimal(0);
            bannerIncomeCount = new BigDecimal(0);
            buyCostCount = new BigDecimal(0);
            shareUserCount = 0;
            shareCount = 0;
            shareRateCount = new BigDecimal(0);
        }
        if (dataCollects.size() > 0)
        {
            return 1;
        } else
        {
            return 0;
        }
    }

}
