package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ProductDataMapper;
import com.fish.dao.primary.model.BuyPay;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.WxConfig;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.ReadJsonUtil;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class ProductDataService implements BaseService<ProductData> {

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
        String sqlProgram = "select * from program_entering where 1 = 1";
        StringBuilder SQL = new StringBuilder(sql);
        StringBuilder SQLProgram = new StringBuilder(sqlProgram);
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null)
        {
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
                            productData.setProgramType(wxConfig.getProgramType());
                            Integer wxShareUser = minitjWx.getWxShareUser();
                            productData.setWxShareUser(wxShareUser);
                            Integer wxShareCount = minitjWx.getWxShareCount();
                            productData.setWxShareCount(wxShareCount);
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
                            productData.setWxBannerIncome(minitjWx.getWxBannerIncome());
                            if (!checkObjFieldIsNull(productData.getMinitjWx()))
                            {
                                productDatas.add(productData);
                            }
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    String ddAppId = wxConfig.getDdappid();
                    LocalDate now = LocalDate.now();
                    LocalDate beforeDate = now.minusDays(1);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String localDate = formatter.format(beforeDate);

                    try
                    {
                        ProductData programData = productDataMapper.selectByAppid(ddAppId, localDate);
                        if (programData != null)
                        {
                            programData.setProgramType(wxConfig.getProgramType());
                            productDatas.add(programData);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } else
        {
            String ddAppId = search.getString("ddappid");
            String times = search.getString("times");
            String type = search.getString("type");
            if (StringUtils.isBlank(type))
            {
                if (StringUtils.isNotBlank(ddAppId))
                {
                    WxConfig wxConfig = cacheService.getWxConfig(ddAppId);
                    Integer programType = wxConfig.getProgramType();
                    if (programType == 0)
                    {
                        SQL.append(" and wx_appid =" + "'").append(ddAppId).append("'");
                        if (StringUtils.isNotBlank(times))
                        {
                            String[] split = times.split("-");
                            SQL.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                            SQL.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                        }
                        List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
                        for (MinitjWx wxData : WxDatas)
                        {
                            ProductData productData = new ProductData();
                            String appId = wxData.getWxAppid();

                            wxConfig = cacheService.getWxConfig(appId);
                            if (wxConfig != null)
                            {
                                productData.setProgramType(wxConfig.getProgramType());
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
                                productDatas.add(productData);
                            }
                        }
                        return productDatas;
                    } else
                    {
                        SQLProgram.append(" and wx_appid =" + "'").append(ddAppId).append("'");
                        if (StringUtils.isNotBlank(times))
                        {
                            String[] split = times.split("-");
                            SQLProgram.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                            SQLProgram.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            productDatas.addAll(productData);
                            return productDatas;
                        } else
                        {
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            productDatas.addAll(productData);
                            return productDatas;
                        }
                    }

                }
                if (StringUtils.isNotBlank(times))
                {
                    String[] split = times.split("-");
                    SQL.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                    SQL.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                    SQLProgram.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                    SQLProgram.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                }
                List<ProductData> programData = productDataMapper.searchData(SQLProgram.toString());
                productDatas.addAll(programData);
                List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
                for (MinitjWx wxData : WxDatas)
                {
                    ProductData productData = new ProductData();
                    String appId = wxData.getWxAppid();
                    // WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
                    WxConfig wxConfig = cacheService.getWxConfig(appId);
                    if (wxConfig != null)
                    {
                        productData.setProgramType(wxConfig.getProgramType());
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
                        productDatas.add(productData);
                    }
                }
            } else
            {
                if (type.equals("0"))
                {
                    if (StringUtils.isNotBlank(times))
                    {
                        String[] split = times.split("-");
                        SQL.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                        SQL.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                    }
                    if (StringUtils.isNotBlank(ddAppId))
                    {
                        SQL.append(" and wx_appid =" + "'").append(ddAppId).append("'");
                        List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQL.toString());
                        for (MinitjWx wxData : WxDatas)
                        {

                            ProductData productData = new ProductData();
                            productData.setProgramType(0);
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
                                productDatas.add(productData);
                            }
                        }
                    } else
                    {
                        wxConfigs = cacheService.getAllWxConfig();
                        for (WxConfig wxConfig : wxConfigs)
                        {
                            if (wxConfig.getProgramType() == 0)
                            {
                                String SQLAppId = SQL.toString();
                                ddAppId = wxConfig.getDdappid();
                                SQLAppId = SQLAppId + " and wx_appid =" + "'" + ddAppId + "'";
                                List<MinitjWx> WxDatas = minitjWxMapper.searchData(SQLAppId);
                                for (MinitjWx wxData : WxDatas)
                                {
                                    ProductData productData = new ProductData();
                                    String appId = wxData.getWxAppid();
                                    wxConfig = cacheService.getWxConfig(appId);
                                    if (wxConfig != null)
                                    {
                                        productData.setProgramType(wxConfig.getProgramType());
                                        productData.setProductName(wxConfig.getProductName());
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
                                        productDatas.add(productData);
                                    }
                                }

                            }
                        }
                    }
                } else
                {
                    if (StringUtils.isNotBlank(ddAppId))
                    {
                        SQLProgram.append(" and wx_appid =" + "'").append(ddAppId).append("'");
                        if (StringUtils.isNotBlank(times))
                        {
                            String[] split = times.split("-");
                            SQLProgram.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                            SQLProgram.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            productDatas.addAll(productData);
                            return productDatas;
                        } else
                        {
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            for (ProductData product : productData)
                            {
                                product.setProgramType(1);
                            }
                            productDatas.addAll(productData);
                            return productDatas;
                        }
                    } else
                    {
                        if (StringUtils.isNotBlank(times))
                        {
                            String[] split = times.split("-");
                            SQLProgram.append(" and wx_date >=" + "'").append(split[0].trim()).append("'");
                            SQLProgram.append(" and wx_date <=" + "'").append(split[1].trim()).append("'");
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            productDatas.addAll(productData);
                            return productDatas;
                        } else
                        {
                            List<ProductData> productData = productDataMapper.searchData(SQLProgram.toString());
                            productDatas.addAll(productData);
                            return productDatas;
                        }
                    }
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

    public int insertExcel(JSONObject record) {
        String context = record.getString("context");
        System.out.println("context :" + context);
        context = context.substring(1, context.length() - 1);
        try
        {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            List<ProductData> lists = new ArrayList<>();
            for (int i = 0; i < param.size(); i++)
            {
                String singleData = param.get(i).toString();

                String singleString = singleData.substring(1, singleData.length() - 1);
                System.out.println("我是singleString :" + singleString);
                String[] split = singleString.split("], ");
                for (int j = 0; j < split.length; j++)
                {

                    if (j != 0 && j < split.length)
                    {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        ProductData productData = new ProductData();
                        for (int x = 0; x < singleSplit.length; x++)
                        {
                            if (x == 0)
                            {
                                mapSingle.put("wx_date", singleSplit[x].trim());
                            }
                            if (x == 1)
                            {
                                mapSingle.put("wx_appid", singleSplit[x].trim());
                            }
                            if (x == 2)
                            {
                                mapSingle.put("product_name", singleSplit[x].trim());
                            }
                            if (x == 3)
                            {
                                mapSingle.put("product_type", singleSplit[x].trim());
                            }
                            if (x == 4)
                            {
                                mapSingle.put("wx_new", singleSplit[x].trim());
                            }
                            if (x == 5)
                            {
                                mapSingle.put("wx_active", singleSplit[x].trim());
                            }
                            if (x == 6)
                            {
                                mapSingle.put("wx_visit", singleSplit[x].trim());
                            }
                            if (x == 7)
                            {
                                mapSingle.put("recharge", singleSplit[x].trim());
                            }
                            if (x == 8)
                            {
                                mapSingle.put("ad_revenue", singleSplit[x].trim());
                            }
                            if (x == 9)
                            {
                                mapSingle.put("wx_video_income", singleSplit[x].trim());
                            }
                            if (x == 10)
                            {
                                mapSingle.put("wx_banner_income", singleSplit[x].trim());
                            }
                            if (x == 11)
                            {
                                mapSingle.put("active_up", singleSplit[x].trim());
                            }
                            if (x == 12)
                            {
                                mapSingle.put("wx_share_user", singleSplit[x].trim());
                            }
                            if (x == 13)
                            {
                                mapSingle.put("wx_share_count", singleSplit[x].trim());
                            }
                            if (x == 14)
                            {
                                mapSingle.put("wx_share_rate", singleSplit[x].trim());
                            }
                            if (x == 15)
                            {
                                mapSingle.put("wx_remain2", singleSplit[x].trim());
                            }
                            if (x == 16)
                            {
                                mapSingle.put("wx_avg_login", singleSplit[x].trim());
                            }
                            if (x == 17)
                            {
                                mapSingle.put("wx_avg_online", singleSplit[x].trim());
                            }
                        }
                        String wxDate = mapSingle.get("wx_date");
                        String wxAppid = mapSingle.get("wx_appid");
                        String productName = mapSingle.get("product_name");
                        String productType = mapSingle.get("product_type");
                        String wxNew = mapSingle.get("wx_new");
                        String wxActive = mapSingle.get("wx_active");
                        String wxVisit = mapSingle.get("wx_visit");
                        String recharge = mapSingle.get("recharge");
                        String adRevenue = mapSingle.get("ad_revenue");
                        String wxVideoIncome = mapSingle.get("wx_video_income");
                        String wxBannerIncome = mapSingle.get("wx_banner_income");
                        String activeUp = mapSingle.get("active_up");
                        String wxShareUser = mapSingle.get("wx_share_user");
                        String wxShareCount = mapSingle.get("wx_share_count");
                        String wxShareRate = mapSingle.get("wx_share_rate");
                        String wxRemain2 = mapSingle.get("wx_remain2");
                        String wxAvgLogin = mapSingle.get("wx_avg_login");
                        String wxAvgOnline = mapSingle.get("wx_avg_online");

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try
                        {
                            date = simpleDateFormat.parse(wxDate);
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        productData.setWxDate(date);
                        productData.setWxAppid(wxAppid);
                        productData.setProductName(productName);
                        productData.setProgramType(Integer.valueOf(productType));
                        productData.setWxNew(Integer.valueOf(wxNew));
                        productData.setWxActive(Integer.valueOf(wxActive));
                        productData.setWxVisit(Integer.valueOf(wxVisit));
                        if (recharge != null)
                        {
                            productData.setRecharge(new BigDecimal(recharge));
                        } else
                        {
                            productData.setRecharge(new BigDecimal(0));
                        }
                        productData.setAdRevenue(new BigDecimal(adRevenue));
                        productData.setWxVideoIncome(new BigDecimal(wxVideoIncome));
                        productData.setWxBannerIncome(new BigDecimal(wxBannerIncome));
                        productData.setActiveUp(new BigDecimal(activeUp));
                        productData.setWxShareUser(Integer.valueOf(wxShareUser));
                        productData.setWxShareCount(Integer.valueOf(wxShareCount));
                        productData.setWxShareRate(new BigDecimal(wxShareRate));
                        productData.setWxRemain2(new BigDecimal(wxRemain2));
                        productData.setWxAvgLogin(new BigDecimal(wxAvgLogin));
                        productData.setWxAvgOnline(new BigDecimal(wxAvgOnline));
                        productData.setInsertTime(new Timestamp(System.currentTimeMillis()));
                        lists.add(productData);
                    }
                }
                productDataMapper.insertBatch(lists);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * java反射机制判断对象所有属性是否全部为空
     *
     * @param obj 对象参数
     * @return 返回属性名称
     */
    private boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {

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

    /**
     * 页面搜索查询
     *
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param productName 产品名称
     * @param parameter   参数
     * @return 返回结果
     */
    public GetResult searchProductData(String beginDate, String endDate, String productName, GetParameter parameter) {
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
            SQL.append(" and wx_appid =" + "'").append(productName).append("'");

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

    public int deleteSelective(ProductData productInfo) {
        Date wxDate = productInfo.getWxDate();
        int count = productDataMapper.deleteByPrimaryKey(productInfo.getWxAppid(), wxDate);
        return count;
    }
}
