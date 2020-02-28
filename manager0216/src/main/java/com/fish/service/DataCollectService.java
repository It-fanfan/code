package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.DataCollect;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.utils.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DataCollectService implements BaseService<DataCollect>, Runnable {

    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;
    @Autowired
    BuyPayMapper buyPayMapper;
    @Autowired
    ProductDataMapper productDataMapper;
    private Integer productCount = 0;//产品数量
    private Integer newCount = 0;//新增数量
    private Integer activeCount = 0;//活跃总人数
    private BigDecimal revenueCount = new BigDecimal(0);//总收入
    private BigDecimal adRevenueCount = new BigDecimal(0);//广告总收入
    private BigDecimal videoIncomeCount = new BigDecimal(0); //视频总收入
    private BigDecimal bannerIncomeCount = new BigDecimal(0); //banner总收入
    private BigDecimal buyCostCount = new BigDecimal(0);//买量支出
    private Integer shareUserCount = 0; //分享人数
    private Integer shareCount = 0; //分享次数
    private BigDecimal shareRateCount = new BigDecimal(0); //分享率

    private Integer productCountp = 0;//产品数量
    private Integer newCountp = 0;//新增数量
    private Integer activeCountp = 0;//活跃总人数
    private BigDecimal revenueCountp = new BigDecimal(0);//总收入
    private BigDecimal adRevenueCountp = new BigDecimal(0);//广告总收入
    private BigDecimal videoIncomeCountp = new BigDecimal(0); //视频总收入
    private BigDecimal bannerIncomeCountp = new BigDecimal(0); //banner总收入
    private BigDecimal buyCostCountp = new BigDecimal(0);//买量支出
    private Integer shareUserCountp = 0; //分享人数
    private Integer shareCountp = 0; //分享次数
    private BigDecimal shareRateCountp = new BigDecimal(0); //分享率


    private Integer productCountg = 0;//产品数量
    private Integer newCountg = 0;//新增数量
    private Integer activeCountg = 0;//活跃总人数
    private BigDecimal revenueCountg = new BigDecimal(0);//总收入
    private BigDecimal adRevenueCountg = new BigDecimal(0);//广告总收入
    private BigDecimal videoIncomeCountg = new BigDecimal(0); //视频总收入
    private BigDecimal bannerIncomeCountg = new BigDecimal(0); //banner总收入
    private BigDecimal buyCostCountg = new BigDecimal(0);//买量支出
    private Integer shareUserCountg = 0; //分享人数
    private Integer shareCountg = 0; //分享次数
    private BigDecimal shareRateCountg = new BigDecimal(0); //分享率
    //缓存数据
    private static List<DataCollect> dataCollects;
    private static List<DataCollect> programCollects;
    private static List<DataCollect> gamesCollects;

    @Override
    public List<DataCollect> selectAll(GetParameter parameter) {
        if (dataCollects == null || dataCollects.size() == 0)
        {
            flushAll();
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
    private Date parseDate(String str) {
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
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("wxDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<DataCollect> getClassInfo() {
        return DataCollect.class;
    }

    @Override
    public boolean removeIf(DataCollect productData, JSONObject searchData) {
        return false;
    }

    public GetResult searchData(String beginDate, String endDate, String type, GetParameter parameter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DataCollect> lists = new ArrayList<>();

        if (type == null || type.equals(""))
        {
            for (DataCollect dataCollect : Objects.requireNonNull(dataCollects))
            {
                Date wxDate = dataCollect.getWxDate();
                String format = sdf.format(wxDate);
                if (StringUtils.isNotEmpty(beginDate))
                {
                    if (StringUtils.isNotEmpty(endDate))
                    {
                        int start = format.compareTo(beginDate);
                        int end = format.compareTo(endDate);
                        if (start >= 0 && end <= 0)
                        {
                            lists.add(dataCollect);
                        }
                    } else
                    {
                        int start = format.compareTo(beginDate);
                        if (start >= 0)
                        {
                            lists.add(dataCollect);
                        }
                    }

                } else
                {
                    if (StringUtils.isNotEmpty(endDate))
                    {
                        int end = format.compareTo(endDate);
                        if (end <= 0)
                        {
                            lists.add(dataCollect);
                        }
                    } else
                    {
                        lists.add(dataCollect);
                    }
                }
            }

        } else if (type.equals("0"))
        {
            for (DataCollect dataCollect : Objects.requireNonNull(gamesCollects))
            {
                Date wxDate = dataCollect.getWxDate();
                String format = sdf.format(wxDate);
                if (StringUtils.isNotEmpty(beginDate))
                {
                    if (StringUtils.isNotEmpty(endDate))
                    {
                        int start = format.compareTo(beginDate);
                        int end = format.compareTo(endDate);
                        if (start >= 0 && end <= 0)
                        {
                            lists.add(dataCollect);
                        }
                    } else
                    {
                        int start = format.compareTo(beginDate);
                        if (start >= 0)
                        {
                            lists.add(dataCollect);
                        }
                    }

                } else
                {
                    if (StringUtils.isNotEmpty(endDate))
                    {
                        int end = format.compareTo(endDate);
                        if (end <= 0)
                        {
                            lists.add(dataCollect);
                        }
                    } else
                    {
                        lists.add(dataCollect);
                    }
                }
            }

        } else if (type.equals("1"))
        {
            programCollects = new ArrayList<>();
            List<String> dates = productDataMapper.allDate();
            //遍历program数据日期
            for (String date : dates)
            {
                DataCollect dataCollect = new DataCollect();
                List<WxConfig> wxConfigs = wxConfigMapper.selectAllPrograms();
                productCountp = wxConfigs.size();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateTime = null;
                try
                {
                    dateTime = simpleDateFormat.parse(date);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                //遍历wx_config中AppId
                for (WxConfig wxConfig : wxConfigs)
                {
                    String ddAppId = wxConfig.getDdappid();
                    ProductData productData = productDataMapper.selectByPrimaryKey(ddAppId, dateTime);
                    BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(date, ddAppId);
                    if (productData != null)
                    {
                        if (buyPay != null)
                        {
                            BigDecimal buyCost = buyPay.getBuyCost();
                            buyCostCountp = buyCostCountp.add(buyCost);
                        }
                        Integer wxNew = productData.getWxNew();
                        newCountp += wxNew;
                        Integer wxActive = productData.getWxActive();
                        activeCountp += wxActive;
                        BigDecimal adRevenue = productData.getAdRevenue();
                        if (adRevenue != null)
                        {
                            adRevenueCountp = adRevenueCountp.add(adRevenue);
                        }
                        BigDecimal wxBannerIncome = productData.getWxBannerIncome();
                        if (wxBannerIncome != null)
                        {
                            bannerIncomeCountp = bannerIncomeCountp.add(wxBannerIncome);
                        }
                        BigDecimal wxVideoIncome = productData.getWxVideoIncome();
                        if (wxVideoIncome != null)
                        {
                            videoIncomeCountp = videoIncomeCountp.add(wxVideoIncome);

                        }
                        Integer wxShareUser = productData.getWxShareUser();
                        if (wxShareUser != null)
                        {
                            shareUserCountp += wxShareUser;
                        }

                        Integer wxShareCount = productData.getWxShareCount();
                        if (wxShareCount != null)
                        {
                            shareCountp += wxShareCount;
                        }

                    }
                }
                //数据求和后拼接赋值
                dataCollect.setProductCount((productCountp == null) ? (0) : productCountp);
                dataCollect.setNewCount((newCountp == null) ? (0) : newCountp);
                dataCollect.setActiveCount((activeCountp == null) ? (0) : activeCountp);
                dataCollect.setBuyPay((buyCostCountp == null) ? (new BigDecimal(0)) : buyCostCountp);
                dataCollect.setVideoIncomeCount((videoIncomeCountp == null) ? (new BigDecimal(0)) : videoIncomeCountp);
                dataCollect.setBannerIncomeCount((bannerIncomeCountp == null) ? (new BigDecimal(0)) : bannerIncomeCountp);
                dataCollect.setAdRevenueCount((adRevenueCountp == null) ? (new BigDecimal(0)) : adRevenueCountp);
                dataCollect.setShareUserCount((shareUserCountp == null) ? (0) : shareUserCountp);
                dataCollect.setShareCount((shareCountp == null) ? (0) : shareCountp);
                dataCollect.setRevenueCount(new BigDecimal(0));
                if (activeCountp != 0)
                {
                    BigDecimal rate = new BigDecimal(shareUserCountp * 10000 / activeCountp);
                    dataCollect.setShareRateCount(rate.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
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
                programCollects.add(dataCollect);
                productCountp = 0;
                newCountp = 0;
                activeCountp = 0;
                revenueCountp = new BigDecimal(0);
                adRevenueCountp = new BigDecimal(0);
                videoIncomeCountp = new BigDecimal(0);
                bannerIncomeCountp = new BigDecimal(0);
                buyCostCountp = new BigDecimal(0);
                shareUserCountp = 0;
                shareCountp = 0;
                shareRateCountp = new BigDecimal(0);
            }
            if (programCollects != null)
            {
                for (DataCollect dataCollect : Objects.requireNonNull(programCollects))
                {
                    Date wxDate = dataCollect.getWxDate();
                    String format = sdf.format(wxDate);
                    if (StringUtils.isNotEmpty(beginDate))
                    {
                        if (StringUtils.isNotEmpty(endDate))
                        {
                            int start = format.compareTo(beginDate);
                            int end = format.compareTo(endDate);
                            if (start >= 0 && end <= 0)
                            {
                                lists.add(dataCollect);
                            }
                        } else
                        {
                            int start = format.compareTo(beginDate);
                            if (start >= 0)
                            {
                                lists.add(dataCollect);
                            }
                        }

                    } else
                    {
                        if (StringUtils.isNotEmpty(endDate))
                        {
                            int end = format.compareTo(endDate);
                            if (end <= 0)
                            {
                                lists.add(dataCollect);
                            }
                        } else
                        {
                            lists.add(dataCollect);
                        }
                    }
                }
            }
        }
        filterData(lists, parameter);
        setDefaultSort(parameter);
        return template(lists, parameter);
    }

    public int flushAll() {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        System.out.println("刷新数据汇总" + sm.format(System.currentTimeMillis()));
        dataCollects = new ArrayList<>();

        List<String> dates = minitjWxMapper.dateCash();
        //遍历fc数据日期
        for (String date : dates)
        {
            DataCollect dataCollect = new DataCollect();
            List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
            productCount = wxConfigs.size();
            //遍历wx_config中AppId
            for (WxConfig wxConfig : wxConfigs)
            {
                String ddAppId = wxConfig.getDdappid();
                MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, date);
                BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(date, ddAppId);

                ProductData programData = productDataMapper.selectByAppid(ddAppId, date);
                if (programData != null)
                {
                    Integer wxActive = programData.getWxActive();
                    activeCount += wxActive;
                    Integer programShareUser = programData.getWxShareUser();
                    shareUserCount += programShareUser;
                    Integer programShareCount = programData.getWxShareCount();
                    shareCount += programShareCount;

                    Integer wxNew = programData.getWxNew();
                    newCount += wxNew;
                    BigDecimal wxBannerIncome = programData.getWxBannerIncome();
                    if (wxBannerIncome != null)
                    {
                        bannerIncomeCount = bannerIncomeCount.add(wxBannerIncome);
                    }
                    BigDecimal wxVideoIncome = programData.getWxVideoIncome();
                    if (wxVideoIncome != null)
                    {
                        videoIncomeCount = videoIncomeCount.add(wxVideoIncome);
                    }
                }
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

            if (activeCount != 0)
            {
                BigDecimal rate = new BigDecimal(shareUserCount * 10000 / activeCount);
                dataCollect.setShareRateCount(rate.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
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
        gamesCollects = new ArrayList<>();

        //遍历fc数据日期
        for (String date : dates)
        {
            DataCollect dataCollectG = new DataCollect();
            List<WxConfig> wxConfigsG = wxConfigMapper.selectAllGames();
            productCountg = wxConfigsG.size();
            //遍历wx_config中AppId
            for (WxConfig wxConfig : wxConfigsG)
            {
                String ddAppId = wxConfig.getDdappid();
                MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, date);
                BuyPay buyPay = buyPayMapper.selectByAppIdAndDate(date, ddAppId);
                if (minitjWx != null)
                {
                    if (buyPay != null)
                    {
                        BigDecimal buyCost = buyPay.getBuyCost();
                        buyCostCountg = buyCostCount.add(buyCost);
                    }
                    Integer wxNew = minitjWx.getWxNew();
                    newCountg += wxNew;
                    Integer wxActive = minitjWx.getWxActive();
                    activeCountg += wxActive;
                    BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
                    bannerIncomeCountg = bannerIncomeCountg.add(wxBannerIncome);
                    BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
                    videoIncomeCountg = videoIncomeCountg.add(wxVideoIncome);

                    Integer wxShareUser = minitjWx.getWxShareUser();
                    shareUserCountg += wxShareUser;
                    Integer wxShareCount = minitjWx.getWxShareCount();
                    shareCountg += wxShareCount;

                }
            }
            //数据求和后拼接赋值
            adRevenueCountg = videoIncomeCountg.add(bannerIncomeCountg);
            dataCollectG.setProductCount(productCountg);
            dataCollectG.setNewCount(newCountg);
            dataCollectG.setActiveCount(activeCountg);
            dataCollectG.setBuyPay(buyCostCountg);
            dataCollectG.setVideoIncomeCount(videoIncomeCountg);
            dataCollectG.setBannerIncomeCount(bannerIncomeCountg);
            dataCollectG.setAdRevenueCount(adRevenueCountg);
            dataCollectG.setShareUserCount(shareUserCountg);
            dataCollectG.setShareCount(shareCountg);

            if (activeCountg != 0)
            {
                BigDecimal rate = new BigDecimal(shareUserCountg * 10000 / activeCountg);
                dataCollectG.setShareRateCount(rate.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
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
            dataCollectG.setWxDate(localDate);
            System.out.println(date);
            gamesCollects.add(dataCollectG);

            productCountg = 0;
            newCountg = 0;
            activeCountg = 0;
            revenueCountg = new BigDecimal(0);
            adRevenueCountg = new BigDecimal(0);
            videoIncomeCountg = new BigDecimal(0);
            bannerIncomeCountg = new BigDecimal(0);
            buyCostCountg = new BigDecimal(0);
            shareUserCountg = 0;
            shareCountg = 0;
            shareRateCountg = new BigDecimal(0);
        }

        if (dataCollects.size() > 0)
        {
            return 1;
        } else
        {
            return 0;
        }
    }

    @Override
    public void run() {
        flushAll();
    }
}
