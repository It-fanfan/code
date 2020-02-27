package com.fish.dao.second.mapper;

import com.fish.dao.second.model.WxGroupManager;

import java.util.List;

public interface WxGroupManagerMapper {

    List<WxGroupManager> selectAll();
    List<WxGroupManager> selectAllConfigSQL(String sql);
    int updateWxGroupManager(WxGroupManager wxGroupManager);
    int updateConfigConfirm(WxGroupManager wxGroupManager);
    int updateByPrimaryKeySelective(WxGroupManager wxGroupManager);
}
