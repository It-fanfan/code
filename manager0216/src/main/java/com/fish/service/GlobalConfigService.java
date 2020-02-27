package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GlobalConfigService implements BaseService<GoodsValue> {

    @Autowired
    GoodsValueMapper goodsValueMapper;

    @Override
    //查询所有商品信息
    public List<GoodsValue> selectAll(GetParameter parameter) {

        List<GoodsValue> goodsValues = goodsValueMapper.selectAll();
        for (GoodsValue goodsValue : goodsValues)
        {
            String goodsType = goodsValue.getDdgoodstype();
            String costType = goodsValue.getDdcosttype();
            Integer ddvalue = goodsValue.getDdvalue();
            if ("coin".equals(costType))
            {
                BigDecimal ddPrice = goodsValue.getDdprice();
                goodsValue.setDdcosttype(ddPrice.intValue() + "金币");
            } else
            {
                BigDecimal ddPrice = goodsValue.getDdprice();
                goodsValue.setDdcosttype(ddPrice.intValue() + "元");
            }
            if ("coin".equals(goodsType))
            {
                goodsValue.setCostDesc("购买" + ddvalue.toString() + "金币");
                goodsValue.setGainDesc(ddvalue.toString() + "金币");
                goodsValue.setCoinNumber(goodsValue.getDdvalue().toString());
            }
            if ("recharge".equals(goodsType))
            {
                int cashNumber = ddvalue / 100;
                goodsValue.setCostDesc("提现" + cashNumber + "元");
                goodsValue.setGainDesc(cashNumber + "元");
                goodsValue.setCashNumber(String.valueOf(cashNumber));
            }
            if ("head".equals(goodsType))
            {
                goodsValue.setCostDesc("购买" + ddvalue.toString() + "号头像");
                goodsValue.setGainDesc(ddvalue.toString() + "号头像");
                goodsValue.setHeadNumber(goodsValue.getDdvalue().toString());
            }
        }

        return goodsValues;
    }

    //新增展示所有产品信息
    public int insert(GoodsValue record) {
        record.setInserttime(new Timestamp(System.currentTimeMillis()));
        String goodsType = record.getDdgoodstype();
        if ("recharge".equals(goodsType) || "coin".equals(goodsType))
        {
            record.setDdcosttype("rmb");
            record.setDdname(record.getDdprice() + "元");
        } else
        {
            record.setDdcosttype("coin");
            record.setDdname("hf" + record.getHeadNumber());
        }
        if (StringUtils.isNotBlank(record.getCoinNumber()))
        {
            String coinNumber = record.getCoinNumber();
            record.setDdvalue(Integer.valueOf(coinNumber));
            record.setDdprice(record.getDdprice());
            record.setDddesc("购买" + coinNumber + "金币");
            record.setDdfrist(true);
        } else if (StringUtils.isNotBlank(record.getHeadNumber()))
        {
            String headNumber = record.getHeadNumber();
            record.setDdvalue(Integer.valueOf(headNumber));
            record.setDdprice(record.getDdprice());
            record.setDddesc(record.getDdprice() + "金币");
            record.setDdfrist(false);
        } else if (StringUtils.isNotBlank(record.getCashNumber()))
        {
            String cashNumber = record.getCashNumber();
            record.setDdvalue(Integer.parseInt(cashNumber) * 100);
            record.setDdprice(record.getDdprice());
            record.setDddesc("提现" + cashNumber + "元");
            record.setDdfrist(false);
        } else
        {
            record.setDdvalue(0);
            record.setDdprice(new BigDecimal(0));
        }
        return goodsValueMapper.insertSelective(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(GoodsValue record) {
        record.setInserttime(new Timestamp(new Date().getTime()));
        String goodsType = record.getDdgoodstype();
        if ("recharge".equals(goodsType) || "coin".equals(goodsType))
        {
            record.setDdcosttype("rmb");
            record.setDdname(record.getDdprice() + "元");
        } else
        {
            record.setDdcosttype("coin");
            record.setDdname("hf" + record.getHeadNumber());
        }
        if (StringUtils.isNotBlank(record.getCoinNumber()))
        {
            String coinNumber = record.getCoinNumber();
            record.setDdvalue(Integer.valueOf(coinNumber));
            record.setDdprice(record.getDdprice());
            record.setDddesc("购买" + coinNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getHeadNumber()))
        {
            String headNumber = record.getHeadNumber();
            record.setDdvalue(Integer.valueOf(headNumber));
            record.setDdprice(record.getDdprice());
            record.setDddesc(record.getDdprice() + "金币");
        } else if (StringUtils.isNotBlank(record.getCashNumber()))
        {
            String cashNumber = record.getCashNumber();
            record.setDdvalue(Integer.parseInt(cashNumber) * 100);
            record.setDdprice(new BigDecimal(record.getDdprice().toString()));
            record.setDddesc("提现" + cashNumber + "元");
        }
        return goodsValueMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteSelective(GoodsValue goodsValue) {
        int count = goodsValueMapper.deleteByPrimaryKey(goodsValue.getDdid());
        return count;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<GoodsValue> getClassInfo() {
        return GoodsValue.class;
    }

    @Override
    public boolean removeIf(GoodsValue goodsValue, JSONObject searchData) {
        if (existValueFalse(searchData.getString("money"), goodsValue.getDdprice().intValue()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("type"), goodsValue.getDdgoodstype()));
    }


}
