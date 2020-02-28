package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.CustomerMaterial;
import org.springframework.stereotype.Component;

@Component
public interface CustomerMaterialMapper {

    CustomerMaterial selectMediaId();
    int deleteByPrimaryKey(Long id);

    int insert(CustomerMaterial record);

    int insertSelective(CustomerMaterial record);

    CustomerMaterial selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerMaterial record);

    int updateByPrimaryKey(CustomerMaterial record);
}