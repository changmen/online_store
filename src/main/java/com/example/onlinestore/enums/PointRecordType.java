package com.example.onlinestore.enums;

/**
 * 积分记录类型枚举
 * 定义会员积分变动的类型
 */
public enum PointRecordType {
    /**
     * 获得积分 - 通过购物、签到等方式获得积分
     */
    EARN,    // 获得积分
    /**
     * 消费积分 - 使用积分抵扣订单金额等
     */
    CONSUME  // 消费积分
} 