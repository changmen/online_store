package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Address;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地址响应DTO
 */
@Data
public class AddressResponse {

    private Long id;
    private Long memberId;
    private String consigneeName;
    private String consigneePhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String postalCode;
    private Boolean isDefault;
    private String tag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 从Address对象创建响应DTO
     */
    public static AddressResponse fromAddress(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setMemberId(address.getMemberId());
        response.setConsigneeName(address.getConsigneeName());
        response.setConsigneePhone(address.getConsigneePhone());
        response.setProvince(address.getProvince());
        response.setCity(address.getCity());
        response.setDistrict(address.getDistrict());
        response.setDetailAddress(address.getDetailAddress());
        response.setPostalCode(address.getPostalCode());
        response.setIsDefault(address.getIsDefault());
        response.setTag(address.getTag());
        response.setCreatedAt(address.getCreatedAt());
        response.setUpdatedAt(address.getUpdatedAt());
        return response;
    }

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