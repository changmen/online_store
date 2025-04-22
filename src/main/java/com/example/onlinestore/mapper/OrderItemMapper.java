package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    /**
     * 根据订单ID查询关联的订单项列表
     *
     * @param orderId 订单的唯一标识符（Long类型）
     * @return 包含关联订单项的列表（List<OrderItemEntity>），当无匹配项时返回空列表
     */
    List<OrderItemEntity> findByOrderId(Long orderId);

    /**
     * 插入单个订单项记录
     *
     * @param item 包含订单项数据的实体对象（OrderItemEntity）
     * @return 受影响的行数（int），通常为1（成功插入）或0（插入失败）
     */
    int insert(OrderItemEntity item);

    /**
     * 批量插入多个订单项记录
     *
     * @param items 包含多个订单项数据的实体对象列表（List<OrderItemEntity>）
     * @return 受影响的行数总和（int），数值等于成功插入的记录数
     *         使用批量插入比单条插入具有更好的数据库操作效率
     */
    int insertBatch(List<OrderItemEntity> items);

} 