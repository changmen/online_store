package com.example.onlinestore.bean;

import com.example.onlinestore.enums.PointRecordType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PointRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = -354852361222888888L;

    /**
     * 主键ID，唯一标识记录
     * 采用自增策略生成
     */
    private Long id;

    /**
     * 关联的会员ID
     * 用于标识本条记录所属的会员
     */
    private Long memberId;

    /**
     * 关联的订单, 积分和订单关联（奖励或消耗）记录
     */
    private String orderNo;

    /**
     * 积分变动数值
     * 使用BigDecimal保证金额计算的精确性
     */
    private BigDecimal points;

    /**
     * 积分记录类型
     * 区分积分增加/消耗等操作类型
     */
    private PointRecordType type;

    /**
     * 操作描述信息
     * 记录积分变动的具体原因或备注
     */
    private String description;
}
