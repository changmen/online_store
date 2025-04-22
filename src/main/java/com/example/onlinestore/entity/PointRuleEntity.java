package com.example.onlinestore.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PointRuleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2213023662906737420L;

    /**
     * 主键ID - 唯一标识符
     */
    private Long id;

    /**
     * 名称 - 实体对象的名称标识
     */
    private String name;

    /**
     * 描述信息 - 对实体对象的详细说明
     */
    private String description;

    /**
     * 积分/分数值 - 使用BigDecimal类型保证数值精度
     * 适用于金融计算或需要高精度计算的场景
     */
    private BigDecimal points;

    /**
     * 状态标识 - 表示实体对象的当前状态
     * 示例值：ACTIVE/INACTIVE/PENDING 等状态枚举
     */
    private String status;

    /**
     * 创建时间 - 记录数据的初始创建时间
     * 使用LocalDateTime类型存储日期时间信息
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间 - 记录数据的最近修改时间
     * 每次数据更新时应自动更新该字段
     */
    private LocalDateTime updatedAt;

} 