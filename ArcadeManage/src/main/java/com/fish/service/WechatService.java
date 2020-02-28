package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.WxInputMapper;
import com.fish.dao.primary.model.WxInput;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.utils.BaseConfig;
import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WechatService implements BaseService<WxInput>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    WxInputMapper wxInputMapper;

    /**
     * 进行保存数据
     *
     * @param wxInput 文件解析
     */
    public void saveWxInput(WxInput wxInput)
    {
        try
        {
            //先进行删除插入数据
            wxInputMapper.deleteByPrimaryKey(wxInput.getInsertTime());
            //进行查找汇总
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //查询虚拟汇总，广告汇总
            String SQL = "SELECT SUM(virtual_pay) AS payTotal,SUM(video_income) AS  videoTotal FROM wx_input WHERE DATE(insert_time)<'" + dateFormat.format(wxInput.getInsertTime()) + "'";
            List<Map> sumResult = wxInputMapper.selectBySQL(SQL);
            wxInput.setVideoTotal(wxInput.getVideoIncome());
            wxInput.setPayTotal(wxInput.getVirtualPay());
            if (sumResult != null && !sumResult.isEmpty())
            {
                try
                {
                    BigDecimal pay = (BigDecimal) sumResult.get(0).get("payTotal");
                    BigDecimal video = (BigDecimal) sumResult.get(0).get("videoTotal");
                    if (pay != null)
                    {
                        pay = pay.add(wxInput.getPayTotal());
                        wxInput.setPayTotal(pay);
                    }
                    if (video != null)
                    {
                        video = video.add(wxInput.getVideoTotal());
                        wxInput.setVideoTotal(video);
                    }
                } catch (Exception e)
                {
                    LOGGER.error(Log4j.getExceptionInfo(e));
                }
            }
            //再进行保存数据
            wxInputMapper.insert(wxInput);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 判断移除数据
     *
     * @param wxInput    数据节点
     * @param searchData 查询内容
     * @return 是否移除
     */
    public boolean removeIf(WxInput wxInput, JSONObject searchData)
    {
        String st = searchData.getString("st"), ed = searchData.getString("ed");
        if (st == null || ed == null || st.isEmpty() || ed.isEmpty())
            return false;
        Date start = parseDate(st), end = parseDate(ed);
        return wxInput.getInsertTime().before(start) || wxInput.getInsertTime().after(end);
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
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            return format.parse(str);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return new Date();
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return 格式数据
     */
    private String formatDate(Date date)
    {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            return format.format(date);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 设置默认排序
     *
     * @param parameter 上传参数
     */
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("insertTime");
        parameter.setOrder("desc");
    }

    /**
     * 进行查询数据
     *
     * @param parameter 查询内容
     * @return 结果
     */
    public GetResult select(GetParameter parameter)
    {
        GetResult<WxInput> result = new GetResult<>();
        List<WxInput> list = selectAll(parameter);
        try
        {
            //动态移除数据
            filterData(list, parameter, false);
            setDefaultSort(parameter);
            return template(list, parameter);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
            result.setCount(404);
            result.setMsg("查询错误，请检查数据!");
        }

        return result;
    }

    /**
     * 拦截数据
     *
     * @param list      查找数据
     * @param parameter 上传参数
     */
    private void filterData(List<WxInput> list, GetParameter parameter, boolean isNew)
    {
        JSONObject searchData = filterData(list, parameter);
        if (!isNew)
            return;
        if (searchData == null)
            createNewDate(list, new Date());
        else
            createNewDate(list, parseDate(searchData.getString("ed")));
    }

    //查询结果
    public List<WxInput> selectAll(GetParameter parameter)
    {
        return wxInputMapper.selectAll();
    }


    /**
     * 进行查找数据
     *
     * @param parameter 查询内容
     */
    public GetResult findWxInput(GetParameter parameter)
    {
        GetResult<WxInput> result = new GetResult<>();
        List<WxInput> list = selectAll(parameter);
        try
        {
            //动态移除数据
            filterData(list, parameter, true);
            setDefaultSort(parameter);
            return template(list, parameter);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
            result.setCount(404);
            result.setMsg("查询错误，请检查数据!");
        }

        return result;
    }

    /**
     * 进行创建一条新增数据
     *
     * @param list 查询结果
     * @param date 日期
     */
    private void createNewDate(List<WxInput> list, Date date)
    {
        String dayFormat = formatDate(date);
        WxInput now = list.stream().filter(o -> formatDate(o.getInsertTime()).equals(dayFormat)).findFirst().orElse(null);
        if (now == null)
        {
            now = new WxInput();
            now.setInsertTime(date);
            list.add(now);
        }
    }

    public Class<WxInput> getClassInfo()
    {
        return WxInput.class;
    }
}
