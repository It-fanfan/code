package com.fish.interceptor;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.BuyPayMapper;
import com.fish.dao.primary.mapper.OnlineMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.primary.model.Online;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.DataCollect;
import com.fish.dao.third.model.MinitjWx;
import com.fish.utils.BaseConfig;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UpdateDataCollect implements Runnable
{
    @Autowired
    BaseConfig baseConfig;
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
    public void run()
    {
        updateFcData();
    }

    public void updateFcData()
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
    }
}
