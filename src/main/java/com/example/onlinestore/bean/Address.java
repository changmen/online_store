package com.example.onlinestore.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = -5574250595988808929L;
    /**
     * 地址唯一标识符，主键
     */
    private Long id;

    /**
     * 关联会员ID，标识地址所属用户
     */
    private Long memberId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人联系电话
     */
    private String receiverPhone;

    /**
     * 省份/直辖市名称
     */
    private String province;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 区/县名称
     */
    private String district;

    /**
     * 详细街道地址（不含省市县信息）
     */
    private String detailAddress;

    /**
     * 是否为默认地址标识
     * true: 默认地址，false: 非默认地址
     */
    private Boolean isDefault;

}
