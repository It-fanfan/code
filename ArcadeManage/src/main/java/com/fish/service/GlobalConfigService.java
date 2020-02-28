package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GlobalConfigService implements BaseService<GoodsValue>
{

    @Autowired
    GoodsValueMapper goodsValueMapper;

    @Override
    //查询展示所有产品信息
    public List<GoodsValue> selectAll(GetParameter parameter)
    {
        return goodsValueMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(GoodsValue record)
    {
        record.setInserttime(new Timestamp(new Date().getTime()));
        record.setDdcosttype("rmb");
        record.setDdgoodstype("coin");
        return goodsValueMapper.insertSelective(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(GoodsValue record)
    {

        return goodsValueMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<GoodsValue> getClassInfo()
    {
        return GoodsValue.class;
    }

    @Override
    public boolean removeIf(GoodsValue goodsValue, JSONObject searchData)
    {
        return false;
    }
}
