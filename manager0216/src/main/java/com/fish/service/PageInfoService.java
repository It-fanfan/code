package com.fish.service;

import com.fish.dao.primary.mapper.PageInfoMapper;
import com.fish.dao.primary.model.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageInfoService
{
    @Autowired
    PageInfoMapper pageInfoMapper;

    public List<PageInfo> selectFirstParent()
    {
        List<PageInfo> firstParentPage = pageInfoMapper.selectFirstParent();
        return firstParentPage;
    }

    public List<PageInfo> selectParent(int index)
    {
        List<PageInfo> parentPage = pageInfoMapper.selectParent(index);
        return parentPage;
    }
}
