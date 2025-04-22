package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 通过订单主键ID查询订单实体
     * @param id 订单记录主键ID（非空）
     * @return 订单实体对象，未找到时返回null
     */
    OrderEntity findById(Long id);

    /**
     * 通过唯一订单编号查询订单实体
     * @param orderNo 系统生成的唯一订单编号（非空）
     * @return 订单实体对象，未找到时返回null
     */
    OrderEntity findByOrderNo(String orderNo);

    /**
     * 查询指定会员的所有订单记录
     * @param memberId 会员账号主键ID（非空）
     * @return 订单实体列表（始终返回非null集合，无结果时返回空集合）
     */
    List<OrderEntity> findByMemberId(Long memberId);

    /**
     * 插入新的订单记录
     * @param order 包含完整订单信息的实体对象（非空）
     * @return 受影响的行数（通常为1表示插入成功）
     */
    int insert(OrderEntity order);

    /**
     * 更新订单全部字段信息
     * @param order 必须包含有效主键的订单实体对象（非空）
     * @return 受影响的行数（通常为1表示更新成功）
     */
    int update(OrderEntity order);

    /**
     * 更新订单状态字段
     * @param id 需要修改的订单主键ID（非空）
     * @param status 新的订单状态值（非空）
     * @return 受影响的行数（0表示未找到对应记录，1表示更新成功）
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 通过订单主键ID和会员主键ID查询订单实体
     * @param id 订单主键ID（非空）
     * @param memberId 会员主键ID（非空）
     * @return 订单实体对象，未找到时返回null
     */
    OrderEntity findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

} 