package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.WxConfig;

import java.util.List;

public interface WxConfigMapper {
    int deleteByPrimaryKey(String ddappid);

    int insert(WxConfig record);

    int insertSelective(WxConfig record);

    WxConfig selectByPrimaryKey(String ddappid);

    int updateByPrimaryKeySelective(WxConfig record);

    int updateByPrimaryKey(WxConfig record);

    List<WxConfig> selectAll();
}