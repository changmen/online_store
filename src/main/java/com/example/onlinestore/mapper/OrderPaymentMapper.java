package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderPaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderPaymentMapper {
    /**
     * 根据主键ID查询订单支付记录
     *
     * @param id 支付记录主键ID
     * @return 订单支付实体对象，未找到时返回null
     */
    OrderPaymentEntity findById(Long id);

    /**
     * 根据支付流水号查询支付记录
     *
     * @param paymentNo 支付系统生成的唯一流水号
     * @return 匹配的支付记录对象，未找到时返回null
     */
    OrderPaymentEntity findByPaymentNo(String paymentNo);

    /**
     * 根据订单ID查询支付记录
     *
     * @param orderId 关联的订单主键ID
     * @return 对应的支付记录对象，未找到时返回null
     */
    OrderPaymentEntity findByOrderId(Long orderId);

    /**
     * 插入新的支付记录
     *
     * @param payment 待插入的支付记录实体对象
     * @return 受影响的行数，通常为1表示插入成功
     */
    int insert(OrderPaymentEntity payment);

    /**
     * 更新支付记录（全字段更新）
     *
     * @param payment 待更新的支付记录实体对象，必须包含有效ID
     * @return 受影响的行数，通常为1表示更新成功
     */
    int update(OrderPaymentEntity payment);

    /**
     * 更新支付记录状态
     *
     * @param id 要更新的支付记录主键ID
     * @param status 新的支付状态值
     * @return 受影响的行数，通常为1表示更新成功
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
} 