package com.example.onlinestore.utils;

/**
 * 订单ID生成器接口定义，用于生成唯一的订单标识字符串
 *
 * 该接口定义了生成订单ID的规范方法，具体实现类应根据业务需求实现不同的生成策略，
 * 例如：基于时间戳、分布式ID算法或数据库序列等方式的订单ID生成方案
 *
 */
public interface OrderNoGenerator {
    /**
     * 生成符合业务规则的唯一订单标识
     *
     * 实现类应当确保：
     * 1. 在分布式环境下生成的ID具有全局唯一性
     * 2. ID生成过程线程安全
     * 3. 避免产生可预测的连续ID序列
     */
    String generateOrderNo();
}
