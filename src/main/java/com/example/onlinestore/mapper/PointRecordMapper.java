package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.PointRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PointRecordMapper {
    PointRecordEntity findById(Long id);

    List<PointRecordEntity> findByMemberId(Long memberId);

    List<PointRecordEntity> findByOrderId(Long orderId);

    int insert(PointRecordEntity record);

    BigDecimal getMemberPointBalance(Long memberId);
} 