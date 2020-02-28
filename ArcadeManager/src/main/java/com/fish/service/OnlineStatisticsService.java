package com.fish.service;

import com.fish.dao.primary.mapper.OnlineStatisticsMapper;
import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class OnlineStatisticsService implements BaseService<OnlineStatistics>{
    @Autowired
    OnlineStatisticsMapper onlineStatisticsMapper;

    @Override
    public List<OnlineStatistics> selectAll(GetParameter parameter) {
        return onlineStatisticsMapper.selectAll(parameter);
    }

    public int insert(OnlineStatistics productInfo) {
        int insert = onlineStatisticsMapper.insert(productInfo);
        return insert;
    }

    public int updateByPrimaryKeySelective(OnlineStatistics productInfo) {
        int modify = onlineStatisticsMapper.updateByPrimaryKeySelective(productInfo);
        return modify;
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<OnlineStatistics> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(OnlineStatistics onlineStatistics, Map<String, String> searchData) {
        return false;
    }


}
