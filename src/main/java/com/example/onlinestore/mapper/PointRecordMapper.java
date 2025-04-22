package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.PointRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PointRecordMapper {
    /**
     * 根据主键ID查询积分记录实体
     *
     * @param id 积分记录主键ID
     * @return 对应的积分记录实体，未找到时返回null
     */
    PointRecordEntity findById(Long id);

    /**
     * 根据会员ID查询关联的积分记录列表
     *
     * @param memberId 会员唯一标识ID
     * @return 该会员的所有积分记录（可能为空列表）
     */
    List<PointRecordEntity> findByMemberId(Long memberId);

    /**
     * 根据订单ID查询关联的积分记录列表
     *
     * @param orderId 订单唯一标识ID
     * @return 该订单关联的积分记录（可能为空列表）
     */
    List<PointRecordEntity> findByOrderId(Long orderId);

    /**
     * 插入新的积分记录
     *
     * @param record 包含积分明细的实体对象
     * @return 受影响的行数（通常为1表示插入成功）
     */
    int insert(PointRecordEntity record);

    /**
     * 获取会员当前积分余额
     *
     * @param memberId 会员唯一标识ID
     * @return 当前可用积分余额（可能返回null表示无记录）
     */
    BigDecimal getMemberPointBalance(Long memberId);

} 