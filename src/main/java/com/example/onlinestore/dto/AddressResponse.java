package com.example.onlinestore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址响应DTO
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddressResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890123456785L;
    
    private Long id;
    private Long memberId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String zipCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
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