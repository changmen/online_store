package com.example.onlinestore.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址信息Bean类
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890123456788L;
    
    // 地址ID
    private Long id;
    
    // 用户ID
    private Long memberId;
    
    // 收货人姓名
    private String receiverName;
    
    // 收货人手机号
    private String receiverPhone;
    
    // 省份
    private String province;
    
    // 城市
    private String city;
    
    // 区/县
    private String district;
    
    // 详细地址
    private String detailAddress;
    
    // 邮政编码
    private String zipCode;
    
    // 是否为默认地址
    private Boolean isDefault;
    
    // 创建时间
    private LocalDateTime createdAt;
    
    // 更新时间
    private LocalDateTime updatedAt;
    
    /**
     * 获取完整地址字符串
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) {
            sb.append(province);
        }
        if (city != null) {
            sb.append(city);
        }
        if (district != null) {
            sb.append(district);
        }
        if (detailAddress != null) {
            sb.append(detailAddress);
        }
        return sb.toString();
    }
}