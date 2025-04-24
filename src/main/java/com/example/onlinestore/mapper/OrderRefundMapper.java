package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderRefundEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderRefundMapper {
    /**
     * 根据主键ID查询订单退款记录
     *
     * @param id 订单退款记录主键ID
     * @return 符合ID的订单退款实体对象，若不存在则返回null
     */
    OrderRefundEntity findById(Long id);

    /**
     * 根据退款业务单号查询订单退款记录
     *
     * @param refundNo 系统生成的唯一退款单号
     * @return 符合退款单号的订单退款实体对象，若不存在则返回null
     */
    OrderRefundEntity findByRefundNo(String refundNo);

    /**
     * 根据关联订单ID查询退款记录
     *
     * @param orderId 关联的原始订单ID
     * @return 该订单对应的退款实体对象，若未发起退款则返回null
     */
    OrderRefundEntity findByOrderId(Long orderId);

    /**
     * 新增订单退款记录
     *
     * @param refund 包含完整退款信息的实体对象（要求非空）
     * @return 受影响的行数，通常返回1表示插入成功
     */
    int insert(OrderRefundEntity refund);

    /**
     * 更新订单退款记录（全字段更新）
     *
     * @param refund 包含更新数据的实体对象，必须包含有效主键ID
     * @return 受影响的行数，返回0表示记录不存在
     */
    int update(OrderRefundEntity refund);

    /**
     * 更新退款记录状态
     *
     * @param id 要更新的退款记录主键ID
     * @param status 新的状态值（需符合系统定义的状态枚举）
     * @return 受影响的行数，返回0表示记录不存在
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 根据会员ID和创建时间查询退款记录
     *
     * @param memberId 会员ID
     * @param createdAt 创建时间
     * @return 符合条件的退款记录列表
     */
    List<OrderRefundEntity> findByMemberIdAndCreatedAtAfter(@Param("memberId") Long memberId, @Param("createdAt") LocalDateTime createdAt);

} 