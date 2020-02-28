package com.fish.service;

import com.fish.dao.second.model.UserAllInfo;
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
import java.util.Map;
import java.util.Vector;

public interface BaseService<T>
{
    Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    /**
     * 进行生成模板数据
     *
     * @param data 下发数据
     * @return 模板数据
     */
    default GetResult<T> template(List<T> data, GetParameter parameter)
    {
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
        result.setData(sendData);
        return result;
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
    boolean removeIf(T t, Map<String, String> searchData);

    /**
     * 检测值是否不匹配
     *
     * @param key   检测值
     * @param value 上报值
     * @return 比较
     */
    default boolean existValueFalse(String key, int value)
    {
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
    default boolean existValueFalse(String key, long value)
    {
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
    default boolean existValueFalse(String key, String value)
    {
        if (key != null && !key.trim().isEmpty())
        {
            return !value.contains(key);
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
    default boolean existTimeFalse(Date date, String times)
    {
        if (times != null && !times.trim().isEmpty())
        {
            Date[] parse = XwhTool.parseDate(times);
            return date.before(parse[0]) || date.after(parse[1]);
        }
        return false;
    }

    /**
     * 拦截数据
     *
     * @param list 查找数据
     */
    default Map<String, String> filterData(List<T> list, GetParameter parameter)
    {
        if (parameter.getSearchData() == null)
        {
            return null;
        }
        Map<String, String> searchData = XwhTool.parseJsonBase(parameter.getSearchData());
        list.removeIf(wxInput ->
                {
                    try
                    {
                        return removeIf(wxInput, searchData);
                    } catch (Exception e)
                    {
                        LOGGER.error(Log4j.getExceptionInfo(e));
                    }
                    return false;
                }
        );
        return searchData;
    }

    //查询结果
    List<T> selectAll(GetParameter parameter);

    /**
     * 查询全部数据
     *
     * @param parameter 上报参数
     * @return 返回结果
     */
    default GetResult findAll(GetParameter parameter)
    {
        GetResult<T> result = new GetResult<>();
        List<T> data = (List<T>) selectAll(parameter);
        try
        {
            //动态移除数据
            filterData(data, parameter);
            setDefaultSort(parameter);
            return template(data, parameter);
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
            result.setCount(404);
            result.setMsg("查询错误，请检查数据!");
        }
        return result;
    }

}
