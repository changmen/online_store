package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AddressEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5181831339517730024L;

    // 用户地址的唯一标识符
    private Long id;

    // 所属会员的ID
    private Long memberId;

    // 收件人姓名
    private String receiverName;

    // 收件人联系电话
    private String receiverPhone;

    // 省份信息
    private String province;

    // 城市信息
    private String city;

    // 区/县信息
    private String district;

    // 详细地址信息
    private String detailAddress;

    // 是否为默认地址（true表示默认，false表示非默认）
    private Boolean isDefault;

    // 记录创建时间
    private LocalDateTime createdAt;

    // 记录最后更新时间
    private LocalDateTime updatedAt;
}