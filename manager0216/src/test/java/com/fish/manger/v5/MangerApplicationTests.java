package com.fish.manger.v5;

import com.fish.dao.primary.mapper.ManageAccountMapper;
import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.model.ManageAccount;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.DataCollect;
import com.fish.dao.third.model.MinitjWx;
import com.fish.manger.v5.generator.LayuiHtml;
import com.fish.utils.RedisData;
import com.fish.utils.RedisUtils;
import com.fish.utils.XwhTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan({"com.fish.manger.v5.mapper", "com.fish.dao.primary.mapper"})
public class MangerApplicationTests
{
    @Autowired
    LayuiHtml layUi;
    @Autowired
    ManageAccountMapper userMapper;
    @Autowired
    private RankingMapper rankingMapper;
    @Autowired
    RedisData redisData;
    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    MinitjWxMapper minitjWxMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    static Logger LOGGER = LoggerFactory.getLogger(MangerApplicationTests.class);

    private void createLayUi()
    {
        layUi.generator();
    }

    /**
     * 设置随机密码
     *
     * @return 密码
     */
    private String setRandomPassword()
    {
        int len = ThreadLocalRandom.current().nextInt(9, 16);
        StringBuilder builder = new StringBuilder();
        char a = '0', b = '9', c = 'a', d = 'z', e = 'A', f = 'Z';
        char[] s = new char[]{'@', '&', '-', '.'};
        int size = 4;
        int hit = ThreadLocalRandom.current().nextInt(3);
        for (int i = 0; i < len; i++)
        {
            switch (hit)
            {
                case 0:
                {
                    int val = b - a;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + a));
                }
                break;
                case 1:
                {
                    int val = d - c;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + c));
                }
                break;
                case 2:
                {
                    int val = f - e;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + e));
                }
                break;
                case 3:
                {
                    size = 3;
                    builder.append(s[ThreadLocalRandom.current().nextInt(s.length)]);
                }
            }
            hit = ThreadLocalRandom.current().nextInt(size);
        }
        return builder.toString();
    }
    @Test
    public void createAccount()
    {
        ManageAccount account = new ManageAccount();
        account.setUsername("admin");
        String password = setRandomPassword();
      //  String password = "admin";
        String value = XwhTool.getMD5Encode(account.getUsername() + password);
        account.setPassword(value);
        account.setNickname("admin");
        account.setUpdatetime(new Date());
        account.setRole("user");
        System.out.println("新建账号:" + account.getUsername() + ",密码:" + password+","+value);
//        ManageAccount temp = userMapper.selectByPrimaryKey(account.getUsername());
//        if (temp == null)
//        {
////            userMapper.insert(account);
//            LOGGER.info("新建账号:" + account.getUsername() + ",密码:" + password);
//            System.out.println("新建账号:" + account.getUsername() + ",密码:" + password);
//        }

//                user.setPassword("");
//                Object tokenCredentials = XwhTool.getMD5Encode(authcToken.getUsername() + String.valueOf(authcToken.getPassword()));
    }

//    @Test
//    public void contextLoads()
//    {
//        createAccount();
//    }

