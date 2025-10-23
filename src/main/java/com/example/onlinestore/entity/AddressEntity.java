package com.example.onlinestore.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址信息实体类
 * 用于映射数据库中的地址表
 */
@Data
@EqualsAndHashCode
@ToString
public class AddressEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址ID，主键
     */
    private Long id;

    /**
     * 用户ID，关联用户表
     */
    private Long memberId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverPhone;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县代码
     */
    private String districtCode;

    /**
     * 区县名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 是否为默认地址 1-是 0-否
     */
    private Integer isDefault;

    /**
     * 地址标签（如：家、公司等）
     */
    private String tag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除 1-已删除 0-未删除
     */
    private Integer isDeleted;
}