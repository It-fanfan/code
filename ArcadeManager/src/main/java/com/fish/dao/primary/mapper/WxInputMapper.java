package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.WxInput;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WxInputMapper {
    int deleteByPrimaryKey(Date insertTime);

    int insert(WxInput record);

    int insertSelective(WxInput record);

    WxInput selectByPrimaryKey(Date insertTime);

    int updateByPrimaryKeySelective(WxInput record);

    int updateByPrimaryKey(WxInput record);

    List<WxInput> selectAll();

    List<Map> selectBySQL(String SQL);
}