package com.example.onlinestore.entity;

import com.example.onlinestore.bean.Address;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址实体类
 */
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

    // 是否为默认地址 (0:否, 1:是)
    private Integer isDefault;

    // 地址标签 (如：家、公司、学校等)
    private String tag;

    // 创建时间
    private LocalDateTime createdAt;

    // 更新时间
    private LocalDateTime updatedAt;

    /**
     * 转换为业务对象
     */
    public Address toAddress() {
        Address address = new Address();
        address.setId(this.id);
        address.setMemberId(this.memberId);
        address.setConsigneeName(this.consigneeName);
        address.setConsigneePhone(this.consigneePhone);
        address.setProvince(this.province);
        address.setCity(this.city);
        address.setDistrict(this.district);
        address.setDetailAddress(this.detailAddress);
        address.setPostalCode(this.postalCode);
        address.setIsDefault(this.isDefault == 1);
        address.setTag(this.tag);
        address.setCreatedAt(this.createdAt);
        address.setUpdatedAt(this.updatedAt);
        return address;
    }

    /**
     * 从业务对象转换
     */
    public static AddressEntity fromAddress(Address address) {
        AddressEntity entity = new AddressEntity();
        entity.setId(address.getId());
        entity.setMemberId(address.getMemberId());
        entity.setConsigneeName(address.getConsigneeName());
        entity.setConsigneePhone(address.getConsigneePhone());
        entity.setProvince(address.getProvince());
        entity.setCity(address.getCity());
        entity.setDistrict(address.getDistrict());
        entity.setDetailAddress(address.getDetailAddress());
        entity.setPostalCode(address.getPostalCode());
        entity.setIsDefault(address.getIsDefault() ? 1 : 0);
        entity.setTag(address.getTag());
        entity.setCreatedAt(address.getCreatedAt());
        entity.setUpdatedAt(address.getUpdatedAt());
        return entity;
    }
}