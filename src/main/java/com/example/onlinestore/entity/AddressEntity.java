package com.example.onlinestore.entity;

import com.example.onlinestore.bean.Address;
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
    private static final long serialVersionUID = -5135051549283930314L;
    
    // 主键ID
    private Long id;
    
    // 用户ID
    private Long memberId;
    
    // 收货人姓名
    private String receiverName;
    
    // 收货人电话
    private String receiverPhone;
    
    // 省份
    private String province;
    
    // 城市
    private String city;
    
    // 区/县
    private String district;
    
    // 详细地址
    private String detailAddress;
    
    // 是否为默认地址 (0: 非默认, 1: 默认)
    private Integer isDefault;
    
    // 创建时间
    private LocalDateTime createdAt;
    
    // 更新时间
    private LocalDateTime updatedAt;
    
    public Address toAddress() {
        Address address = new Address();
        address.setId(this.id);
        address.setMemberId(this.memberId);
        address.setReceiverName(this.receiverName);
        address.setReceiverPhone(this.receiverPhone);
        address.setProvince(this.province);
        address.setCity(this.city);
        address.setDistrict(this.district);
        address.setDetailAddress(this.detailAddress);
        address.setDefault(this.isDefault != null && this.isDefault == 1);
        return address;
    }
    
    public static AddressEntity fromAddress(Address address) {
        AddressEntity entity = new AddressEntity();
        entity.setId(address.getId());
        entity.setMemberId(address.getMemberId());
        entity.setReceiverName(address.getReceiverName());
        entity.setReceiverPhone(address.getReceiverPhone());
        entity.setProvince(address.getProvince());
        entity.setCity(address.getCity());
        entity.setDistrict(address.getDistrict());
        entity.setDetailAddress(address.getDetailAddress());
        entity.setIsDefault(address.isDefault() ? 1 : 0);
        return entity;
    }
}