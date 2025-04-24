package com.example.onlinestore.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PointRecordEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2334802280281976316L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 关联会员ID，表示积分所属用户
     */
    private Long memberId;
    /**
     * 关联订单号，记录积分产生来源的订单
     */
    private String orderNo;
    /**
     * 积分数值（高精度），使用BigDecimal保证计算精度
     */
    private BigDecimal points;
    /**
     * 积分变动类型（如：消费获得/退款扣除等）
     */
    private String type;
    /**
     * 积分变动描述，记录业务场景说明
     */
    private String description;
    /**
     * 记录创建时间，由数据库自动维护
     */
    private LocalDateTime createdAt;
    /**
     * 记录最后更新时间，由数据库自动维护
     */
    private LocalDateTime updatedAt;

} 