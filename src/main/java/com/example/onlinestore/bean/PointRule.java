package com.example.onlinestore.bean;

import com.example.onlinestore.enums.PointRuleStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PointRule implements Serializable {
    @Serial
    private static final long serialVersionUID = 569206843034914160L;

    /**
     * 唯一标识符
     * 用于唯一标识积分规则实例
     */
    private Long id;

    /**
     * 规则名称
     * 记录积分规则的显示名称，长度建议不超过64字符
     */
    private String name;

    /**
     * 规则描述
     * 详细说明积分规则的适用场景和计算逻辑，支持富文本
     */
    private String description;

    /**
     * 积分数值
     * 使用BigDecimal类型保障金额/积分计算的精确性
     * 存储格式：DECIMAL(19,4) 建议范围：0.0001 ~ 999999999999.9999
     */
    private BigDecimal points;

    /**
     * 规则状态
     * 枚举值包括：ENABLED(生效)/DISABLED(停用)/ARCHIVED(归档)
     */
    private PointRuleStatus status;

} 