package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.PointRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointRuleMapper {
    PointRuleEntity findById(Long id);

    List<PointRuleEntity> findAllActive();

    int insert(PointRuleEntity rule);

    int update(PointRuleEntity rule);

    int updateStatus(Long id, Integer status);
} 