package com.fish.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.utils.BaseComparator;
import com.fish.utils.XwhTool;
import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public interface BaseService<T> {
    Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    /**
     * 进行生成模板数据
     *
     * @param data 下发数据
     * @return 模板数据
     */
    default GetResult<T> template(List<T> data, GetParameter parameter) {
        GetResult<T> result = new GetResult<>();
        String field = parameter.getSort();
        if (field != null)
        {
            try
            {
                PropertyDescriptor pd = new PropertyDescriptor(field, getClassInfo());
                Method method = pd.getReadMethod();
                if (method != null)
                {
                    BaseComparator<T> comparator = new BaseComparator<>(method);
                    if (parameter.getOrder().equals("desc"))
                    {
                        data.sort(comparator.reversed());
                    } else
                    {
                        data.sort(comparator);
                    }
                }
            } catch (Exception e)
            {
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
        }
        result.setCode(200);
        result.setCount(data.size());
        if (parameter.isExcel())
        {
            result.setData(data);
            return result;
        }
        int page = parameter.getPage();
        int rows = parameter.getLimit();
        List<T> sendData = new Vector<>();
        for (int i = (page - 1) * rows; i < page * rows; i++)
        {
            if (data.size() > i)
            {
                sendData.add(data.get(i));
            }
        }
        //当前第一页才进行加载折线图
        if (page == 1)
            result.setLineData(getEcharts(data));
        result.setData(sendData);
        finish(result);
        return result;
    }

    /**
     * 获取折线图数据
     * xAxis 点数据
     * series 线集合 {name:string,data:array,smooth:false}
     *
     * @param data 查找数据
     */
    default JSONObject getEcharts(List<T> data) {
        return null;
    }

    default void finish(GetResult<T> result) {
    }

    void setDefaultSort(GetParameter parameter);

    Class<T> getClassInfo();

    /**
     * 移除数据
     *
     * @param t          节点
     * @param searchData 查询内容
     * @return 是否移除
     */
    boolean removeIf(T t, JSONObject searchData);

    /**
     * 检测值是否不匹配
     *
     * @param key   检测值
     * @param value 上报值
     * @return 比较
     */
    default boolean existValueFalse(String key, int value) {
        if (key != null && !key.trim().isEmpty())
        {
            return value != Integer.valueOf(key);
        }
        return false;
    }

    /**
     * 检测值是否不匹配
     *
     * @param key   检测值
     * @param value 上报值
     * @return 比较
     */
    default boolean existValueFalse(String key, long value) {
        if (key != null && !key.trim().isEmpty())
        {
            return value != Long.valueOf(key);
        }
        return false;
    }

    /**
     * 检测值是否不匹配
     *
     * @param key   检测值
     * @param value 上报值
     * @return 比较
     */
    default boolean existValueFalse(String key, String value) {
        if (key != null && !key.trim().isEmpty())
        {
            return !value.contains(key);
        }
        return false;
    }


    /**
     * 检测值是否不匹配
     *
     * @param key   检测值
     * @param value 上报值
     * @return 比较
     */
    default boolean existValueFull(String key, String value) {
        if (key != null && !key.trim().isEmpty())
        {
            return !value.equals(key);
        }
        return false;
    }

    /**
     * 检测时间比较
     *
     * @param date  检测值
     * @param times 上报值
     * @return 非值
     */
    default boolean existTimeFalse(Date date, String times) {
        if (times != null && !times.trim().isEmpty())
        {
            Date[] parse = XwhTool.parseDate(times);
            return date.before(parse[0]) || date.after(parse[1]);
        }
        return false;
    }

    /**
     * 检测时间比较
     *
     * @param date  检测值
     * @param times 上报值
     * @return 非值
     */
    default boolean existTimeBefore(Date date, String times) {
        if (times != null && !times.trim().isEmpty())
        {
            Date time = XwhTool.parseTime(times);
            if (time == null)
                return false;
            return date.before(time);
        }
        return false;
    }

    /**
     * 检测时间比较
     *
     * @param date  检测值
     * @param times 上报值
     * @return 非值
     */
    default boolean existTimeAfter(Date date, String times) {
        if (times != null && !times.trim().isEmpty())
        {
            Date time = XwhTool.parseTime(times);
            if (time == null)
                return false;
            return date.after(time);
        }
        return false;
    }

    /**
     * 获取搜索条件
     *
     * @param searchData 搜索参数
     * @return
     */
    default JSONObject getSearchData(String searchData) {
        if (searchData == null)
            return null;
        return JSON.parseObject(searchData);
    }

    /**
     * 拦截数据
     *
     * @param list 查找数据
     */
    default JSONObject filterData(List<T> list, GetParameter parameter) {
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null)
        {
            return null;
        }
        list.removeIf(wxInput ->
        {
            try
            {
                return removeIf(wxInput, search);
            } catch (Exception e)
            {
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
            return false;
        });
        return search;
    }

    //查询结果
    List<T> selectAll(GetParameter parameter);

    /**
     * 查询全部数据
     *
     * @param parameter 上报参数
     * @return 返回结果
     */
    default GetResult findAll(GetParameter parameter) {
        long startTime = System.currentTimeMillis();
        GetResult<T> result = new GetResult<>();
        List<T> data = selectAll(parameter);
        Vector<Long> record = new Vector<>();
        try
        {
            record.add(System.currentTimeMillis() - startTime);
            //动态移除数据
            filterData(data, parameter);
            record.add(System.currentTimeMillis() - startTime);
            setDefaultSort(parameter);
            record.add(System.currentTimeMillis() - startTime);
            return template(data, parameter);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
            result.setCount(404);
            result.setMsg("查询错误，请检查数据!");
        } finally
        {
            record.add(System.currentTimeMillis() - startTime);
            // System.out.println("查询获奖记录:" + (System.currentTimeMillis() - startTime) / 1000 + "s,长度:" + JSONObject.toJSONString(result).length() + "记录长度:" + JSONObject.toJSONString(record));
        }
        return result;
    }

}
