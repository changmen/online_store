package com.example.onlinestore.bean;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址业务对象
 */
@Data
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;

    // 用户ID
    private Long memberId;

    // 收货人姓名
    private String consigneeName;

    // 收货人电话
    private String consigneePhone;

    // 省份
    private String province;

    // 城市
    private String city;

    // 区县
    private String district;

    // 详细地址
    private String detailAddress;

    // 邮政编码
    private String postalCode;

    // 是否为默认地址
    private Boolean isDefault;

    // 地址标签 (如：家、公司、学校等)
    private String tag;

    // 创建时间
    private LocalDateTime createdAt;

    // 更新时间
    private LocalDateTime updatedAt;

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        if (province != null) {
            fullAddress.append(province);
        }
        if (city != null) {
            fullAddress.append(city);
        }
        if (district != null) {
            fullAddress.append(district);
        }
        if (detailAddress != null) {
            fullAddress.append(detailAddress);
        }
        return fullAddress.toString();
    }
}