//    @Test
//    public  void creat(){
//        createLayUi();
//    }

    @Test
    public void redisData() {
        int productCount = 0;//产品数量
         Integer newCount = 0;//新增数量
         Integer activeCount = 0;//活跃数量
         BigDecimal revenueCount = new BigDecimal(0);//总收入
         BigDecimal adRevenueCount = new BigDecimal(0);//广告总收入
         BigDecimal videoIncomeCount = new BigDecimal(0); //视频总收入
         BigDecimal bannerIncomeCount = new BigDecimal(0); //banner总收入
         BigDecimal buyPay = new BigDecimal(0);//买量支出
         Integer shareUserCount = 0; //分享人数
         Integer shareCount = 0; //分享次数
         BigDecimal shareRateCount = new BigDecimal(0); //分享率
        ArrayList<DataCollect> dataCollects = new ArrayList<>();
//        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();

//        stringValueOperations.set("aaabc" , "efg");
//        stringValueOperations.set("aaaol" , "poi");
//
//        String aaaol = stringValueOperations.get("aaaol");
//        System.out.println(aaaol);
        String coin = RedisUtils.hget("user-oxDM75DNVEcHIT0eHnL-QYCMv8sY", "coin");
//        System.out.println(coin);
//
//        RedisUtils.hset("user-oSn_LwyddZA4PLWhR7qKz_C8xyRU", "coin", "8950");
//        Set<String> keys = redisTemplate.keys("user-*");
//        System.out.println(JSON.toJSONString(keys));
//        redisData.searchAllUser();


//        List<DataCollect> dataCollects = new ArrayList<>();
//        DataCollect dataCollect1 = new DataCollect();
//
//        dataCollect1.setWxDate(new Date());
//        dataCollect1.setVideoIncomeCount(new BigDecimal(1));
//        dataCollect1.setNewCount(1);
//        dataCollect1.setProductCount(1);
//        dataCollect1.setShareUserCount(1);
//        dataCollect1.setShareRateCount(new BigDecimal(1));
//        dataCollect1.setBannerIncomeCount(new BigDecimal(1));
//        dataCollect1.setRevenueCount(new BigDecimal(1));
//        dataCollect1.setBuyPay(new BigDecimal(1));
//        dataCollect1.setActiveCount(1);
//
//        dataCollect1.setAdRevenueCount(new BigDecimal(1));
//
//        dataCollect1.setShareCount(1);
//
//
//
//        DataCollect dataCollect2 = new DataCollect();
//        dataCollect2.setWxDate(new Date());
//        dataCollect2.setVideoIncomeCount(new BigDecimal(2));
//        dataCollect2.setNewCount(2);
//        dataCollect2.setProductCount(2);
//        dataCollect2.setShareUserCount(2);
//        dataCollect2.setShareRateCount(new BigDecimal(2));
//        dataCollect2.setBannerIncomeCount(new BigDecimal(2));
//        dataCollect2.setRevenueCount(new BigDecimal(2));
//        dataCollect2.setBuyPay(new BigDecimal(2));
//        dataCollect2.setActiveCount(2);
//
//        dataCollect2.setAdRevenueCount(new BigDecimal(2));
//
//        dataCollect2.setShareCount(2);
//
//        dataCollects.add(dataCollect1);
//        dataCollects.add(dataCollect2);
        List<String> dates = minitjWxMapper.dateCash();
        for (String date : dates) {
            DataCollect dataCollect = new DataCollect();

            List<WxConfig> wxConfigs = wxConfigMapper.selectAllGames();
            productCount = wxConfigs.size();
            for (WxConfig wxConfig : wxConfigs) {
                String ddAppId = wxConfig.getDdappid();
                MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey(ddAppId, date);
                if(minitjWx !=null){
                    Integer wxNew = minitjWx.getWxNew();
                    newCount = newCount + wxNew;
                    Integer wxActive = minitjWx.getWxActive();
                    activeCount = activeCount + wxActive;
                    BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
                    bannerIncomeCount = bannerIncomeCount.add(wxBannerIncome);
                    BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
                    videoIncomeCount = videoIncomeCount.add(wxVideoIncome);

                    Integer wxShareUser = minitjWx.getWxShareUser();
                    shareUserCount = shareUserCount + wxShareUser;
                    Integer wxShareCount = minitjWx.getWxShareCount();
                    shareCount = shareCount + wxShareCount;
                    BigDecimal wxShareRate = minitjWx.getWxShareRate();
                    shareRateCount = shareRateCount.add(wxShareRate);
                }

            }
            dataCollect.setProductCount(productCount);
            dataCollect.setNewCount(newCount);
            dataCollect.setActiveCount(activeCount);
            dataCollect.setVideoIncomeCount(videoIncomeCount);
            dataCollect.setBannerIncomeCount(bannerIncomeCount);
            dataCollect.setShareUserCount(shareUserCount);
            dataCollect.setShareCount(shareCount);
            if(productCount != 0){
                dataCollect.setShareRateCount(shareRateCount.divide(new BigDecimal(productCount),2, RoundingMode.HALF_UP));
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date localDate = null;
            try {
                localDate = format.parse(date);
            } catch (ParseException e) {
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
            buyPay = new BigDecimal(0);
            shareUserCount = 0;
            shareCount = 0;
            shareRateCount = new BigDecimal(0);
        }
//        //存储缓存
        RedisUtils.setList("dataList", dataCollects);

        List<DataCollect> list  = (List<DataCollect>)RedisUtils.getList("dataList");
        for (DataCollect data  : list) {
            System.out.println(data.toString());
        }
    }

}
