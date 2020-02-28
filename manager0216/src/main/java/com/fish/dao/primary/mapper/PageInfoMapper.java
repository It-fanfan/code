package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageInfoMapper
{


    List<PageInfo> selectFirstParent();

    List<PageInfo> selectParent(@Param("pageParentId") Integer index);
}
