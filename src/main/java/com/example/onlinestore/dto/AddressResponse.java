package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Address;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址响应DTO
 */
@Setter
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class AddressResponse implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 5555666677778888999L;
    
    /**
     * 地址ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long memberId;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区县
     */
    private String district;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 是否为默认地址
     */
    private Boolean isDefault;
    
    /**
     * 地址标签
     */
    private String addressLabel;
    
    /**
     * 完整地址
     */
    private String fullAddress;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 从 Address 对象创建响应DTO
     */
    public static AddressResponse of(Address address) {
        if (address == null) {
            return null;
        }
        
        return AddressResponse.builder()
                .id(address.getId())
                .memberId(address.getMemberId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .detailAddress(address.getDetailAddress())
                .postalCode(address.getPostalCode())
                .isDefault(address.getIsDefault())
                .addressLabel(address.getAddressLabel())
                .fullAddress(address.getFullAddress())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}